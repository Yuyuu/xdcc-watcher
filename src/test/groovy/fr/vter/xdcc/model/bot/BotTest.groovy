package fr.vter.xdcc.model.bot

import org.bson.types.ObjectId
import spock.lang.Specification
import watcher.model.bot.Bot
import watcher.model.bot.Pack

@SuppressWarnings("GroovyAccessibility")
class BotTest extends Specification {

  def "gets a default id upon creation"() {
    given:
    def bot = new Bot("joe")

    expect:
    bot.id != null
    bot.id instanceof ObjectId
  }

  def "the current schema version is given to a created bot"() {
    given:
    def bot = new Bot("joe")

    expect:
    bot.schemaVersion == Bot.SCHEMA_VERSION
  }

  def "can create a bot for a name"() {
    given:
    def bot = new Bot("joe")

    expect:
    bot.name == "joe"
  }

  def "keeps track of the last update and check"() {
    given:
    def bot = new Bot("joe")
    bot.checked()
    bot.updated()

    expect:
    bot.lastChecked != null
    bot.lastUpdated != null
  }

  def "can contain a pack"() {
    given:
    def bot = new Bot("joe")

    when:
    def pack = new Pack(2, "episode 2")
    bot.updatePacks([pack] as Set)

    then:
    bot.has(pack)
  }

  def "date of last update is updated when packs are added"() {
    given:
    def bot = new Bot("joe")

    when:
    def pack = new Pack(2, "episode 2")
    bot.updatePacks([pack] as Set)

    then:
    bot.lastUpdated != null
  }
}
