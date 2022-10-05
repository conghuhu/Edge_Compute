package me.mason.springbatch.service;

import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.dao.edge.EdgePrescriptionRepository;
import me.mason.springbatch.dao.origin.OriginWMedicalSRepository;
import me.mason.springbatch.dao.target.TargetPrescriptionRepository;
import me.mason.springbatch.dao.target.TargetWMedicalSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/5 13:09
 */
@Service
public class IncrementReserveService extends CommonService {
    @Autowired
    private TargetWMedicalSRepository targetWMedicalSRepository;


    @Autowired
    private OriginWMedicalSRepository originWMedicalSRepository;

    @Autowired
    private TargetPrescriptionRepository targetPrescriptionRepository;

    @Autowired
    private EdgePrescriptionRepository edgePrescriptionRepository;


    @Override
    public int getCdcTempId() {
        return SyncConstants.CDC_TEMP_ID_USER;
    }

    @Override
    public Date selectWMedicalMaxUpdateTime() {
        return targetWMedicalSRepository.selectMaxUpdateTime();
    }

    @Override
    public Date selectReverseWMedicalMaxUpdateTime() {
        return originWMedicalSRepository.selectMaxUpdateTime();
    }

    @Override
    public Date selectPrescriptionMaxUpdateTime() {
        return targetPrescriptionRepository.selectMaxUpdateTime();
    }

    @Override
    public Date selectReversePrescriptionMaxUpdateTime() {
        return edgePrescriptionRepository.selectMaxUpdateTime();
    }
}
