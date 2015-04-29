package watcher.irc.bot;

import watcher.irc.bot.state.StateHandler;

public interface ExternalState {

  void setStateHandler(StateHandler stateHandler);

  void terminate();
}
