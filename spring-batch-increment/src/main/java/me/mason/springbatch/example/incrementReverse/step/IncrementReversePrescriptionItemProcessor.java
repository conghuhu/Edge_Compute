package me.mason.springbatch.example.incrementReverse.step;

import lombok.extern.slf4j.Slf4j;
import me.mason.springbatch.common.LogConstants;
import me.mason.springbatch.entity.Prescription;
import me.mason.springbatch.entity.WMedicalS;
import org.springframework.batch.item.ItemProcessor;

import java.util.Objects;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/7 10:38
 */
@Slf4j
public class IncrementReversePrescriptionItemProcessor implements ItemProcessor<Prescription, Prescription> {
    @Override
    public Prescription process(Prescription prescription) throws Exception {
        String title = prescription.getPrescriptionDescription();
        if (Objects.nonNull(title)) {
            prescription.setPrescriptionDescription(title.toUpperCase());
        }
        log.info(LogConstants.LOG_TAG + "increment item process: " + prescription.getPrescriptionDescription());
        return prescription;
    }
}
