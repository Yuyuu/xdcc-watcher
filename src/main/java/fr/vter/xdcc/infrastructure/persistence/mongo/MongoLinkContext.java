package fr.vter.xdcc.infrastructure.persistence.mongo;

import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class MongoLinkContext {

  @Inject
  public MongoLinkContext(MongoSessionManager sessionManager) {
    sessions = ThreadLocal.withInitial(sessionManager::createSession);
  }

  public void beforeExecution() {
    LOGGER.debug("Starting a session");
    sessions.get().start();
  }

  public void afterExecution() {
    LOGGER.debug("Synchronization with MongoDB");
    sessions.get().flush();
  }

  public void ultimately() {
    LOGGER.debug("Closing the session");
    sessions.get().stop();
    sessions.remove();
  }

  public void onError() {
    LOGGER.debug("Cleaning on session error");
    sessions.get().clear();
  }

  public MongoSession currentSession() {
    return sessions.get();
  }

  private final ThreadLocal<MongoSession> sessions;
  private static final Logger LOGGER = LoggerFactory.getLogger(MongoLinkContext.class);
}
