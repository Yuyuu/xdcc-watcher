package watcher.irc.bot;

import com.google.common.collect.Lists;
import fr.vter.xdcc.infrastructure.persistence.mongo.MongoLinkContext;
import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.ReplyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watcher.irc.bot.state.WatcherWithExternalState;
import watcher.irc.bot.state.StateHandler;
import watcher.worker.ListFileWorker;
import watcher.worker.WebsiteWorker;

import javax.inject.Inject;
import java.io.File;
import java.util.List;

public class PackWatcher extends PircBot implements WatcherWithExternalState {

  @Inject
  public PackWatcher(MongoLinkContext mongoLinkContext, ListFileWorker listFileWorker, WebsiteWorker websiteWorker) {
    this.mongoLinkContext = mongoLinkContext;
    this.listFileWorker = listFileWorker;
    this.websiteWorker = websiteWorker;

    setName("xdcc-packwatcher");
    setLogin("xdcc-packwatcher");
    setAutoNickChange(true);
  }

  @Override
  protected void onConnect() {
    LOGGER.debug("{} connected to server", getClass().getSimpleName());
    mongoLinkContext.beforeExecution();
  }

  @Override
  protected void onDisconnect() {
    LOGGER.debug("{} disconnected from server", getClass().getSimpleName());
    mongoLinkContext.ultimately();
  }

  @Override
  protected synchronized void onServerResponse(int code, String response) {
    if (code == ReplyConstants.ERR_NOSUCHNICK) {
      LOGGER.error("Failed to contact bot: {}", response);
      botChecked();
    }
  }

  @Override
  protected synchronized void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
    if (notice.contains("Invalid Pack Number")) {
      LOGGER.debug("List file of bot {} is not available, parsing website instead", sourceNick);
      try {
        websiteWorker.updateAvailablePacks(sourceNick);
        mongoLinkContext.afterExecution();
      } catch (Throwable e) {
        LOGGER.error("Error updating available packs with website", e);
        mongoLinkContext.onError();
      } finally {
        botChecked();
      }
    }
  }

  @Override
  protected synchronized void onFileTransferFinished(DccFileTransfer dccFileTransfer, Exception exception) {
    dccFileTransfer.close();
    if (exception != null) {
      LOGGER.error("Transfer failed", exception);
    } else {
      File listFile = dccFileTransfer.getFile();
      LOGGER.debug("Transfer completed: {}", listFile.getAbsolutePath());
      try {
        listFileWorker.updateAvailablePacks(dccFileTransfer.getNick(), listFile);
        mongoLinkContext.afterExecution();
      } catch (Throwable e) {
        mongoLinkContext.onError();
      }
    }
    botChecked();
  }

  @Override
  protected void onIncomingFileTransfer(DccFileTransfer transfer) {
    if (isSenderLegit(transfer)) {
      File file = new File(filename(transfer));
      transfer.receive(file, false);
      LOGGER.info("Accepted file {} from {}", file.getName(), transfer.getNick());
    } else {
      LOGGER.info("Rejected file from: {}", transfer.getNick());
      transfer.close();
    }
  }

  private boolean isSenderLegit(DccFileTransfer transfer) {
    return legitSenderPrefixes.stream().anyMatch(prefix -> transfer.getNick().startsWith(prefix));
  }

  private void botChecked() {
    if (stateHandler == null) {
      terminate();
    } else {
      stateHandler.done();
    }
  }

  private static String filename(DccFileTransfer transfer) {
    return System.getProperty("java.io.tmpdir") + basename(transfer.getFile().getName()) + "_xdcc.txt";
  }

  private static String basename(String filename) {
    int dotIndex = filename.lastIndexOf('.');
    return (dotIndex <= 0) ? filename : filename.substring(0, dotIndex);
  }

  @Override
  public void setStateHandler(StateHandler stateHandler) {
    this.stateHandler = stateHandler;
  }

  @Override
  public void terminate() {
    disconnect();
  }

  private StateHandler stateHandler;
  private final MongoLinkContext mongoLinkContext;
  private final ListFileWorker listFileWorker;
  private final WebsiteWorker websiteWorker;
  private static final List<String> legitSenderPrefixes = Lists.newArrayList("[SeriaL]Xdcc`" ,"[DarksiDe]`", "[Darkside]`" , "iNFEXiOUS`");
  private static final Logger LOGGER = LoggerFactory.getLogger(PackWatcher.class);
}
