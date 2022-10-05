package me.mason.springbatch.example.incrementReverse.listenner;

import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.dao.origin.OriginWMedicalSRepository;
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
 * @time: 2022/3/6 16:35
 */
public class IncrementReverseWMedicalSEndListener extends JobExecutionListenerSupport {

    @Autowired
    private CdcTempService cdcTempService;

    @Autowired
    private OriginWMedicalSRepository originWMedicalSRepository;

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus status = jobExecution.getStatus();
        Date latestDate = originWMedicalSRepository.selectMaxUpdateTime();
        cdcTempService.updateCdcTempAfterJob(SyncConstants.CDC_TEMP_ID_USER, status, latestDate);
    }
}
