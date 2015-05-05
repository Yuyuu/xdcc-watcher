package watcher.job

import org.quartz.JobExecutionContext
import spock.lang.Specification
import watcher.irc.bot.BotWatcher
import watcher.irc.bot.WatcherFactory
import watcher.schedule.configuration.IrcConfiguration

class BotWatchingJobTest extends Specification {

  BotWatchingJob job
  WatcherFactory watcherFactory = Mock(WatcherFactory)
  IrcConfiguration ircConfiguration = Mock(IrcConfiguration)

  def setup() {
    ircConfiguration.server = "irc.server"
    ircConfiguration.channel = "irc.channel"
    job = new BotWatchingJob(watcherFactory, ircConfiguration)
  }

  def "connects the bot to the channel and ends"() {
    given:
    BotWatcher botWatcher = Mock(BotWatcher)
    watcherFactory.createWatcher(BotWatcher.class) >> botWatcher

    when:
    JobExecutionContext context = Mock(JobExecutionContext)
    job.execute(context)

    then:
    1 * botWatcher.connectToServer("irc.server")
    then:
    1 * botWatcher.joinServerChannel("irc.channel")
  }
}
