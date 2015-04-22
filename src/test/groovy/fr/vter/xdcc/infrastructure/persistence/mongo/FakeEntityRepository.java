package fr.vter.xdcc.infrastructure.persistence.mongo;

import org.mongolink.MongoSession;

public class FakeEntityRepository extends MongoLinkRepository<FakeEntity> {

  protected FakeEntityRepository(MongoSession session) {
    super(session);
  }
}
