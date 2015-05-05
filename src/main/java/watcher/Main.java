package watcher;

import fr.vter.xdcc.schedule.quartz.ScheduleRunner;
import watcher.schedule.XdccSchedule;

public class Main {

  public static void main(String[] args) throws Exception {
    new ScheduleRunner(new XdccSchedule()).start();
  }
}
