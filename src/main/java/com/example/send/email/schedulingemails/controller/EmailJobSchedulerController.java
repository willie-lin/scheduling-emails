package com.example.send.email.schedulingemails.controller;

import com.example.send.email.schedulingemails.job.EmailJob;
import com.example.send.email.schedulingemails.payload.ScheduleEmailRequest;
import com.example.send.email.schedulingemails.payload.ScheduleEmailResponse;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author YuAn
 * @Package: com.example.send.email.schedulingemails.controller
 * @auther: YuAn
 * @Date: 2018/9/6
 * @Time: 9:30
 * @Project_name: scheduling-emails
 * To change this template use File | Settings | File Templates.
 * @Description:
 */

@RestController
public class EmailJobSchedulerController {

    private static final Logger logger = LoggerFactory.getLogger(EmailJobSchedulerController.class);


    @Autowired
    private Scheduler scheduler;


    @PostMapping("/scheduleEmail")
    public ResponseEntity<ScheduleEmailResponse> schedulerEmail(@Valid @RequestBody ScheduleEmailRequest scheduleEmailRequest){
        try {

            ZonedDateTime dateTime = ZonedDateTime.of(scheduleEmailRequest.getDateTime(),scheduleEmailRequest.getTimeZone());

            if (dateTime.isBefore(ZonedDateTime.now())){
                ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false,

                        "dateTime must be after current time");
                return ResponseEntity.badRequest().body(scheduleEmailResponse);
            }

            JobDetail jobDetail = buildJobDetail(scheduleEmailRequest);

            Trigger trigger = buildJobTrigger(jobDetail, dateTime);

            scheduler.scheduleJob(jobDetail, trigger);

            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(true,
                    jobDetail.getKey().getName(), jobDetail.getKey().getGroup(),
                    "Email Scheduled Successfully！");
            return ResponseEntity.ok(scheduleEmailResponse);
        }catch (SchedulerException e){
            logger.error("Error scheduling email", e);

            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false, "Error scheduling email. Please try later!");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
        }

    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-trigger")
                .withDescription("Send Email Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).build();
    }

    private JobDetail buildJobDetail(ScheduleEmailRequest scheduleEmailRequest) {

        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", scheduleEmailRequest.getEmail());
        jobDataMap.put("subject", scheduleEmailRequest.getSubject());
        jobDataMap.put("body", scheduleEmailRequest.getBody());

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .build();
    }


}
