package watcher.worker

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Rule
import spock.lang.Specification
import watcher.infrastructure.persistence.memory.WithMemoryRepository
import watcher.model.RepositoryLocator
import watcher.model.bot.Bot

@SuppressWarnings("GroovyAccessibility")
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
    def repository = RepositoryLocator.bots()
    repository.get(kimBot.id) == kimBot
    repository.findByNickname("kim").get().id == kimBot.id
  }

  def "sets the date of unavailability of a bot"() {
    given:
    def kimBot = new Bot("kim")
    RepositoryLocator.bots().add(kimBot)

    when:
    botWorker.updateAvailableBots(["lea", "bob"])

    then:
    def bot = RepositoryLocator.bots().get(kimBot.id)
    bot.dateOfFirstUnavailability != null
  }

  def "removes the bots that are unavailable for more than a week"() {
    given:
    def kimBot = new Bot("kim")
    kimBot.dateOfFirstUnavailability = new DateTime(2015, 5, 8, 10, 0, DateTimeZone.forID("Europe/Paris"))
    RepositoryLocator.bots().add(kimBot)

    when:
    botWorker.updateAvailableBots(["lea", "bob"])

    then:
    RepositoryLocator.bots().get(kimBot.id) == null
  }

  def "resets the date if a bot becomes available again"() {
    given:
    def kimBot = new Bot("kim")
    kimBot.dateOfFirstUnavailability = new DateTime(2015, 5, 13, 10, 0, DateTimeZone.forID("Europe/Paris"))
    RepositoryLocator.bots().add(kimBot)

    when:
    botWorker.updateAvailableBots(["lea", "kim"])

    then:
    def bot = RepositoryLocator.bots().get(kimBot.id)
    bot.dateOfFirstUnavailability == null
  }
}
