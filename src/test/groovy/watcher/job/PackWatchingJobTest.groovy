package watcher.job

import org.junit.Rule
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import spock.lang.Specification
import spock.lang.Unroll
import watcher.infrastructure.persistence.memory.WithMemoryRepository
import watcher.irc.bot.PackWatcher
import watcher.irc.bot.WatcherFactory
import watcher.model.RepositoryLocator
import watcher.model.bot.Bot
import watcher.schedule.configuration.IrcConfiguration

class PackWatchingJobTest extends Specification {

  @Rule
  WithMemoryRepository memoryRepository = new WithMemoryRepository()

  PackWatchingJob job
  WatcherFactory watcherFactory = Mock(WatcherFactory)
  IrcConfiguration ircConfiguration = Mock(IrcConfiguration)

  JobExecutionContext context = Mock(JobExecutionContext)
  JobDataMap dataMap = Mock(JobDataMap)

  def setup() {
    ircConfiguration.server = "irc.server"
    ircConfiguration.channel = "irc.channel"
    job = new PackWatchingJob(watcherFactory, ircConfiguration)

    JobDetail jobDetail = Mock(JobDetail)
    context.getJobDetail() >> jobDetail
    jobDetail.getJobDataMap() >> dataMap
  }

  def "uses circular offsets"() {
    given:
    withBotsInRepository(3)
    job.setCurrentOffset(2)
    job.setMaxBotsToUpdatePerJob(2)

    when:
    job.execute(context)

    then:
    1 * watcherFactory.createPackWatcherWithObjective({ List bots -> bots.size() == 2}) >> Mock(PackWatcher)
  }

  @Unroll
  def "calculates a correct offset"() {
    given:
    withBotsInRepository(botsInRepository)
    job.setCurrentOffset(currentOffset)
    job.setMaxBotsToUpdatePerJob(maxBotsToUpdate)

    and:
    watcherFactory.createPackWatcherWithObjective(_ as List) >> Mock(PackWatcher)

    when:
    job.execute(context)

    then:
    1 * dataMap.put("currentOffset", nextOffset)

    where:
    botsInRepository | currentOffset | maxBotsToUpdate || nextOffset
    4                | 0             | 2               || 2
    4                | 2             | 2               || 0
  }

  def "connects the bot to the channel and ends"() {
    given:
    withBotsInRepository(2)
    job.setCurrentOffset(0)
    job.setMaxBotsToUpdatePerJob(1)

    and:
    PackWatcher packWatcher = Mock(PackWatcher)
    watcherFactory.createPackWatcherWithObjective(_ as List) >> packWatcher

    when:
    job.execute(context)

    then:
    1 * packWatcher.connectToServer("irc.server")
    then:
    1 * packWatcher.joinServerChannel("irc.channel")
  }

  private static void withBotsInRepository(int n) {
    def repo = RepositoryLocator.bots();
    n.times {
      repo.add(new Bot("bot_$it"))
    }
  }
}
