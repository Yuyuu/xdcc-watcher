package watcher.worker

import spock.lang.Specification

class BotListingUrlFinderTest extends Specification {

  BotListingUrlFinder finder

  def setup() {
    finder = new BotListingUrlFinder()
  }

  def "finds the listing URL of a bot"() {
    given:
    def data = getClass().classLoader.getResourceAsStream("finder/url_finder.html")

    when:
    def url = finder.findListingUrl("[Darkside]`Z_Nation", data)

    then:
    url == "http://listing.xdaysaysay.com/xdcc/serial_us,26/darksidez_nation,246/"
  }
}
