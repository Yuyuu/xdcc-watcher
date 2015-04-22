package watcher.infrastructure.persistence.mongo

import fr.vter.xdcc.infrastructure.persistence.mongo.WithMongoLink
import watcher.model.bot.Bot
import org.junit.Rule
import spock.lang.Specification

class BotRepositoryMongoLinkTest extends Specification {

  @Rule
  WithMongoLink mongolink = WithMongoLink.withPackage("watcher.infrastructure.persistence.mongo.mapping")

  BotRepositoryMongoLink repository

  def setup() {
    repository = new BotRepositoryMongoLink(mongolink.currentSession())
  }

  def "can add a bot"() {
    given:
    def bot = new Bot("joe")

    when:
    repository.add(bot)
    mongolink.cleanSession();

    then:
    def foundElement = mongolink.collection("bot").findOne(_id: bot.id)
    foundElement["_id"] == bot.id
    foundElement["name"] == "joe"
  }

  def "can get a bot"() {
    given:
    def bot = new Bot("joe")

    when:
    repository.add(bot)
    mongolink.cleanSession();

    then:
    def foundElement = repository.get(bot.id)
    foundElement["name"] == "joe"
  }

  def "can remove a bot"() {
    given:
    def bot = new Bot("joe")
    repository.add(bot)

    when:
    repository.remove(bot)
    mongolink.cleanSession();

    then:
    repository.get(bot.id) == null
  }
}
