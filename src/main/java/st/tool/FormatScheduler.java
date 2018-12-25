package st.tool;

import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class FormatScheduler {

    private static final FormatScheduler formatScheduler = new FormatScheduler();

    private FormatScheduler() {
    }

    public static FormatScheduler getInit(Class<? extends Job> clazz, Map<String, Object> data) {
        formatScheduler.initScheduler();
        formatScheduler.initJob(clazz, data);
        return formatScheduler;
    }

    private Scheduler scheduler;
    private Trigger   trigger;
    private JobDetail job;

    private void initScheduler() {
        try {
            if (scheduler == null)
                scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private void initJob(Class<? extends Job> clazz, Map<String, Object> data) {
        JobBuilder jobB = JobBuilder.newJob(clazz);
        if (data != null) {
            JobDataMap jobData = new JobDataMap();
            jobData.putAll(data);
            jobB.usingJobData(jobData);
        }
        job = jobB.build();
    }

    private void start() {
        try {
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void startSimpleTrigger(int intervalSeconds) {
        TriggerBuilder<Trigger> tgrB = TriggerBuilder.newTrigger();
        tgrB.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(intervalSeconds));
        trigger = tgrB.build();
        start();
    }

    public void startCronTrigger(String cron) {
        TriggerBuilder<Trigger> tgrB = TriggerBuilder.newTrigger();
        tgrB.withSchedule(CronScheduleBuilder.cronSchedule(cron));
        trigger = tgrB.build();
        start();
    }

    public void stop() {
        // Thread.sleep(1000);// 运行一段时间后关闭
        try {
            scheduler.shutdown(true);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}

class JobTemp implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail detail = context.getJobDetail();
        JobDataMap jobData = detail.getJobDataMap();
        Object list = jobData.get("list");
        System.out.println(FormatCalendar.format(FormatCalendar.now()) + "--" + list);
    }

    public static void main(String[] args) {
        FormatScheduler fs = FormatScheduler.getInit(JobTemp.class, null);
        // fs.startSimpleTrigger(intervalSeconds);
        fs.startCronTrigger("0/2 * * * * ?");
    }

}