package me.mason.springbatch.controller;

import io.swagger.annotations.ApiOperation;
import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.dto.ResponseResult;
import me.mason.springbatch.entity.DataFlow;
import me.mason.springbatch.service.DataFlowService;
import me.mason.springbatch.service.IncrementReserveService;
import me.mason.springbatch.service.batch.JobLauncherService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * increment控制器
 *
 * @author mason
 * @date 2019/6/1
 */
@RestController
@RequestMapping("/increment")
public class IncrementController {

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
    @Qualifier("incrementReverseWMedicalSJob")
    private Job incrementReverseWMedicalSJob;

    @Autowired
    @Qualifier("incrementPrescriptionJob")
    private Job incrementPrescriptionJob;

    @Autowired
    @Qualifier("incrementReversePrescriptionJob")
    private Job incrementReversePrescriptionJob;


    @ApiOperation(value = "运行任务", tags = "increment", notes = "使用spring batch运行任务")
    @GetMapping("/run_medical")
    public ResponseResult<String> runJobMedical() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = incrementReserveService.initWMedicalJobParam();
        Map<String, Object> stringObjectMap = jobLauncherService.startJob(incrementWMedicalSJob, jobParameters);
        Object resultStr = stringObjectMap.get(SyncConstants.STR_RETURN_RESULT);
        dataFlowService.jobWMedicalSInsertDataFlow();
        return ResponseResult.ok(resultStr);
    }

    @GetMapping("/reverse_medical")
    public ResponseResult<String> reverseMedical() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = incrementReserveService.initReverseWMedicalJobParam();
        Map<String, Object> stringObjectMap = jobLauncherService.startJob(incrementReverseWMedicalSJob, jobParameters);
        Object resultStr = stringObjectMap.get(SyncConstants.STR_RETURN_RESULT);
        dataFlowService.jobWMedicalSReverseInsertDataFlow();
        return ResponseResult.ok(resultStr);
    }


    @ApiOperation(value = "运行任务", tags = "increment", notes = "使用spring batch运行任务")
    @GetMapping("/run_prescription")
    public ResponseResult<String> runJobPrescription() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = incrementReserveService.initPrescriptionJobParam();
        Map<String, Object> stringObjectMap = jobLauncherService.startJob(incrementPrescriptionJob, jobParameters);
        Object resultStr = stringObjectMap.get(SyncConstants.STR_RETURN_RESULT);
        dataFlowService.jobPrescriptionInsertDataFlow();
        return ResponseResult.ok(resultStr);
    }

    @GetMapping("/reverse_prescription")
    public ResponseResult<String> reverseJobPrescription() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = incrementReserveService.initReversePrescriptionJobParam();
        Map<String, Object> stringObjectMap = jobLauncherService.startJob(incrementReversePrescriptionJob, jobParameters);
        Object resultStr = stringObjectMap.get(SyncConstants.STR_RETURN_RESULT);
        dataFlowService.jobPrescriptionReverseInsertDataFlow();
        return ResponseResult.ok(resultStr);
    }


    @GetMapping("/hangDead_medical")
    public ResponseResult<String> hangDead() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = incrementReserveService.initWMedicalJobParam();
        Map<String, Object> stringObjectMap = jobLauncherService.startJob(incrementWMedicalSJob, jobParameters);
        Object resultStr = stringObjectMap.get(SyncConstants.STR_RETURN_RESULT);
        dataFlowService.jobWMedicalSDeadInsertDataFlow();
        return ResponseResult.ok(resultStr);
    }

    @GetMapping("/hangDead_prescription")
    public ResponseResult<String> hangDead1() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = incrementReserveService.initPrescriptionJobParam();
        Map<String, Object> stringObjectMap = jobLauncherService.startJob(incrementPrescriptionJob, jobParameters);
        Object resultStr = stringObjectMap.get(SyncConstants.STR_RETURN_RESULT);
        dataFlowService.jobPrescriptionDeadInsertDataFlow();
        return ResponseResult.ok(resultStr);
    }

    @GetMapping("/getDataFlow")
    public ResponseResult<List<DataFlow>> getDataFlow()  {
        List<DataFlow> list =new ArrayList<DataFlow>();
        list = dataFlowService.getDataFlow();
        return ResponseResult.ok(list);
    }
}
