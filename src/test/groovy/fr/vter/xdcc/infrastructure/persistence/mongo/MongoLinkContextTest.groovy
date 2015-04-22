package fr.vter.xdcc.infrastructure.persistence.mongo

import org.mongolink.MongoSession
import org.mongolink.MongoSessionManager
import spock.lang.Specification

class MongoLinkContextTest extends Specification {

  def sessionManager = Mock(MongoSessionManager)
  def session = Mock(MongoSession)

  def setup() {
    sessionManager.createSession() >> session
  }

  def "starts a session at the beginning of a command"() {
    given:
    def context = new MongoLinkContext(sessionManager)

    when:
    context.beforeExecution()

    then:
    1 * session.start()
    context.currentSession() == session
  }

  def "flushes the session after the execution of a command"() {
    given:
    def context = new MongoLinkContext(sessionManager)

    when:
    context.afterExecution()

    then:
    1 * session.flush()
  }

  def "ultimately stops the session"() {
    given:
    def context = new MongoLinkContext(sessionManager)

    when:
    context.ultimately()

    then:
    1 * session.stop()
  }

  def "clears the session on error"() {
    given:
    def context = new MongoLinkContext(sessionManager)

    when:
    context.onError()

    then:
    1 * session.clear()
  }
}
