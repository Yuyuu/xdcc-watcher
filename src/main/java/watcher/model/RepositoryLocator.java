package watcher.model;

import watcher.model.bot.BotRepository;

public abstract class RepositoryLocator {

  public static void initialize(RepositoryLocator instance) {
    RepositoryLocator.instance = instance;
  }

  public static BotRepository bots() {
    return instance.getBots();
  }

  protected abstract BotRepository getBots();

  private static RepositoryLocator instance;
}
