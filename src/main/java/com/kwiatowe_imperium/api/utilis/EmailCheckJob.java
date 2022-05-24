package com.kwiatowe_imperium.api.utilis;

import com.kwiatowe_imperium.api.models.Email;
import com.kwiatowe_imperium.api.repo.EmailRepository;
import com.kwiatowe_imperium.api.servies.EmailSenderService;
import lombok.AllArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class EmailCheckJob extends QuartzJobBean {

    EmailRepository emailRepository;

    EmailSenderService emailSenderService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        System.out.println("dupa");
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        emailRepository = (EmailRepository) jobDataMap.get("emailRepository");
        emailSenderService = (EmailSenderService) jobDataMap.get("emailSenderService");

        List<Email> emailList = emailRepository.findAll();

        for (Email email : emailList) {

            System.out.println(email.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth() + " XDD " + LocalDateTime.now().getDayOfMonth());
            if(email.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()){
                emailSenderService.sendEmail(email.getSendTo(),email.getSubject(),email.getBody());
                emailRepository.delete(email);
            }
        }
    }
}
