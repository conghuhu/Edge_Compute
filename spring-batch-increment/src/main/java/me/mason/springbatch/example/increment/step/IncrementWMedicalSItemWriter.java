package me.mason.springbatch.example.increment.step;

import me.mason.springbatch.dao.target.TargetWMedicalSRepository;
import me.mason.springbatch.entity.WMedicalS;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/6 14:02
 */
public class IncrementWMedicalSItemWriter implements ItemWriter<WMedicalS> {
    @Autowired
    private TargetWMedicalSRepository targetWMedicalSRepository;

    @Override
    public void write(List<? extends WMedicalS> items) throws Exception {
        if (Objects.nonNull(items)) {
            targetWMedicalSRepository.getSQLManager().updateBatch("wMedicalS.insertIncreWMedicalS", items);
        }
    }
}
