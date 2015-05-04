package watcher.website;

import watcher.schedule.configuration.WebsiteConfiguration;
import watcher.worker.WebsiteLocator;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

public class XdaysaysayWebsiteLocator extends WebsiteLocator {

  @Inject
  public XdaysaysayWebsiteLocator(WebsiteConfiguration websiteConfiguration) {
    this.websiteConfiguration = websiteConfiguration;
  }

  @Override
  protected URL getXdaysaysayUrl() throws IOException {
    return new URL(websiteConfiguration.xdaysaysayUrl);
  }

  private final WebsiteConfiguration websiteConfiguration;
}
