package watcher.irc.bot;

import com.google.inject.Injector;
import org.jibble.pircbot.PircBot;
import watcher.irc.bot.state.PackWatcherStateHandler;

import javax.inject.Inject;

public class WatcherFactory {

  @Inject
  public WatcherFactory(Injector injector) {
    this.injector = injector;
  }

  public <TWatcher extends PircBot> TWatcher createWatcher(Class<TWatcher> watcherClass) {
    return injector.getInstance(watcherClass);
  }

  public PackWatcher createPackWatcherWithObjective(int numberOfBotsToUpdate) {
    PackWatcher packWatcher = injector.getInstance(PackWatcher.class);
    packWatcher.setStateHandler(new PackWatcherStateHandler(packWatcher, numberOfBotsToUpdate));
    return packWatcher;
  }

  private final Injector injector;
}
