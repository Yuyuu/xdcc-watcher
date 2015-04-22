package fr.vter.xdcc.model;

import org.bson.types.ObjectId;

public abstract class GenericEntityWithObjectId extends GenericEntity<ObjectId> {

  protected GenericEntityWithObjectId() {
    super(new ObjectId());
  }
}
