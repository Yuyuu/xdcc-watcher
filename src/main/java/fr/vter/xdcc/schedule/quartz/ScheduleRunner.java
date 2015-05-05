package fr.vter.xdcc.schedule.quartz;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class ScheduleRunner {

  public ScheduleRunner(BaseSchedule schedule) {
    this.schedule = schedule;
  }

  public void start() throws Exception {
    Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
    schedule.configure(scheduler);
    scheduler.start();
  }

  private final BaseSchedule schedule;
}
