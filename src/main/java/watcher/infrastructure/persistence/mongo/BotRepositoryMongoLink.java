package watcher.infrastructure.persistence.mongo;

import fr.vter.xdcc.infrastructure.persistence.mongo.MongoLinkRepository;
import watcher.model.bot.Bot;
import org.mongolink.MongoSession;

public class BotRepositoryMongoLink extends MongoLinkRepository<Bot> {

  protected BotRepositoryMongoLink(MongoSession session) {
    super(session);
  }
}
