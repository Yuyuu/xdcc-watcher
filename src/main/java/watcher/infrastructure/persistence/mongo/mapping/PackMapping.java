package watcher.infrastructure.persistence.mongo.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import watcher.model.bot.Pack;

public class PackMapping extends AggregateMap<Pack> {

  @Override
  public void map() {
    id().onField("id").natural();
    property().onField("position");
    property().onField("title");
  }
}
