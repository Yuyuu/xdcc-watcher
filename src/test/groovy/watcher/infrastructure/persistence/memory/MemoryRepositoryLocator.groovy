package watcher.infrastructure.persistence.memory

import watcher.model.RepositoryLocator
import watcher.model.bot.BotRepository

class MemoryRepositoryLocator extends RepositoryLocator {

  @Override
  protected BotRepository getBots() {
    return memoryBotRepository
  }

  private final MemoryBotRepository memoryBotRepository = new MemoryBotRepository()
}
