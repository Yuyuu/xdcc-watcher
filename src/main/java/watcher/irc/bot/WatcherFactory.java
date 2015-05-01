package watcher.irc.bot;

import com.google.inject.Injector;
import watcher.irc.bot.state.PackWatcherStateHandler;
import watcher.model.bot.Bot;

import javax.inject.Inject;
import java.util.List;

public class WatcherFactory {

  @Inject
  public WatcherFactory(Injector injector) {
    this.injector = injector;
  }

  public <TWatcher extends Watcher> TWatcher createWatcher(Class<TWatcher> watcherClass) {
    return injector.getInstance(watcherClass);
  }

  public PackWatcher createPackWatcherWithObjective(List<Bot> botsToUpdate) {
    PackWatcher packWatcher = injector.getInstance(PackWatcher.class);
    packWatcher.setStateHandler(new PackWatcherStateHandler(packWatcher, botsToUpdate));
    return packWatcher;
  }

  private final Injector injector;
}
