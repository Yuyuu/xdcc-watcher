package fr.vter.xdcc.infrastructure.persistence.mongo

import org.bson.types.ObjectId
import org.junit.Rule
import spock.lang.Specification

class MongoRepositoryTest extends Specification {

  @Rule
  WithMongoLink mongolink = WithMongoLink.withPackage("fr.vter.xdcc.infrastructure.persistence.mongo.mapping")

  FakeEntityRepository repository

  def setup() {
    repository = new FakeEntityRepository(mongolink.currentSession())
  }

  def "can add an entity"() {
    given:
    def id = new ObjectId()

    when:
    repository.add(new FakeEntity(id))
    mongolink.cleanSession();

    then:
    def foundElement = mongolink.collection("fakeentity").findOne(_id: id)
    foundElement["_id"] == id
  }


  def "can remove an entity"() {
    given:
    def id = new ObjectId()
    def entity = new FakeEntity(id)
    repository.add(entity)

    when:
    repository.remove(entity)
    mongolink.cleanSession()

    then:
    repository.get(id) == null
  }
}
