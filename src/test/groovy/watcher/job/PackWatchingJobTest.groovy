package watcher.job

import org.junit.Rule
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import spock.lang.Specification
import watcher.infrastructure.persistence.memory.WithMemoryRepository
import watcher.irc.bot.PackWatcher
import watcher.irc.bot.WatcherFactory
import watcher.model.RepositoryLocator
import watcher.model.bot.Bot

class PackWatchingJobTest extends Specification {

  @Rule
  WithMemoryRepository memoryRepository = new WithMemoryRepository()

  PackWatchingJob job
  WatcherFactory watcherFactory = Mock(WatcherFactory)

  JobExecutionContext context = Mock(JobExecutionContext)
  JobDataMap dataMap = Mock(JobDataMap)

  def setup() {
    job = new PackWatchingJob(watcherFactory)

    JobDetail jobDetail = Mock(JobDetail)
    context.getJobDetail() >> jobDetail
    jobDetail.getJobDataMap() >> dataMap
  }

  def "uses circular offsets"() {
    given:
    withBotsInRepository(3)
    job.setCurrentOffset(2)
    job.setMaxToUpdatePerJob(2)

    when:
    job.execute(context)

    then:
    1 * watcherFactory.createPackWatcherWithObjective({ List bots ->
      bots.findIndexOf { Bot bot -> bot.nickname() == "bot_2"} == 0 &&
          bots.findIndexOf { Bot bot -> bot.nickname() == "bot_0"} == 1
    }) >> Mock(PackWatcher)
    then:
    1 * dataMap.put("currentOffset", 1)
  }

  def "calculates a correct offset if no cycling is required"() {
    given:
    withBotsInRepository(4)
    job.setCurrentOffset(0)
    job.setMaxToUpdatePerJob(2)

    and:
    watcherFactory.createPackWatcherWithObjective(_ as List) >> Mock(PackWatcher)

    when:
    job.execute(context)

    then:
    1 * dataMap.put("currentOffset", 2)
  }

  def "connects the bot to the channel and ends"() {
    given:
    withBotsInRepository(2)
    job.setCurrentOffset(0)
    job.setMaxToUpdatePerJob(1)

    and:
    PackWatcher packWatcher = Mock(PackWatcher)
    watcherFactory.createPackWatcherWithObjective(_ as List) >> packWatcher

    when:
    job.execute(context)

    then:
    1 * packWatcher.connectToServer("irc.otaku-irc.fr")
    then:
    1 * packWatcher.joinServerChannel("#serial_us")
  }

  private static void withBotsInRepository(int n) {
    def repo = RepositoryLocator.bots();
    n.times {
      repo.add(new Bot("bot_$it"))
    }
  }
}
