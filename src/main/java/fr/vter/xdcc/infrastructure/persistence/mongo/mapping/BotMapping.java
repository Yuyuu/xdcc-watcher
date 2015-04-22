package fr.vter.xdcc.infrastructure.persistence.mongo.mapping;

import fr.vter.xdcc.model.bot.Bot;
import org.mongolink.domain.mapper.AggregateMap;

public class BotMapping extends AggregateMap<Bot> {

  @Override
  public void map() {
    id().onProperty(element().getId()).natural();
    property().onField("name");
    property().onProperty(element().getLastUpdated());
    property().onProperty(element().getLastChecked());
    property().onProperty(element().getSchemaVersion());
  }
}
