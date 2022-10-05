package me.mason.springbatch.example.incrementReverse.step;

import me.mason.springbatch.dao.edge.EdgePrescriptionRepository;
import me.mason.springbatch.dao.origin.OriginWMedicalSRepository;
import me.mason.springbatch.entity.Prescription;
import me.mason.springbatch.entity.WMedicalS;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/7 10:35
 */
public class IncrementReversePrescriptionItemWriter implements ItemWriter<Prescription> {
    @Autowired
    private EdgePrescriptionRepository EdgePrescriptionRepository;

    @Override
    public void write(List<? extends Prescription> items) throws Exception {
        if (Objects.nonNull(items)) {
            EdgePrescriptionRepository.getSQLManager().updateBatch("prescription.insertEdgeIncrePrescription", items);
        }
    }
}
