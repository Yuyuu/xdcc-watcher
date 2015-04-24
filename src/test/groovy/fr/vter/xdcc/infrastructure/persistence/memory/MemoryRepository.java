package fr.vter.xdcc.infrastructure.persistence.memory;

import com.google.common.collect.Sets;
import fr.vter.xdcc.model.EntityWithObjectId;
import fr.vter.xdcc.model.Repository;
import org.bson.types.ObjectId;

import java.util.Set;

public class MemoryRepository<TEntity extends EntityWithObjectId> implements Repository<TEntity> {

  @Override
  public TEntity get(ObjectId id) {
    return entities.stream()
        .filter(entity -> entity.getId().equals(id))
        .findFirst().get();
  }

  @Override
  public void add(TEntity entity) {
    entities.add(entity);
  }

  @Override
  public void remove(TEntity entity) {
    entities.remove(entity);
  }

  protected final Set<TEntity> entities = Sets.newHashSet();
}
