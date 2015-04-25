package watcher.worker

import org.junit.Rule
import spock.lang.Specification
import watcher.infrastructure.persistence.memory.WithMemoryRepository
import watcher.model.RepositoryLocator
import watcher.model.bot.Bot
import watcher.model.bot.Pack
import watcher.worker.parser.WebsiteParser

@SuppressWarnings("GroovyAccessibility")
class WebsiteWorkerTest extends Specification {

  @Rule
  WithMemoryRepository memoryRepository = new WithMemoryRepository()

  @Rule
  WithFakeWebsite withFakeWebsite = new WithFakeWebsite()

  WebsiteWorker websiteWorker
  WebsiteParser websiteParser
  BotListingUrlFinder botListingUrlFinder

  def setup() {
    websiteParser = Mock(WebsiteParser)
    botListingUrlFinder = Mock(BotListingUrlFinder)
    websiteWorker = new WebsiteWorker(websiteParser, botListingUrlFinder)
  }

  def "updates the available bots"() {
    given:
    def joeBot = new Bot("joe")
    joeBot.setListingUrl(urlToResource("finder/empty_page.html"))
    RepositoryLocator.bots().add(joeBot)

    and:
    websiteParser.parsePacksFrom(_ as InputStream) >> [1L: "episode 1", 2L: "episode 2"]

    when:
    websiteWorker.updateAvailablePacks("joe")

    then:
    def bot = RepositoryLocator.bots().get(joeBot.id)
    bot.packs() == [new Pack(1, "episode 1"), new Pack(2, "episode 2")] as Set
  }

  def "can fetch the listing URL of the bot if it is not known yet"() {
    given:
    def joeBot = new Bot("joe")
    RepositoryLocator.bots().add(joeBot)

    and:
    def url = urlToResource("finder/empty_page.html")
    botListingUrlFinder.findListingUrl("joe", _ as InputStream) >> url
    websiteParser.parsePacksFrom(_ as InputStream) >> [:]

    when:
    websiteWorker.updateAvailablePacks("joe")

    then:
    def bot = RepositoryLocator.bots().get(joeBot.id)
    bot.url() == url
  }

  def "can create the bot if it does not exist yet"() {
    given:
    botListingUrlFinder.findListingUrl("joe", _ as InputStream) >> urlToResource("finder/empty_page.html")
    websiteParser.parsePacksFrom(_ as InputStream) >> [1L: "episode 1"]

    when:
    websiteWorker.updateAvailablePacks("joe")

    then:
    def bot = RepositoryLocator.bots().findByNickname("joe").get()
    bot.packs() == [new Pack(1, "episode 1")] as Set
  }

  def "skips the process if the listing url cannot be found"() {
    given:
    def joeBot = new Bot("joe")
    RepositoryLocator.bots().add(joeBot)

    and:
    botListingUrlFinder.findListingUrl("joe", _ as InputStream) >> null

    when:
    websiteWorker.updateAvailablePacks("joe")

    then:
    notThrown(Exception)
    def bot = RepositoryLocator.bots().get(joeBot.id)
    bot.packs().empty
  }

  def "updates the last checked date"() {
    given:
    def joeBot = new Bot("joe")
    joeBot.setListingUrl(urlToResource("finder/empty_page.html"))
    RepositoryLocator.bots().add(joeBot)

    and:
    websiteParser.parsePacksFrom(_ as InputStream) >> [:]

    when:
    websiteWorker.updateAvailablePacks("joe")

    then:
    def bot = RepositoryLocator.bots().get(joeBot.id)
    bot.lastChecked != null
  }

  private String urlToResource(String resourcePath) {
    return getClass().classLoader.getResource(resourcePath).toString()
  }
}
