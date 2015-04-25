package watcher.worker;

import java.io.IOException;
import java.net.URL;

public abstract class WebsiteLocator {

  public static void initialize(WebsiteLocator instance) {
    WebsiteLocator.instance = instance;
  }

  public static URL xdaysaysay() throws IOException {
    return instance.getXdaysaysayUrl();
  }

  protected abstract URL getXdaysaysayUrl() throws IOException;

  private static WebsiteLocator instance;
}
