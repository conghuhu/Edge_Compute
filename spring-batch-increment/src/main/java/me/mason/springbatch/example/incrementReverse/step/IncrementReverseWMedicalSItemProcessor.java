package me.mason.springbatch.example.incrementReverse.step;

import lombok.extern.slf4j.Slf4j;
import me.mason.springbatch.common.LogConstants;
import me.mason.springbatch.entity.WMedicalS;
import org.springframework.batch.item.ItemProcessor;

import java.util.Objects;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/6 16:29
 */
@Slf4j
public class IncrementReverseWMedicalSItemProcessor implements ItemProcessor<WMedicalS, WMedicalS> {
    @Override
    public WMedicalS process(WMedicalS wMedicalS) throws Exception {
        String title = wMedicalS.getWarehouseNumberName();
        if (Objects.nonNull(title)) {
            wMedicalS.setWarehouseNumberName(title.toUpperCase());
        }
        log.info(LogConstants.LOG_TAG + "increment item process: " + wMedicalS.getDescription());
        return wMedicalS;
    }
}
