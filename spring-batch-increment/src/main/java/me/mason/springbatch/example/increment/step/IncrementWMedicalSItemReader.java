package me.mason.springbatch.example.increment.step;

import me.mason.springbatch.dao.origin.OriginWMedicalSRepository;
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
 * @time: 2022/3/6 13:44
 */
public class IncrementWMedicalSItemReader implements ItemReader<WMedicalS> {
    protected List<WMedicalS> items;

    protected Map<String, Object> params;
    @Autowired
    private OriginWMedicalSRepository originWMedicalSRepository;
    @Override
    public WMedicalS read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (Objects.isNull(items)) {
            //使用beetlsql的md执行sql
            items = originWMedicalSRepository.getOriginIncreWMedicalS(params);
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
