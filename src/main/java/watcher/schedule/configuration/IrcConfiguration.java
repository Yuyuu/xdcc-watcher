package watcher.schedule.configuration;

import javax.inject.Inject;
import javax.inject.Named;

public class IrcConfiguration {

  @Inject
  @Named("irc.server")
  public String server;

  @Inject
  @Named("irc.channel")
  public String channel;
}
