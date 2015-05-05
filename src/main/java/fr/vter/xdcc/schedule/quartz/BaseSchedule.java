package fr.vter.xdcc.schedule.quartz;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Handler;
import java.util.logging.LogManager;

public abstract class BaseSchedule {

  public BaseSchedule() {
    // Deactivate JUL logs
    final java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
    final Handler[] handlers = rootLogger.getHandlers();
    for (final Handler handler : handlers) {
      rootLogger.removeHandler(handler);
    }
    SLF4JBridgeHandler.install();
  }

  protected abstract void configure(Scheduler scheduler) throws Exception;

  protected static final Logger LOGGER = LoggerFactory.getLogger(BaseSchedule.class);
}
