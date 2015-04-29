package watcher.irc.bot

import com.google.inject.Injector
import spock.lang.Specification

class WatcherFactoryTest extends Specification {

  WatcherFactory watcherFactory
  Injector injector

  def setup() {
    injector = Mock(Injector)
    watcherFactory = new WatcherFactory(injector)
  }

  @SuppressWarnings("GroovyAssignabilityCheck")
  def "creates a pack watcher with an objective of bots to update"() {
    given:
    PackWatcher packWatcher = Mock(PackWatcher)
    injector.getInstance(PackWatcher.class) >> packWatcher

    when:
    watcherFactory.createPackWatcherWithObjective(5)

    then:
    1 * packWatcher.setStateHandler({ handler -> handler.numberOfBotsToCheck == 5 })
  }

  def "creates a default pack watcher"() {
    given:
    PackWatcher packWatcher = Mock(PackWatcher)
    injector.getInstance(PackWatcher.class) >> packWatcher

    when:
    def watcher = watcherFactory.createWatcher(PackWatcher.class)

    then:
    watcher instanceof PackWatcher
  }

  def "creates a default bot watcher"() {
    given:
    BotWatcher botWatcher = Mock(BotWatcher)
    injector.getInstance(BotWatcher.class) >> botWatcher

    when:
    def watcher = watcherFactory.createWatcher(BotWatcher.class)

    then:
    watcher instanceof BotWatcher
  }
}
