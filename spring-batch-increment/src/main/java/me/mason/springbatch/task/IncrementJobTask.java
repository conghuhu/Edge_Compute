package me.mason.springbatch.task;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.mason.springbatch.common.LogConstants;
import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.dto.ResponseResult;
import me.mason.springbatch.entity.DataFlow;
import me.mason.springbatch.service.DataFlowService;
import me.mason.springbatch.service.IncrementReserveService;
import me.mason.springbatch.service.UpdateListenService;
import me.mason.springbatch.service.batch.JobLauncherService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: spring-batch-increment
 * @description
 * @author: LZ
 * @create: 2021-12-20 16:01
 **/
@Component
@Slf4j
public class IncrementJobTask {

    @Autowired
    private JobLauncherService jobLauncherService;
    @Autowired
    private IncrementReserveService incrementReserveService;
    @Autowired
    private DataFlowService dataFlowService;

    @Autowired
    @Qualifier("incrementWMedicalSJob")
    private Job incrementWMedicalSJob;



    @Autowired
    @Qualifier("incrementPrescriptionJob")
    private Job incrementPrescriptionJob;




    @Scheduled(cron = "0 0 * * * ?")
    public ResponseResult<String> runJobMedical() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = incrementReserveService.initWMedicalJobParam();
        Map<String, Object> stringObjectMap = jobLauncherService.startJob(incrementWMedicalSJob, jobParameters);
        Object resultStr = stringObjectMap.get(SyncConstants.STR_RETURN_RESULT);
        dataFlowService.jobWMedicalSInsertDataFlow();
        return ResponseResult.ok(resultStr);
    }




    @Scheduled(cron = "0 0 * * * ?")
    public ResponseResult<String> runJobPrescription() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = incrementReserveService.initPrescriptionJobParam();
        Map<String, Object> stringObjectMap = jobLauncherService.startJob(incrementPrescriptionJob, jobParameters);
        Object resultStr = stringObjectMap.get(SyncConstants.STR_RETURN_RESULT);
        dataFlowService.jobPrescriptionInsertDataFlow();
        return ResponseResult.ok(resultStr);
    }


}
