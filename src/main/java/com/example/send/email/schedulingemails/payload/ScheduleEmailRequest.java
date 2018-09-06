package com.example.send.email.schedulingemails.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created with IntelliJ IDEA.
 *
 * @Package: com.example.send.email.schedulingemails.payload
 * @auther: YuAn
 * @Date: 2018/9/5
 * @Time: 19:14
 * @Project_name: scheduling-emails
 * To change this template use File | Settings | File Templates.
 * @Description:
 */

@Data
public class ScheduleEmailRequest {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String body;

    @NotNull
    private LocalDateTime dateTime;


    private ZoneId timeZone;

}
