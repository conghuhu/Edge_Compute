package cn.cislc.dockerservice.config;

import cn.cislc.dockerservice.job.DockerHealthJob;

import org.quartz.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.Date;

/**
 * @author conghuhu
 * @create 2022-03-06 15:39
 */
@Configuration
public class QuartzConfig {

    @Bean(name = "dockerHealthJobDetail")
    public MethodInvokingJobDetailFactoryBean dockerHealthJobDetail(DockerHealthJob dockerHealthJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(true);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(dockerHealthJob);
        // 需要执行的方法
        jobDetail.setTargetMethod("executeInternal");
        return jobDetail;
    }

    @Bean(name = "dockerHealthTrigger")
    public SimpleTriggerFactoryBean dockerHealthTrigger(JobDetail dockerHealthJobDetail) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(dockerHealthJobDetail);
        // 设置任务启动延迟
        trigger.setStartDelay(0);
        // 设置定时任务启动时间
        trigger.setStartTime(new Date());
        trigger.setRepeatInterval(2500);
        return trigger;
    }

    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactory(JobDetail dockerHealthJobDetail, Trigger dockerHealthTrigger) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        // 延时启动，应用启动1秒后
        bean.setStartupDelay(1);
        bean.setJobDetails(dockerHealthJobDetail);
        // 注册触发器
        bean.setTriggers(dockerHealthTrigger);
        return bean;
    }
}
