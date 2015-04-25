package watcher.worker

import org.junit.rules.ExternalResource

class WithFakeWebsite extends ExternalResource {

  @Override
  protected void before() throws Throwable {
    WebsiteLocator.initialize(new FakeWebsiteLocator())
  }

  @Override
  protected void after() {
    WebsiteLocator.initialize(null)
  }
}
