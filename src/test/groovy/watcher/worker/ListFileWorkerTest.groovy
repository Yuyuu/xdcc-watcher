package watcher.worker

import org.junit.Rule
import spock.lang.Specification
import watcher.infrastructure.persistence.memory.WithMemoryRepository
import watcher.model.RepositoryLocator
import watcher.model.bot.Bot
import watcher.model.bot.Pack
import watcher.worker.parser.ListFileParser

@SuppressWarnings("GroovyAccessibility")
class ListFileWorkerTest extends Specification {

  @Rule
  WithMemoryRepository memoryRepository = new WithMemoryRepository()

  ListFileWorker listFileWorker
  ListFileParser listFileParser

  def setup() {
    listFileParser = Mock(ListFileParser)
    listFileWorker = new ListFileWorker(listFileParser)
  }

  def "updates the available bots"() {
    given:
    def joeBot = new Bot("joe")
    RepositoryLocator.bots().add(joeBot)

    and:
    def file = new File("list.txt")
    listFileParser.parsePacksFrom(file) >> [1L: "episode 1", 2L: "episode 2"]

    when:
    listFileWorker.updateAvailablePacks("joe", file)

    then:
    def bot = RepositoryLocator.bots().get(joeBot.id)
    bot.packs() == [new Pack(1, "episode 1", joeBot.id), new Pack(2, "episode 2", joeBot.id)] as Set
  }

  def "can create the bot if it does not exist yet"() {
    given:
    def file = new File("list.txt")
    listFileParser.parsePacksFrom(file) >> [1L: "episode 1"]

    when:
    listFileWorker.updateAvailablePacks("joe", file)

    then:
    def bot = RepositoryLocator.bots().findByNickname("joe").get()
    bot.packs() == [new Pack(1, "episode 1", bot.id)] as Set
  }

  def "updates the last checked date"() {
    given:
    def joeBot = new Bot("joe")
    RepositoryLocator.bots().add(joeBot)

    and:
    def file = new File("list.txt")
    listFileParser.parsePacksFrom(file) >> [:]

    when:
    listFileWorker.updateAvailablePacks("joe", file)

    then:
    def bot = RepositoryLocator.bots().get(joeBot.id)
    bot.lastChecked != null
  }
}
