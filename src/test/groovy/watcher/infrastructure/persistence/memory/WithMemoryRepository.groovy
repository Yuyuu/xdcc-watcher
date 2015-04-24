package watcher.infrastructure.persistence.memory

import org.junit.rules.ExternalResource
import watcher.model.RepositoryLocator

class WithMemoryRepository extends ExternalResource {

  @Override
  protected void before() throws Throwable {
    RepositoryLocator.initialize(new MemoryRepositoryLocator())
  }

  @Override
  protected void after() {
    RepositoryLocator.initialize(null)
  }
}
