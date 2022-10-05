package me.mason.springbatch.example.increment.step;

import me.mason.springbatch.dao.edge.EdgePrescriptionRepository;
import me.mason.springbatch.dao.origin.OriginWMedicalSRepository;
import me.mason.springbatch.entity.Prescription;
import me.mason.springbatch.entity.WMedicalS;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/6 18:23
 */
public class IncrementPrescriptionItemReader implements ItemReader<Prescription> {
    protected List<Prescription> items;

    protected Map<String, Object> params;
    @Autowired
    private EdgePrescriptionRepository edgePrescriptionRepository;
    @Override
    public Prescription read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (Objects.isNull(items)) {
            //使用beetlsql的md执行sql
            items = edgePrescriptionRepository.getEdgeIncrePrescription(params);
            if (items.size() > 0) {
                return items.remove(0);
            }
        } else {
            if (!items.isEmpty()) {
                return items.remove(0);
            }
        }
        return null;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
