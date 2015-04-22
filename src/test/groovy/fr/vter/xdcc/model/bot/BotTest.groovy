package fr.vter.xdcc.model.bot

import org.bson.types.ObjectId
import spock.lang.Specification

class BotTest extends Specification {

  def "gets a default id upon creation"() {
    given:
    def bot = new Bot("bob")

    expect:
    bot.id != null
    bot.id instanceof ObjectId
  }

  def "the current schema version is given to a created bot"() {
    given:
    def bot = new Bot("bob")

    expect:
    bot.schemaVersion == Bot.SCHEMA_VERSION
  }

  def "a bot is created with a name"() {
    given:
    def bot = new Bot("bob")

    expect:
    bot.name == "bob"
  }
}
