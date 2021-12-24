package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.html.SqlRuParse;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.util.ArrayList;
import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class MemGrab implements Grab {
    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) {
        try {
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("parse", parse);
            data.put("store", store);
            JobDetail job = newJob(SqlRu.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInMinutes(20)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(1000000);
            scheduler.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class SqlRu implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            SqlRuParse sqlRuParse = (SqlRuParse) context
                    .getJobDetail()
                    .getJobDataMap()
                    .get("parse");
            MemStore memStore = (MemStore) context
                    .getJobDetail()
                    .getJobDataMap()
                    .get("store");
            List<Post> posts = new ArrayList<>();
            try {
                posts = sqlRuParse.list("https://www.sql.ru/forum/job-offers/");
            } catch (Exception e) {
                e.printStackTrace();
            }
            posts.forEach(memStore::save);
            memStore.getAll().forEach(System.out::println);
            System.out.println(memStore.findById(10));
        }
    }

    public static void main(String[] args) throws Exception {
      Parse sqlRuParse = new SqlRuParse(new SqlRuDateTimeParser());
        Store memStore = new MemStore();
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        Grab sqlRuGrab = new MemGrab();
        sqlRuGrab.init(sqlRuParse, memStore, scheduler);
    }
}
