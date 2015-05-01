package watcher.irc.bot.state;

import watcher.irc.bot.PackWatcher;
import watcher.model.bot.Bot;

import java.util.List;

public class PackWatcherStateHandler implements StateHandler {

  public PackWatcherStateHandler(PackWatcher packWatcher, List<Bot> botsToUpdate) {
    this.packWatcher = packWatcher;
    this.botsToUpdate = botsToUpdate;
  }

  @Override
  public void connectedToChannel() {
    botsToUpdate.stream().forEach(bot -> packWatcher.say(bot.nickname(), "xdcc send -1"));
  }

  @Override
  public void done() {
    botsChecked++;
    if (botsChecked >= botsToUpdate.size()) {
      packWatcher.disconnectFromServer();
    }
  }

  private long botsChecked = 0;
  private final PackWatcher packWatcher;
  private final List<Bot> botsToUpdate;
}
