package watcher.irc.bot;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;

public abstract class GenericWatcher extends PircBot implements Watcher {

  @Override
  public void connectToServer(String hostname) throws IOException, IrcException {
    connect(hostname);
  }

  @Override
  public void joinServerChannel(String channelName) {
    joinChannel(channelName);
  }

  @Override
  public void disconnectFromServer() {
    disconnect();
  }
}
