package watcher.job;

import org.jibble.pircbot.IrcException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watcher.irc.bot.BotWatcher;
import watcher.irc.bot.WatcherFactory;
import watcher.schedule.configuration.IrcConfiguration;

import javax.inject.Inject;
import java.io.IOException;

public class BotWatchingJob implements Job {

  @Inject
  public BotWatchingJob(WatcherFactory watcherFactory, IrcConfiguration ircConfiguration) {
    this.watcherFactory = watcherFactory;
    this.ircConfiguration = ircConfiguration;
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    final BotWatcher botWatcher = watcherFactory.createWatcher(BotWatcher.class);

    LOGGER.debug("Beginning of {}", getClass().getSimpleName());
    try {
      botWatcher.connectToServer(ircConfiguration.server);
      botWatcher.joinServerChannel(ircConfiguration.channel);
    } catch (IrcException | IOException e) {
      LOGGER.error("Failed to connect the watcher to the server", e);
    }
    LOGGER.debug("End of {}", getClass().getSimpleName());
  }

  private final WatcherFactory watcherFactory;
  private final IrcConfiguration ircConfiguration;
  private static final Logger LOGGER = LoggerFactory.getLogger(BotWatchingJob.class);
}
