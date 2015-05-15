package watcher.job;

import org.jibble.pircbot.IrcException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watcher.irc.bot.PackWatcher;
import watcher.irc.bot.WatcherFactory;
import watcher.model.RepositoryLocator;
import watcher.model.bot.Bot;
import watcher.model.bot.BotRepository;
import watcher.schedule.configuration.IrcConfiguration;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@PersistJobDataAfterExecution
public class PackWatchingJob implements Job {

  @Inject
  public PackWatchingJob(WatcherFactory watcherFactory, IrcConfiguration ircConfiguration) {
    this.watcherFactory = watcherFactory;
    this.ircConfiguration = ircConfiguration;
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    LOGGER.debug("Beginning of {}", getClass().getSimpleName());

    final long numberOfBotsInRepository = RepositoryLocator.bots().count();
    numberOfBotsToUpdateBeforeCycling = numberOfBotsInRepository - currentOffset;
    final List<Bot> botsToUpdate = determineBotsToUpdate();

    final PackWatcher packWatcher = watcherFactory.createPackWatcherWithObjective(botsToUpdate);

    try {
      packWatcher.connectToServer(ircConfiguration.server);
      packWatcher.joinServerChannel(ircConfiguration.channel);

      final long nextOffset = calculateNextOffset(numberOfBotsInRepository);
      context.getJobDetail().getJobDataMap().put("currentOffset", nextOffset);
    } catch (IrcException | IOException e) {
      LOGGER.error("Failed to connect the watcher to the server", e);
    }

    LOGGER.debug("End of {}", getClass().getSimpleName());
  }

  private List<Bot> determineBotsToUpdate() {
    final BotRepository botRepository = RepositoryLocator.bots();
    final List<Bot> botsToUpdate = botRepository.paginateWithoutLoadingPacks((int) maxBotsToUpdatePerJob, (int) currentOffset);

    if (numberOfBotsToUpdateBeforeCycling < maxBotsToUpdatePerJob) {
      botsToUpdate.addAll(
          botRepository.paginateWithoutLoadingPacks((int) (maxBotsToUpdatePerJob - numberOfBotsToUpdateBeforeCycling), 0)
      );
    }

    return botsToUpdate;
  }

  private long calculateNextOffset(long numberOfBotsInRepository) {
    long nextOffset;
    if (numberOfBotsToUpdateBeforeCycling < maxBotsToUpdatePerJob) {
      nextOffset = maxBotsToUpdatePerJob - numberOfBotsToUpdateBeforeCycling;
    } else {
      final long assumptiveOffset = currentOffset + maxBotsToUpdatePerJob;
      nextOffset = (assumptiveOffset == numberOfBotsInRepository) ? 0 : assumptiveOffset;
    }
    return nextOffset;
  }

  public void setCurrentOffset(long currentOffset) {
    this.currentOffset = currentOffset;
  }

  public void setMaxBotsToUpdatePerJob(long maxBotsToUpdatePerJob) {
    this.maxBotsToUpdatePerJob = maxBotsToUpdatePerJob;
  }

  private long currentOffset;
  private long maxBotsToUpdatePerJob;
  private long numberOfBotsToUpdateBeforeCycling;
  private final WatcherFactory watcherFactory;
  private final IrcConfiguration ircConfiguration;
  private static final Logger LOGGER = LoggerFactory.getLogger(PackWatchingJob.class);
}
