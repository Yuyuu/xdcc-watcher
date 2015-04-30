package watcher.irc.bot.state

import spock.lang.Specification
import watcher.irc.bot.PackWatcher

@SuppressWarnings("GroovyAccessibility")
class PackWatcherStateHandlerTest extends Specification {

  PackWatcherStateHandler stateHandler
  PackWatcher packWatcher

  def setup() {
    packWatcher = Mock(PackWatcher)
  }

  def "does not terminate the bot until it has checked all the bots"() {
    given:
    stateHandler = new PackWatcherStateHandler(packWatcher, 2)

    when:
    stateHandler.done()

    then:
    0 * packWatcher.disconnectFromServer()
    stateHandler.botsChecked == 1
  }

  def "disconnects the bot once it has checked all the bots"() {
    given:
    stateHandler = new PackWatcherStateHandler(packWatcher, 1)

    when:
    stateHandler.done()

    then:
    1 * packWatcher.disconnectFromServer()
  }
}
