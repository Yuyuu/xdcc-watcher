package watcher.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watcher.model.RepositoryLocator;
import watcher.model.bot.Bot;
import watcher.model.bot.Pack;
import watcher.worker.parser.WebsiteParser;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class WebsiteWorker {

  @Inject
  public WebsiteWorker(WebsiteParser websiteParser, BotListingUrlFinder botListingUrlFinder) {
    this.websiteParser = websiteParser;
    this.botListingUrlFinder = botListingUrlFinder;
  }

  public void updateAvailablePacks(String botNickname) {
    Bot bot = getBotWithNickname(botNickname);

    String botListingUrl = Optional.ofNullable(bot.url()).orElseGet(findListingUrl(botNickname));

    if (botListingUrl == null) {
      LOGGER.error("Listing URL of bot {} could not be found", botNickname);
      return;
    }

    try (InputStream listingData = new URL(botListingUrl).openStream()) {
      Map<Long, String> packDataFromWebsite = websiteParser.parsePacksFrom(listingData);
      bot.setListingUrl(botListingUrl);
      internalUpdate(bot, packDataFromWebsite);
    } catch (IOException e) {
      LOGGER.error(
          "Failed to open a connection at {} listing URL [{}]: {}",
          botNickname,
          botListingUrl,
          e
      );
      bot.setListingUrl(null);
    }
  }

  private void internalUpdate(Bot bot, Map<Long,String> packDataFromWebsite) {
    Set<Pack> packs = packDataFromWebsite.entrySet().stream()
        .map(entry -> new Pack(entry.getKey(), entry.getValue()))
        .collect(Collectors.toSet());

    if (!packs.equals(bot.packs())) {
      bot.updatePacks(packs);
      LOGGER.info("Bot {} has new packs", bot.getNickname());
    } else {
      LOGGER.debug("Packs of bot {} remain unchanged", bot.getNickname());
    }

    bot.checked();
  }

  private Bot getBotWithNickname(String nickname) {
    final Optional<Bot> optional = RepositoryLocator.bots().findByNickname(nickname);
    return optional.orElseGet(createBot(nickname));
  }

  private Supplier<Bot> createBot(String botNickname) {
    return () -> {
      final Bot result = new Bot(botNickname);
      RepositoryLocator.bots().add(result);
      return result;
    };
  }

  private Supplier<String> findListingUrl(String botNickname) {
    return () -> {
      String listingUrl = null;
      try (InputStream htmlData = WebsiteLocator.xdaysaysay().openStream()) {
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
