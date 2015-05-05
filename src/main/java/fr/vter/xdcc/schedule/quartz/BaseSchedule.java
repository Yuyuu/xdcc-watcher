package fr.vter.xdcc.schedule.quartz;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSchedule {

  protected abstract void configure(Scheduler scheduler) throws Exception;

  protected static final Logger LOGGER = LoggerFactory.getLogger(BaseSchedule.class);
}
