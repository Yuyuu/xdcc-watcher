package watcher.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watcher.model.bot.Bot;
import watcher.worker.parser.WebsiteParser;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class WebsiteWorker extends Worker {

  @Inject
  public WebsiteWorker(WebsiteParser websiteParser, BotListingUrlFinder botListingUrlFinder) {
    this.websiteParser = websiteParser;
    this.botListingUrlFinder = botListingUrlFinder;
  }

  public void updateAvailablePacks(String botNickname) {
    final Bot bot = getBotWithNickname(botNickname);

    final String botListingUrl = Optional.ofNullable(bot.url()).orElseGet(findListingUrl(botNickname));

    if (botListingUrl == null) {
      LOGGER.error("Listing URL of bot {} could not be found", botNickname);
      return;
    }

    try (final InputStream listingData = new URL(botListingUrl).openStream()) {
      final Map<Long, String> packDataFromWebsite = websiteParser.parsePacksFrom(listingData);
      bot.setListingUrl(botListingUrl);
      updatePacks(bot, packDataFromWebsite);
    } catch (IOException e) {
      bot.setListingUrl(null);
      LOGGER.error(
          "Failed to open a connection at {} listing URL [{}]: {}",
          botNickname,
          botListingUrl,
          e
      );
    }
  }

  private Supplier<String> findListingUrl(String botNickname) {
    return () -> {
      String listingUrl = null;
      try (final InputStream htmlData = WebsiteLocator.xdaysaysay().openStream()) {
        listingUrl = botListingUrlFinder.findListingUrl(botNickname, htmlData);
      } catch (IOException e) {
        LOGGER.error("Failed to open a connection to the listing website: {}", e);
      }
      return listingUrl;
    };
  }

  private final WebsiteParser websiteParser;
  private final BotListingUrlFinder botListingUrlFinder;
  private static final Logger LOGGER = LoggerFactory.getLogger(WebsiteWorker.class);
}
