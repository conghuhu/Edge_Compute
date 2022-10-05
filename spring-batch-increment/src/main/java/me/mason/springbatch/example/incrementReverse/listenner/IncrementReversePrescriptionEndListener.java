package me.mason.springbatch.example.incrementReverse.listenner;

import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.dao.edge.EdgePrescriptionRepository;
import me.mason.springbatch.dao.origin.OriginWMedicalSRepository;
import me.mason.springbatch.service.CdcTempService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/7 10:40
 */
public class IncrementReversePrescriptionEndListener extends JobExecutionListenerSupport {

    @Autowired
    private CdcTempService cdcTempService;

    @Autowired
    private EdgePrescriptionRepository edgePrescriptionRepository;

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus status = jobExecution.getStatus();
        Date latestDate = edgePrescriptionRepository.selectMaxUpdateTime();
        cdcTempService.updateCdcTemp2AfterJob(SyncConstants.CDC_TEMP_ID_USER, status, latestDate);
    }
}
