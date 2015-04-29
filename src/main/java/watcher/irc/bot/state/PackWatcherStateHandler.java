package watcher.irc.bot.state;

import watcher.irc.bot.PackWatcher;

public class PackWatcherStateHandler implements StateHandler {

  public PackWatcherStateHandler(PackWatcher bot, int numberOfBotsToCheck) {
    this.bot = bot;
    this.bot.setStateHandler(this);
    this.numberOfBotsToCheck = numberOfBotsToCheck;
  }

  @Override
  public void done() {
    botsChecked++;
    if (botsChecked >= numberOfBotsToCheck) {
      bot.terminate();
    }
  }

  private int botsChecked = 0;
  private final PackWatcher bot;
  private final int numberOfBotsToCheck;
}
