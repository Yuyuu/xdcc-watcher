package watcher.schedule.configuration;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.mongodb.MongoClientURI;
import fr.vter.xdcc.infrastructure.persistence.mongo.MongoLinkContext;
import org.mongolink.MongoSessionManager;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;
import org.mongolink.domain.mapper.ContextBuilder;
import org.quartz.spi.JobFactory;
import watcher.infrastructure.persistence.mongo.RepositoryLocatorMongoLink;
import watcher.irc.bot.WatcherFactory;
import watcher.job.GuiceJobFactory;
import watcher.model.RepositoryLocator;
import watcher.website.XdaysaysayWebsiteLocator;
import watcher.worker.*;
import watcher.worker.parser.ListFileParser;
import watcher.worker.parser.WebsiteParser;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;

public class GuiceConfiguration extends AbstractModule {

  @Override
  protected void configure() {
    Names.bindProperties(binder(), properties());
    configurePersistence();
    configureWebsite();
    configureWorkers();
    configureJobs();
  }

  private Properties properties() {
    URL url = Resources.getResource("env/" + Optional.ofNullable(System.getenv("env")).orElse("dev") + "/application.properties");
    ByteSource inputSupplier = Resources.asByteSource(url);
    Properties properties = new Properties();
    try {
      properties.load(inputSupplier.openStream());
    } catch (IOException e) {
    }
    return properties;
  }

  private void configurePersistence() {
    bind(MongoLinkContext.class).in(Singleton.class);

    bind(RepositoryLocator.class).to(RepositoryLocatorMongoLink.class).in(Singleton.class);
  }

  private void configureWebsite() {
    bind(WebsiteLocator.class).to(XdaysaysayWebsiteLocator.class).in(Singleton.class);
  }

  private void configureWorkers() {
    bind(BotWorker.class).in(Singleton.class);

    bind(ListFileParser.class).in(Singleton.class);
    bind(ListFileWorker.class).in(Singleton.class);

    bind(BotListingUrlFinder.class).in(Singleton.class);

    bind(WebsiteParser.class).in(Singleton.class);
    bind(WebsiteWorker.class).in(Singleton.class);
  }

  private void configureJobs() {
    bind(JobFactory.class).to(GuiceJobFactory.class).in(Singleton.class);
    bind(WatcherFactory.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  public MongoSessionManager mongoLink() {
    final MongoClientURI uri = new MongoClientURI(
        Optional.ofNullable(System.getenv("XDCC_WATCHER_MONGO_URI"))
            .orElseThrow(() -> new IllegalStateException("Missing database configuration"))
    );
    Settings settings = Settings.defaultInstance().withDefaultUpdateStrategy(UpdateStrategies.DIFF)
        .withHost(extractHostname(uri.getHosts().get(0)))
        .withPort(extractPort(uri.getHosts().get(0)))
        .withDbName(uri.getDatabase());
    if (uri.getCredentials() != null) {
      settings = settings.withAuthentication(uri.getUsername(), new String(uri.getPassword()));
    }

    return MongoSessionManager.create(
        new ContextBuilder("watcher.infrastructure.persistence.mongo.mapping"),
        settings
    );
  }

  private static String extractHostname(String host) {
    int colonIndex = findColonIndex(host);
    return host.substring(0, colonIndex);
  }

  private static int extractPort(String host) {
    int colonIndex = findColonIndex(host);
    return Integer.parseInt(host.substring(colonIndex + 1));
  }

  private static int findColonIndex(String host) {
    int colonIndex = host.indexOf(':');
    if (colonIndex < 0) {
      throw new IllegalStateException("Malformed database URI");
    }
    return colonIndex;
  }
}
