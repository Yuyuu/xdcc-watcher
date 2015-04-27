package watcher.worker

import org.junit.Rule
import spock.lang.Specification
import watcher.infrastructure.persistence.memory.WithMemoryRepository
import watcher.model.RepositoryLocator
import watcher.model.bot.Bot

class BotWorkerTest extends Specification {

  @Rule
  WithMemoryRepository memoryRepository = new WithMemoryRepository()

  BotWorker botWorker

  def setup() {
    botWorker = new BotWorker()
  }

  def "saves the newly added bots"() {
    when:
    botWorker.updateAvailableBots(["lea", "bob", "kim"])

    then:
    def repository = RepositoryLocator.bots()
    repository.findByNickname("lea").present
    repository.findByNickname("kim").present
    repository.findByNickname("bob").present
  }

  def "does not override bots that already exist"() {
    given:
    def kimBot = new Bot("kim")
    RepositoryLocator.bots().add(kimBot)

    when:
    botWorker.updateAvailableBots(["lea", "bob", "kim"])

    then:
    RepositoryLocator.bots().get(kimBot.id) == kimBot
  }

  def "removes the bots that are not present anymore"() {
    given:
    def kimBot = new Bot("kim")
    RepositoryLocator.bots().add(kimBot)

    when:
    botWorker.updateAvailableBots(["lea", "bob"])

    then:
    RepositoryLocator.bots().get(kimBot.id) == null
  }
}
