package watcher.schedule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import fr.vter.xdcc.schedule.quartz.BaseSchedule;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import watcher.job.BotWatchingJob;
import watcher.job.PackWatchingJob;
import watcher.model.RepositoryLocator;
import watcher.schedule.configuration.GuiceConfiguration;
import watcher.worker.WebsiteLocator;

import java.util.Optional;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class XdccSchedule extends BaseSchedule {

  public XdccSchedule() throws Exception {
    injector = Guice.createInjector(stage(), new GuiceConfiguration());
    RepositoryLocator.initialize(injector.getInstance(RepositoryLocator.class));
    WebsiteLocator.initialize(injector.getInstance(WebsiteLocator.class));
  }

  private Stage stage() {
    final Optional<String> env = Optional.ofNullable(System.getenv("env"));
    LOGGER.info("Configuration mode: {}", env.orElse("dev"));
    if (env.orElse("dev").equals("dev")) {
      return Stage.DEVELOPMENT;
    }
    return Stage.PRODUCTION;
  }

  @Override
  protected void configure(Scheduler scheduler) throws Exception {
    scheduler.setJobFactory(injector.getInstance(JobFactory.class));

    configureBotWatchingJob(scheduler);
    configurePackWatchingJob(scheduler);
  }

  private static void configureBotWatchingJob(Scheduler scheduler) throws SchedulerException {
    JobDetail job = newJob(BotWatchingJob.class)
        .withIdentity("BotJob", "WatcherGroup").build();
    Trigger trigger = newTrigger()
        .withIdentity("BotJobTrigger", "WatcherGroup")
        .startAt(futureDate(10, DateBuilder.IntervalUnit.SECOND))
        .withSchedule(simpleSchedule().withIntervalInHours(6).repeatForever())
        .build();

    scheduler.scheduleJob(job, trigger);
  }

  private static void configurePackWatchingJob(Scheduler scheduler) throws SchedulerException {
    JobDetail job = newJob(PackWatchingJob.class)
        .withIdentity("PackJob", "WatcherGroup")
        .usingJobData("maxBotsToUpdatePerJob", 15)
        .usingJobData("currentOffset", 0)
        .build();
    Trigger trigger = newTrigger()
        .withIdentity("PackJobTrigger", "WatcherGroup")
        .startAt(futureDate(15, DateBuilder.IntervalUnit.MINUTE))
        .withSchedule(simpleSchedule().withIntervalInMinutes(60).repeatForever())
        .build();

    scheduler.scheduleJob(job, trigger);
  }

  private final Injector injector;
}
