package me.mason.springbatch.example.increment.step;

import lombok.extern.slf4j.Slf4j;
import me.mason.springbatch.common.LogConstants;
import me.mason.springbatch.entity.Prescription;
import org.springframework.batch.item.ItemProcessor;

import java.util.Objects;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/6 18:30
 */
@Slf4j
public class IncrementPrescriptionItemProcessor implements ItemProcessor<Prescription, Prescription> {
    @Override
    public Prescription process(Prescription prescription) throws Exception {
        String title = prescription.getPrescriptionName();
        if (Objects.nonNull(title)) {
            prescription.setPrescriptionName(title.toUpperCase());
        }
        log.info(LogConstants.LOG_TAG + "increment item process: " + prescription.getPrescriptionDescription());
        return prescription;
    }
}
