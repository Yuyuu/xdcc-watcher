package watcher.irc.bot;

import org.jibble.pircbot.IrcException;

import java.io.IOException;

public interface Watcher {

  void connectToServer(String hostname) throws IOException, IrcException;

  void joinServerChannel(String channelName);

  void disconnectFromServer();

  void say(String target, String message);
}
