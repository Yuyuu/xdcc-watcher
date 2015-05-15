package watcher.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watcher.model.RepositoryLocator;
import watcher.model.bot.Bot;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BotWorker {

  public void updateAvailableBots(List<String> namesOfBotsInChannel) {
    final List<Bot> botsInRepository = RepositoryLocator.bots().findAllWithoutLoadingPacks();

    addNewBots(botsInRepository, namesOfBotsInChannel);
    removeObsoleteBots(botsInRepository, namesOfBotsInChannel);
  }

  private static void addNewBots(List<Bot> botsInRepository, List<String> namesOfBotsInChannel) {
    final List<String> namesOfBotsInRepository = botsInRepository.stream()
        .map(Bot::nickname)
        .collect(Collectors.toList());

    final Set<Bot> botsToAdd = namesOfBotsInChannel.stream()
        .filter(nickname -> !namesOfBotsInRepository.contains(nickname))
        .map(Bot::new)
        .collect(Collectors.toSet());

    LOGGER.info("Adding {} newly available bots", botsToAdd.size());
    RepositoryLocator.bots().addAll(botsToAdd);
  }

  private static void removeObsoleteBots(List<Bot> botsInRepository, List<String> namesOfBotsInChannel) {
    final List<Bot> botsToRemove = botsInRepository.stream()
        .filter(bot -> !namesOfBotsInChannel.contains(bot.nickname()))
        .collect(Collectors.toList());

    LOGGER.info("Removing {} bots which are not available anymore", botsToRemove.size());
    RepositoryLocator.bots().removeAll(botsToRemove);
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(BotWorker.class);
}
