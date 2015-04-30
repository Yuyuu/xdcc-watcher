package watcher.job

import org.quartz.JobExecutionContext
import spock.lang.Specification
import watcher.irc.bot.BotWatcher
import watcher.irc.bot.WatcherFactory

class BotWatchingJobTest extends Specification {

  BotWatchingJob job
  WatcherFactory watcherFactory

  def setup() {
    watcherFactory = Mock(WatcherFactory)
    job = new BotWatchingJob(watcherFactory)
  }

  def "connects the bot to the channel and ends"() {
    given:
    BotWatcher botWatcher = Mock(BotWatcher)
    watcherFactory.createWatcher(BotWatcher.class) >> botWatcher

    when:
    JobExecutionContext context = Mock(JobExecutionContext)
    job.execute(context)

    then:
    1 * botWatcher.connectToServer("irc.otaku-irc.fr")
    then:
    1 * botWatcher.joinServerChannel("#serial_us")
  }
}
