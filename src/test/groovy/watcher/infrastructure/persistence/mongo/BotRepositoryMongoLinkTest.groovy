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
    mongolink.collection("bot") << [_id:botId, nickname:"joe", packs:[[_id:packId, position:6, title:"episode 6", botId: botId]], schemaVersion:1]

    when:
    def bot = repository.get(botId)

    then:
    bot.id == botId
    bot.nickname == "joe"
    bot.has(new Pack(6, "episode 6", botId))
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

  def "can find by nickname"() {
    given:
    mongolink.collection("bot") << [
        [_id:ObjectId.get(), nickname:"joe", packs:[]],
        [_id:ObjectId.get(), nickname:"lea", packs:[]]
    ]

    when:
    def optional = repository.findByNickname("lea")

    then:
    optional.present
    optional.get().nickname() == "lea"
  }

  def "can get all without loading the packs"() {
    given:
    mongolink.collection("bot") << [
        [_id:ObjectId.get(), nickname:"joe", packs:[[_id:ObjectId.get(), position:6, title:"episode 6"]]],
        [_id:ObjectId.get(), nickname:"lea", packs:[[_id:ObjectId.get(), position:2, title:"episode 2"]]]
    ]

    expect:
    List<Bot> list = repository.findAllWithoutLoadingPacks();
    list.size() == 2
    list.every { it.id != null && it.packs().size() == 0 }
  }

  def "can add several at once"() {
    given:
    def listOfBots = [new Bot("kim"), new Bot("lea")]
    repository.addAll(listOfBots)
    mongolink.cleanSession()

    expect:
    listOfBots.every { repository.get(it.id) != null }
  }

  def "can remove several at once"() {
    given:
    def kimBot = new Bot("kim")
    def joeBot = new Bot("joe")
    def leaBot = new Bot("lea")
    repository.addAll([kimBot, leaBot, joeBot])

    when:
    repository.removeAll([joeBot, leaBot])
    mongolink.cleanSession()

    then:
    repository.get(kimBot.id) != null
    repository.get(joeBot.id) == null
    repository.get(leaBot.id) == null
  }

  def "counts the number of bots"() {
    given:
    mongolink.collection("bot") << [
        [_id:ObjectId.get(), nickname:"joe", packs:[]],
        [_id:ObjectId.get(), nickname:"lea", packs:[]]
    ]

    expect:
    repository.count() == 2
  }

  def "can paginate and without loading the packs"() {
    given:
    def kimId = ObjectId.get()
    def bobId = ObjectId.get()
    mongolink.collection("bot") << [
        [_id:ObjectId.get(), nickname:"joe", packs:[]],
        [_id:kimId, nickname:"kim", packs:[[_id:ObjectId.get(), position:6, title:"episode 6"]]],
        [_id:bobId, nickname:"bob", packs:[]],
        [_id:ObjectId.get(), nickname:"lea", packs:[]]
    ]

    expect:
    def bots = repository.paginateWithoutLoadingPacks(2, 1)
    bots[0].id == kimId
    bots[0].packs().empty
    bots[1].id == bobId
  }
}
