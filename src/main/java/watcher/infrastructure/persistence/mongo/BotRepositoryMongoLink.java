package watcher.infrastructure.persistence.mongo;

import com.google.common.collect.Lists;
import fr.vter.xdcc.infrastructure.persistence.mongo.MongoLinkRepository;
import org.jongo.Jongo;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Restrictions;
import watcher.model.bot.Bot;
import watcher.model.bot.BotRepository;

import java.util.List;
import java.util.Optional;

public class BotRepositoryMongoLink extends MongoLinkRepository<Bot> implements BotRepository {

  protected BotRepositoryMongoLink(MongoSession session) {
    super(session);
  }

  @Override
  public long count() {
    final Jongo jongo = new Jongo((getSession().getDb()));
    return jongo.getCollection("bot").count();
  }

  @Override
  public List<Bot> findAllWithoutLoadingPacks() {
    final Jongo jongo = new Jongo(getSession().getDb());
    final Iterable<Bot> bots = jongo.getCollection("bot").find().projection("{packs: 0}").as(Bot.class);
    return Lists.newArrayList(bots);
  }

  @Override
  public Optional<Bot> findByNickname(String nickname) {
    final Criteria criteria = criteria();
    criteria.add(Restrictions.equals("nickname", nickname));
    List<Bot> bots = criteria.list();
    return bots.stream().findFirst();
  }

  @Override
  public List<Bot> paginateWithoutLoadingPacks(int max, int offset) {
    final Jongo jongo = new Jongo(getSession().getDb());
    final Iterable<Bot> bots = jongo.getCollection("bot").find()
        .skip(offset).limit(max).projection("{packs: 0}").as(Bot.class);
    return Lists.newArrayList(bots);
  }
}
