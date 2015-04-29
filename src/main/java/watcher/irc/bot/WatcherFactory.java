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

  public <TBot extends PircBot> TBot createWatcher(Class<TBot> botClass) {
    return injector.getInstance(botClass);
  }

  public PackWatcher createPackWatcherWithObjective(int numberOfBotsToUpdate) {
    PackWatcher packWatcher = injector.getInstance(PackWatcher.class);
    packWatcher.setStateHandler(new PackWatcherStateHandler(packWatcher, numberOfBotsToUpdate));
    return packWatcher;
  }

  private final Injector injector;
}
