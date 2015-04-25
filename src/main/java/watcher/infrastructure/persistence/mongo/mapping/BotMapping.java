package watcher.infrastructure.persistence.mongo.mapping;

import watcher.model.bot.Bot;
import org.mongolink.domain.mapper.AggregateMap;

public class BotMapping extends AggregateMap<Bot> {

  @Override
  public void map() {
    id().onField("id").natural();
    property().onField("nickname");
    property().onField("listingUrl");
    collection().onField("packs");
    property().onField("lastUpdated");
    property().onField("lastChecked");
    property().onField("schemaVersion");
  }
}
