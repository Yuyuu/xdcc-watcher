package watcher.worker

class FakeWebsiteLocator extends WebsiteLocator {

  @Override
  protected URL getXdaysaysayUrl() {
    getClass().classLoader.getResource("finder/empty_page.html")
  }
}
