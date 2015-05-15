package watcher.irc.bot

import fr.vter.xdcc.infrastructure.persistence.mongo.MongoLinkContext
import org.jibble.pircbot.DccFileTransfer
import org.jibble.pircbot.ReplyConstants
import spock.lang.Specification
import watcher.irc.bot.state.PackWatcherStateHandler
import watcher.worker.ListFileWorker
import watcher.worker.WebsiteWorker

@SuppressWarnings("GroovyAccessibility")
class PackWatcherTest extends Specification {

  PackWatcher packWatcher
  MongoLinkContext mongoLinkContext
  ListFileWorker listFileWorker
  WebsiteWorker websiteWorker
  PackWatcherStateHandler stateHandler

  def setup() {
    mongoLinkContext = Mock(MongoLinkContext)
    listFileWorker = Mock(ListFileWorker)
    websiteWorker = Mock(WebsiteWorker)
    packWatcher = new PackWatcher(mongoLinkContext, listFileWorker, websiteWorker)

    stateHandler = Mock(PackWatcherStateHandler)
    packWatcher.stateHandler = stateHandler
  }

  def "skips a bot if it was not present on the channel"() {
    when:
    packWatcher.onServerResponse(ReplyConstants.ERR_NOSUCHNICK, "")

    then:
    1 * stateHandler.done()
  }

  def "uses the website when the list file is not available"() {
    when:
    packWatcher.onNotice("kim", null, null, null, "*** Invalid Pack Number ***")

    then:
    1 * mongoLinkContext.beforeExecution()
    1 * websiteWorker.updateAvailablePacks("kim")
    1 * mongoLinkContext.afterExecution()
    1 * mongoLinkContext.ultimately()
    1 * stateHandler.done()
  }

  def "handles errors on the context when using the website"() {
    given:
    websiteWorker.updateAvailablePacks("kim") >> { throw new RuntimeException() }

    when:
    packWatcher.onNotice("kim", null, null, null, "*** Invalid Pack Number ***")

    then:
    1 * mongoLinkContext.onError()
    1 * mongoLinkContext.ultimately()
    1 * stateHandler.done()
  }

  def "ignores a notice not about invalid pack number"() {
    when:
    packWatcher.onNotice("kim", null, null, null, "Hello")

    then:
    0 * websiteWorker.updateAvailablePacks("kim")
    0 * stateHandler.done()
  }

  def "skips the bot on errors at list file transfer"() {
    given:
    DccFileTransfer dccFileTransfer = Mock(DccFileTransfer)

    when:
    packWatcher.onFileTransferFinished(dccFileTransfer, new RuntimeException())

    then:
    0 * listFileWorker.updateAvailablePacks(_ as String, _ as File)
    1 * stateHandler.done()
  }

  def "uses the list file to update the available packs"() {
    given:
    DccFileTransfer dccFileTransfer = Mock(DccFileTransfer)
    dccFileTransfer.getFile() >> new File("spock.txt")
    dccFileTransfer.getNick() >> "bot"

    when:
    packWatcher.onFileTransferFinished(dccFileTransfer, null)

    then:
    1 * mongoLinkContext.beforeExecution()
    1 * listFileWorker.updateAvailablePacks(dccFileTransfer.getNick(), dccFileTransfer.getFile())
    1 * mongoLinkContext.afterExecution()
    1 * mongoLinkContext.ultimately()
    1 * stateHandler.done()
  }

  def "handles errors on the context when using the list file"() {
    given:
    listFileWorker.updateAvailablePacks("kim", _ as File) >> { throw new RuntimeException() }

    and:
    DccFileTransfer dccFileTransfer = Mock(DccFileTransfer)
    dccFileTransfer.getFile() >> new File("spock.txt")
    dccFileTransfer.getNick() >> "kim"

    when:
    packWatcher.onFileTransferFinished(dccFileTransfer, null)

    then:
    1 * mongoLinkContext.onError()
    1 * mongoLinkContext.ultimately()
    1 * stateHandler.done()
  }

  def "refuses a file from unauthorized source"() {
    given:
    DccFileTransfer transfer = Mock(DccFileTransfer)
    transfer.getNick() >> "unauthorized"

    when:
    packWatcher.onIncomingFileTransfer(transfer)

    then:
    1 * transfer.close()
  }

  def "accepts the list file of a bot"() {
    given:
    File file = new File("tmp.txt")
    DccFileTransfer dccFileTransfer = Mock(DccFileTransfer)
    dccFileTransfer.getFile() >> file
    dccFileTransfer.getNick() >> "[SeriaL]Xdcc`authorized"

    when:
    packWatcher.onIncomingFileTransfer(dccFileTransfer)

    then:
    1 * dccFileTransfer.receive(_ as File, false)
  }

  def "notifies the external state handler when connected to channel"() {
    when:
    packWatcher.onUserList("#channel", null)

    then:
    1 * stateHandler.connectedToChannel()
  }
}
