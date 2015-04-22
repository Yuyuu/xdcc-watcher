package fr.vter.xdcc.infrastructure.persistence.mongo.mapping;

import fr.vter.xdcc.infrastructure.persistence.mongo.FakeEntity;
import org.mongolink.domain.mapper.AggregateMap;

public class FakeEntityMapping extends AggregateMap<FakeEntity> {

  @Override
  public void map() {
    id().onProperty(FakeEntity::getId).natural();
  }
}
