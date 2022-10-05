package me.mason.springbatch.example.increment.listener;

import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.dao.target.TargetPrescriptionRepository;
import me.mason.springbatch.dao.target.TargetWMedicalSRepository;
import me.mason.springbatch.service.CdcTempService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/6 18:45
 */
public class IncrementPrescriptionEndListener  extends JobExecutionListenerSupport {

    @Autowired
    private CdcTempService cdcTempService;

    @Autowired
    private TargetPrescriptionRepository targetPrescriptionRepository;

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus status = jobExecution.getStatus();
        Date latestDate = targetPrescriptionRepository.selectMaxUpdateTime();
        cdcTempService.updateCdcTemp2AfterJob(SyncConstants.CDC_TEMP_ID_USER, status, latestDate);
    }
}
