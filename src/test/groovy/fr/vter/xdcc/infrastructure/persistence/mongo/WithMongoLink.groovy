package fr.vter.xdcc.infrastructure.persistence.mongo

import com.gmongo.GMongo
import com.mongodb.DB
import com.mongodb.DBCollection
import org.junit.rules.ExternalResource
import org.mongolink.MongoSession
import org.mongolink.test.MongolinkRule

class WithMongoLink extends ExternalResource {

  public static WithMongoLink withPackage(String packageName) {
    final WithMongoLink result = new WithMongoLink()
    result.mongolink = MongolinkRule.withPackage(packageName)
    return result
  }

  private WithMongoLink() {
  }

  @Override
  protected void before() throws Throwable {
    mongolink.before()
    gMongo = new GMongo(mongolink.currentSession.db.getMongo())
  }

  @Override
  protected void after() {
    mongolink.after()
  }


  public void cleanSession() {
    mongolink.cleanSession()
  }

  public MongoSession currentSession() {
    return mongolink.currentSession
  }

  public DB db() {
    return gMongo.getDB("test")
  }

  public DBCollection collection(String collection) {
    return db().getCollection(collection)
  }

  private MongolinkRule mongolink
  private GMongo gMongo
}
