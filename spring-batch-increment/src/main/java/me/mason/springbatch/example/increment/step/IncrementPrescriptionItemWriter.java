package me.mason.springbatch.example.increment.step;

import me.mason.springbatch.dao.target.TargetPrescriptionRepository;
import me.mason.springbatch.entity.Prescription;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/6 18:33
 */
public class IncrementPrescriptionItemWriter implements ItemWriter<Prescription> {
    @Autowired
    private TargetPrescriptionRepository targetPrescriptionRepository;

    @Override
    public void write(List<? extends Prescription> items) throws Exception {
        if (Objects.nonNull(items)) {
            targetPrescriptionRepository.getSQLManager().updateBatch("prescription.insertEdgeIncrePrescription", items);
        }
    }
}
