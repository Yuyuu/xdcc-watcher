package watcher.irc.bot.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
      LOGGER.debug("Watcher updated all his bots");
      packWatcher.disconnectFromServer();
    }
  }

  private long botsChecked = 0;
  private final PackWatcher packWatcher;
  private final List<Bot> botsToUpdate;
  private static final Logger LOGGER = LoggerFactory.getLogger(PackWatcherStateHandler.class);
}
