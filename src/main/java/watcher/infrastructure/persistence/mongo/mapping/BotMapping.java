package watcher.infrastructure.persistence.mongo.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import watcher.model.bot.Bot;

public class BotMapping extends AggregateMap<Bot> {

  @Override
  public void map() {
    id().onField("_id").natural();
    property().onField("nickname");
    property().onField("listingUrl");
    collection().onField("packs");
    property().onField("lastUpdated");
    property().onField("lastChecked");
    property().onField("schemaVersion");
  }
}
