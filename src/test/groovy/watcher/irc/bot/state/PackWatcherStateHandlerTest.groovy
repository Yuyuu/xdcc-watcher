package watcher.irc.bot.state

import spock.lang.Specification
import watcher.irc.bot.PackWatcher
import watcher.model.bot.Bot

@SuppressWarnings("GroovyAccessibility")
class PackWatcherStateHandlerTest extends Specification {

  PackWatcherStateHandler stateHandler
  PackWatcher packWatcher

  def setup() {
    packWatcher = Mock(PackWatcher)
  }

  def "does not terminate the bot until it has checked all the bots"() {
    given:
    stateHandler = new PackWatcherStateHandler(packWatcher, [new Bot("joe"), new Bot("kim")])

    when:
    stateHandler.done()

    then:
    0 * packWatcher.disconnectFromServer()
    stateHandler.botsChecked == 1
  }

  def "disconnects the bot once it has checked all the bots"() {
    given:
    stateHandler = new PackWatcherStateHandler(packWatcher, [new Bot("kim")])

    when:
    stateHandler.done()

    then:
    1 * packWatcher.disconnectFromServer()
  }

  def "waits for the watcher to be in channel"() {
    given:
    stateHandler = new PackWatcherStateHandler(packWatcher, [new Bot("joe"), new Bot("kim")])

    when:
    stateHandler.connectedToChannel()

    then:
    1 * packWatcher.say("joe", "xdcc send -1")
    1 * packWatcher.say("kim", "xdcc send -1")
  }
}
