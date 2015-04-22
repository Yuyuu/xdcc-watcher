package fr.vter.xdcc.model

import spock.lang.Specification

class GenericEntityTest extends Specification {

  def "two entities with the same id are equal"() {
    given:
    def firstEntity = new DummyEntity("1")
    def secondEntity = new DummyEntity("1")

    expect:
    firstEntity == secondEntity
  }

  def "two entities with different ids are different"() {
    given:
    def firstEntity = new DummyEntity("1")
    def secondEntity = new DummyEntity("2")

    expect:
    firstEntity != secondEntity
  }

  class DummyEntity extends GenericEntity<String> {

    DummyEntity(String id) {
      super(id)
    }
  }
}
