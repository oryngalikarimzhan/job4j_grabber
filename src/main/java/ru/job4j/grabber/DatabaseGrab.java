package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.html.SqlRuParse;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.quartz.AlertRabbit;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class DatabaseGrab implements Grab {
    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        try (Connection connection = initConnection()) {
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
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
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection initConnection() {
        try (InputStream in = AlertRabbit.class.getClassLoader()
                .getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("rabbit.driver"));
            return DriverManager.getConnection(
                    config.getProperty("rabbit.url"),
                    config.getProperty("rabbit.username"),
                    config.getProperty("rabbit.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static class SqlRu implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            SqlRuParse sqlRuParse = (SqlRuParse) context
                    .getJobDetail()
                    .getJobDataMap()
                    .get("parse");
            DatabaseStore dbStore = (DatabaseStore) context
                    .getJobDetail()
                    .getJobDataMap()
                    .get("store");
            Connection connection = (Connection) context
                    .getJobDetail()
                    .getJobDataMap()
                    .get("connection");
            try (Statement statement = connection.createStatement()) {
                String tableCreator = "create table if not exists post ("
                        + "id serial primary key, "
                        + "title varchar(255), "
                        + "link varchar(255), "
                        + "description text, "
                        + "created timestamp"
                        + ");";
                statement.execute(tableCreator);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                dbStore.connect(connection);
                List<Post> posts = sqlRuParse.list("http://www.sql.ru/forum/job-offers/");
                posts.forEach(dbStore::save);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws SchedulerException {
        Parse sqlRuParse = new SqlRuParse(new SqlRuDateTimeParser());
        Store dbStore = new DatabaseStore();
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        Grab sqlRuGrab = new DatabaseGrab();
        sqlRuGrab.init(sqlRuParse, dbStore, scheduler);

    }
}
