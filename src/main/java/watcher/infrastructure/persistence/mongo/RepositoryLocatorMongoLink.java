package watcher.infrastructure.persistence.mongo;

import fr.vter.xdcc.infrastructure.persistence.mongo.MongoLinkContext;
import watcher.model.RepositoryLocator;
import watcher.model.bot.BotRepository;

import javax.inject.Inject;

public class RepositoryLocatorMongoLink extends RepositoryLocator {

  @Inject
  public RepositoryLocatorMongoLink(MongoLinkContext mongoLinkContext) {
    this.mongoLinkContext = mongoLinkContext;
  }

  @Override
  protected BotRepository getBots() {
    return new BotRepositoryMongoLink(mongoLinkContext.currentSession());
  }

  private final MongoLinkContext mongoLinkContext;
}
