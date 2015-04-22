package fr.vter.xdcc.infrastructure.persistence.mongo;

import com.google.common.reflect.TypeToken;
import fr.vter.xdcc.model.EntityWithObjectId;
import fr.vter.xdcc.model.Repository;
import org.bson.types.ObjectId;
import org.mongolink.MongoSession;

public abstract class MongoLinkRepository<TEntity extends EntityWithObjectId> implements Repository<TEntity> {

  protected MongoLinkRepository(MongoSession session) {
    this.session = session;
  }

  @Override
  public TEntity get(ObjectId id) {
    return getSession().get(id, entityType());
  }

  @Override
  public void add(TEntity entity) {
    getSession().save(entity);
  }

  @Override
  public void remove(TEntity entity) {
    getSession().delete(entity);
  }

  protected Class<TEntity> entityType() {
    return (Class<TEntity>) typeToken.getRawType();
  }

  protected MongoSession getSession() {
    return session;
  }

  private final MongoSession session;
  private final TypeToken<TEntity> typeToken = new TypeToken<TEntity>(getClass()) {
  };
}
