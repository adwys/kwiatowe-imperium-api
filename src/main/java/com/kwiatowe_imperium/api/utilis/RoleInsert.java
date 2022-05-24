package com.kwiatowe_imperium.api.utilis;

import com.kwiatowe_imperium.api.models.Role;
import com.kwiatowe_imperium.api.repo.EmailRepository;
import com.kwiatowe_imperium.api.repo.RoleRepository;
import com.kwiatowe_imperium.api.servies.EmailSenderService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RoleInsert implements ApplicationRunner {

    @Autowired
    RoleRepository repository;

    @Autowired
    EmailRepository emailRepository;
    @Autowired
    EmailSenderService emailSenderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(repository.findByName("ADMIN") == null){
            repository.save(new Role("ADMIN"));
        }
        if(repository.findByName("USER") == null){
            repository.save(new Role("USER"));
        }

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("emailRepository",emailRepository);
        jobDataMap.put("emailSenderService",emailSenderService);
        JobDetail jobDetail = JobBuilder.newJob(EmailCheckJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("CronTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *")).build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        scheduler.start();
        scheduler.scheduleJob(jobDetail,trigger);


    }
}
