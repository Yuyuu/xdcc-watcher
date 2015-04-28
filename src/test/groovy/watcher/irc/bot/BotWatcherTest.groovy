package watcher.irc.bot

import fr.vter.xdcc.infrastructure.persistence.mongo.MongoLinkContext
import org.jibble.pircbot.User
import spock.lang.Specification
import watcher.worker.BotWorker

class BotWatcherTest extends Specification {

  BotWatcher botWatcher
  MongoLinkContext mongoLinkContext
  BotWorker botWorker

  def setup() {
    mongoLinkContext = Mock(MongoLinkContext)
    botWorker = Mock(BotWorker)
    botWatcher = new BotWatcher(mongoLinkContext, botWorker)
  }

  def "finds all the bots in the channel user list"() {
    given:
    User[] users = generateUsers()

    when:
    botWatcher.onUserList("#channel", users)

    then:
    1 * botWorker.updateAvailableBots(["[SeriaL]user1", "[DarksiDe]user2", "[Darkside]user3", "iNFEXiOUSuser4"])
  }

  def "encapsulates the work in the context"() {
    given:
    User[] users = generateUsers()

    when:
    botWatcher.onUserList("#channel", users)

    then:
    1 * mongoLinkContext.beforeExecution()
    then:
    1* mongoLinkContext.afterExecution()
    then:
    1* mongoLinkContext.ultimately()
  }

  def "handles errors on the context"() {
    given:
    User[] users = generateUsers()
    botWorker.updateAvailableBots(_ as List) >> { throw new RuntimeException() }

    when:
    botWatcher.onUserList("#channel", users)

    then:
    1 * mongoLinkContext.onError()
    then:
    1 * mongoLinkContext.ultimately()
  }

  def "no bot no work"() {
    when:
    botWatcher.onUserList("#channel", [] as User[])

    then:
    0 * botWorker.updateAvailableBots(_ as List)
  }

  private User[] generateUsers() {
    User user1 = Mock(User)
    User user2 = Mock(User)
    User user3 = Mock(User)
    User user4 = Mock(User)
    User user5 = Mock(User)
    User user6 = Mock(User)
    User user7 = Mock(User)

    user1.getNick() >> "%[SeriaL]user1"
    user2.getNick() >> "%[DarksiDe]user2"
    user3.getNick() >> "%[Darkside]user3"
    user4.getNick() >> "%iNFEXiOUSuser4"
    user5.getNick() >> "[SeriaL]user5"
    user6.getNick() >> "user6"
    user7.getNick() >> "%user7"

    return [user1, user2, user3, user4, user5, user6, user7]
  }
}
