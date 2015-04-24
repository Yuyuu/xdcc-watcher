package watcher.infrastructure.persistence.mongo;

import fr.vter.xdcc.infrastructure.persistence.mongo.MongoLinkRepository;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Restrictions;
import watcher.model.bot.Bot;
import org.mongolink.MongoSession;
import watcher.model.bot.BotRepository;

import java.util.List;
import java.util.Optional;

public class BotRepositoryMongoLink extends MongoLinkRepository<Bot> implements BotRepository {

  protected BotRepositoryMongoLink(MongoSession session) {
    super(session);
  }

  @Override
  public Optional<Bot> findByNickname(String nickname) {
    final Criteria criteria = criteria();
    criteria.add(Restrictions.equals("name", nickname));
    List<Bot> bots = criteria.list();
    return bots.stream().findFirst();
  }
}
