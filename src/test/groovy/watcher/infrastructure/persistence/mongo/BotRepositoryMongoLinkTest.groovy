package watcher.infrastructure.persistence.mongo

import fr.vter.xdcc.infrastructure.persistence.mongo.WithMongoLink
import org.bson.types.ObjectId
import org.junit.Rule
import spock.lang.Specification
import watcher.model.bot.Bot
import watcher.model.bot.Pack

@SuppressWarnings("GroovyAccessibility")
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
    def foundElement = repository.get(bot.id)
    foundElement == bot
  }

  def "can get a bot"() {
    given:
    def botId = ObjectId.get();
    def packId = ObjectId.get();
    mongolink.collection("bot") << [_id:botId, name:"joe", packSet:[[_id:packId, position:6, title:"episode 6"]], schemaVersion:1]

    when:
    def bot = repository.get(botId)

    then:
    bot.id == botId
    bot.name == "joe"
    bot.has(new Pack(6, "episode 6"))
    bot.packSet.first().id == packId
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
