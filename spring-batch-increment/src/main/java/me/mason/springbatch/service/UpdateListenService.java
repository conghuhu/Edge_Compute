package me.mason.springbatch.service;

import lombok.extern.slf4j.Slf4j;
import me.mason.springbatch.common.LogConstants;
import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.dao.edge.EdgePrescriptionRepository;
import me.mason.springbatch.dao.local.CdcTemp2Repository;
import me.mason.springbatch.dao.local.CdcTempRepository;
import me.mason.springbatch.dao.origin.OriginWMedicalSRepository;
import me.mason.springbatch.entity.CdcTemp;
import me.mason.springbatch.entity.CdcTemp2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: MyEdgeComputing
 * @description
 * @author: LZ
 * @create: 2021-12-23 14:19
 **/
@Service
@Slf4j
public class UpdateListenService {
    @Autowired
    private CdcTempRepository cdcTempRepository;
    @Autowired
    private CdcTemp2Repository cdcTemp2Repository;
    @Autowired
    private OriginWMedicalSRepository originWMedicalSRepository;
    @Autowired
    private EdgePrescriptionRepository edgePrescriptionRepository;

    /**
     * 判断Oringin表是否有更新,有更新则同步cdc时间
     */
    public boolean isExcuteUpdateOringin() {
        CdcTemp temp = cdcTempRepository.getSQLManager().single(CdcTemp.class, SyncConstants.CDC_ISUPDATE_TEMP_ID_USER);
        Date oringin_Date = originWMedicalSRepository.selectMaxUpdateTime();
        Date cdc_Date = temp.getLastUpdateTime();
        //因为UPDATE_DATE总是大于等于CREATE_DATE,所以只比较update即可
        if (oringin_Date.after(cdc_Date)) {
            temp.setLastUpdateTime(oringin_Date);
            cdcTempRepository.updateById(temp);
            log.debug(LogConstants.LOG_TAG + "Oringin表发生更新");
            return true;
        }
        return false;

    }

    /**
     * 判断edge表是否有更新，有更新则同步cdc时间
     */
    public boolean isExcuteUpdateEdge() {
        CdcTemp2 temp = cdcTemp2Repository.getSQLManager().single(CdcTemp2.class, SyncConstants.CDC_ISUPDATE_TEMP_ID_USER);
        Date Edge_Date = edgePrescriptionRepository.selectMaxUpdateTime();
        Date cdc_Date = temp.getLastUpdateTime();
        //因为UPDATE_DATE总是大于等于CREATE_DATE,所以只比较update即可
        if (Edge_Date.after(cdc_Date)) {
            temp.setLastUpdateTime(Edge_Date);
            cdcTemp2Repository.updateById(temp);
            log.debug(LogConstants.LOG_TAG + "edge表发生更新");
            return true;
        }
        return false;

    }
}
