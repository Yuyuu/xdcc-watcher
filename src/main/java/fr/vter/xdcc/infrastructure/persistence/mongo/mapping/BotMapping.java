package fr.vter.xdcc.infrastructure.persistence.mongo.mapping;

import fr.vter.xdcc.model.GenericEntity;
import fr.vter.xdcc.model.bot.Bot;
import org.mongolink.domain.mapper.AggregateMap;

public class BotMapping extends AggregateMap<Bot> {

  @Override
  public void map() {
    id().onProperty(GenericEntity::getId).natural();
    property().onField("name");
    property().onProperty(Bot::getLastUpdated);
    property().onProperty(Bot::getLastChecked);
    property().onProperty(Bot::getSchemaVersion);
  }
}
