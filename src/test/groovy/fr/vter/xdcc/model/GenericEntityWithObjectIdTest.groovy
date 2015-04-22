package fr.vter.xdcc.model

import org.bson.types.ObjectId
import spock.lang.Specification

class GenericEntityWithObjectIdTest extends Specification {

  def "it generates a default object id"() {
    given:
    DummyEntity dummyEntity = new DummyEntity()

    expect:
    dummyEntity.id != null
    dummyEntity.id instanceof ObjectId
  }

  class DummyEntity extends GenericEntityWithObjectId {
  }
}
