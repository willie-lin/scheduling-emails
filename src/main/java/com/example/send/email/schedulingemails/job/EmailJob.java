package com.example.send.email.schedulingemails.job;

import jdk.nashorn.internal.runtime.options.LoggingOption;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import sun.applet.Main;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 *
 * @Package: com.example.send.email.schedulingemails.job
 * @auther: YuAn
 * @Date: 2018/9/5
 * @Time: 21:58
 * @Project_name: scheduling-emails
 * To change this template use File | Settings | File Templates.
 * @Description:
 */
@Component
public class EmailJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();

        String subject = jobDataMap.getString("subject");

        String body = jobDataMap.getString("body");

        String recipientEmail = jobDataMap.getString("email");

        sendMail(mailProperties.getUsername(), recipientEmail,subject, body);


    }

    private void sendMail(String fromEmail, String toEmail, String subject, String body) {

        try {
            logger.info("Sending Email to {}", toEmail);
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());

            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);

            mailSender.send(message);

        }catch (MessagingException e){
            logger.error("Failed to send email to {}", toEmail);
        }

    }
}
