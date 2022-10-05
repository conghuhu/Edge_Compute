package me.mason.springbatch.example.incrementReverse.step;

import me.mason.springbatch.dao.origin.OriginWMedicalSRepository;
import me.mason.springbatch.dao.target.TargetWMedicalSRepository;
import me.mason.springbatch.entity.WMedicalS;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/6 16:29
 */
public class IncrementReverseWMedicalSItemWriter implements ItemWriter<WMedicalS> {
    @Autowired
    private OriginWMedicalSRepository originWMedicalSRepository;

    @Override
    public void write(List<? extends WMedicalS> items) throws Exception {
        if (Objects.nonNull(items)) {
            originWMedicalSRepository.getSQLManager().updateBatch("wMedicalS.insertTargetIncreWMedicalS", items);
        }
    }
}
