package watcher.job;

import org.jibble.pircbot.IrcException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watcher.irc.bot.PackWatcher;
import watcher.irc.bot.WatcherFactory;
import watcher.model.RepositoryLocator;
import watcher.model.bot.Bot;
import watcher.model.bot.BotRepository;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class PackWatchingJob implements Job {

  @Inject
  public PackWatchingJob(WatcherFactory watcherFactory) {
    this.watcherFactory = watcherFactory;
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    LOGGER.debug("Beginning of {}", getClass().getSimpleName());

    this.numberOfBotsToUpdateBeforeCycling = RepositoryLocator.bots().count() - currentOffset;
    final List<Bot> nicknamesOfBotsToUpdate = determineBotsToUpdate();

    final PackWatcher packWatcher = watcherFactory.createPackWatcherWithObjective(nicknamesOfBotsToUpdate);

    try {
      packWatcher.connectToServer("irc.otaku-irc.fr");
      packWatcher.joinServerChannel("#serial_us");

      final long nextOffset = calculateNextOffset();
      context.getJobDetail().getJobDataMap().put("currentOffset", nextOffset);
    } catch (IrcException | IOException e) {
      LOGGER.error("Failed to connect the watcher to the server", e);
    }

    LOGGER.debug("End of {}", getClass().getSimpleName());
  }

  private List<Bot> determineBotsToUpdate() {
    final BotRepository botRepository = RepositoryLocator.bots();
    final List<Bot> botsToUpdate = botRepository.paginate((int) maxBotsToUpdatePerJob, (int) currentOffset);

    if (numberOfBotsToUpdateBeforeCycling < maxBotsToUpdatePerJob) {
      botsToUpdate.addAll(
          botRepository.paginate((int) (maxBotsToUpdatePerJob - numberOfBotsToUpdateBeforeCycling), 0)
      );
    }

    return botsToUpdate;
  }

  private long calculateNextOffset() {
    long nextOffset;
    if (numberOfBotsToUpdateBeforeCycling < maxBotsToUpdatePerJob) {
      nextOffset = maxBotsToUpdatePerJob - numberOfBotsToUpdateBeforeCycling;
    } else {
      nextOffset = currentOffset + maxBotsToUpdatePerJob;
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
  private static final Logger LOGGER = LoggerFactory.getLogger(PackWatchingJob.class);
}
