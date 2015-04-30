package watcher.irc.bot.state;

import watcher.irc.bot.PackWatcher;

public class PackWatcherStateHandler implements StateHandler {

  public PackWatcherStateHandler(PackWatcher packWatcher, int numberOfBotsToCheck) {
    this.packWatcher = packWatcher;
    this.numberOfBotsToCheck = numberOfBotsToCheck;
  }

  @Override
  public void done() {
    botsChecked++;
    if (botsChecked >= numberOfBotsToCheck) {
      packWatcher.disconnectFromServer();
    }
  }

  private int botsChecked = 0;
  private final PackWatcher packWatcher;
  private final int numberOfBotsToCheck;
}
