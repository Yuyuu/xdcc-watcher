package fr.vter.xdcc.infrastructure.persistence.mongo;

import fr.vter.xdcc.model.bot.Bot;
import org.mongolink.MongoSession;

public class BotRepository extends MongoLinkRepository<Bot> {

  protected BotRepository(MongoSession session) {
    super(session);
  }
}
