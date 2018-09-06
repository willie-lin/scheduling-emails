package com.example.send.email.schedulingemails.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Package: com.example.send.email.schedulingemails.payload
 * @auther: YuAn
 * @Date: 2018/9/5
 * @Time: 19:15
 * @Project_name: scheduling-emails
 * To change this template use File | Settings | File Templates.
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleEmailResponse {


    private boolean success;
    private String jobId;
    private String jobGroup;
    private String message;

    public ScheduleEmailResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }


    public ScheduleEmailResponse(boolean success, String jobId, String jobGroup, String message) {
        this.success = success;
        this.jobId = jobId;
        this.jobGroup = jobGroup;
        this.message = message;
    }


}
