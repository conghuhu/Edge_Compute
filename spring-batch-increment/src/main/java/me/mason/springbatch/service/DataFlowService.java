package me.mason.springbatch.service;

import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.dao.local.DataFlowRepository;
import me.mason.springbatch.entity.DataFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/5 14:57
 */
@Service
public class DataFlowService {

    @Autowired
    private DataFlow dataFlow;

    @Autowired
    private DataFlowRepository dataFlowRepository;

    public void jobWMedicalSInsertDataFlow() {
        //数据流向的插入
        Date date = new Date(System.currentTimeMillis());
        String fromData = new String();
        fromData = "主机号：150.158.97.5  西药库";
        String toData = new String("主机号：101.42.243.67  数据云端");
        String status = SyncConstants.STR_RETURN_RESULT;
        dataFlow.setExecutionTime(date);
        dataFlow.setFromData(fromData);
        dataFlow.setToData(toData);
        dataFlow.setStatus("COMPLETED");
        dataFlowRepository.getSQLManager().update("user.insertDataFlow", dataFlow);
    }

    public void jobPrescriptionInsertDataFlow() {
        //数据流向的插入
        Date date = new Date(System.currentTimeMillis());
        String fromData = new String();
        fromData = "主机号：124.222.224.173  处方";
        String toData = new String(">主机号：101.42.243.67  数据云端");
        String status = SyncConstants.STR_RETURN_RESULT;
        dataFlow.setExecutionTime(date);
        dataFlow.setFromData(fromData);
        dataFlow.setToData(toData);
        dataFlow.setStatus("COMPLETED");
        dataFlowRepository.getSQLManager().update("user.insertDataFlow", dataFlow);
    }

    public void jobWMedicalSReverseInsertDataFlow() {
        //数据流向的插入
        Date date = new Date(System.currentTimeMillis());
        String fromData = new String();
        fromData = "主机号：101.42.243.67  数据云端";
        String toData = new String("主机号：150.158.97.5  西药库");
        String status = SyncConstants.STR_RETURN_RESULT;
        dataFlow.setExecutionTime(date);
        dataFlow.setFromData(fromData);
        dataFlow.setToData(toData);
        dataFlow.setStatus("COMPLETED");
        dataFlowRepository.getSQLManager().update("user.insertDataFlow", dataFlow);
    }

    public void jobPrescriptionReverseInsertDataFlow() {
        //数据流向的插入
        Date date = new Date(System.currentTimeMillis());
        String fromData = new String();
        fromData = "主机号：101.42.243.67  数据云端";
        String toData = new String(">主机号：124.222.224.173  处方");
        String status = SyncConstants.STR_RETURN_RESULT;
        dataFlow.setExecutionTime(date);
        dataFlow.setFromData(fromData);
        dataFlow.setToData(toData);
        dataFlow.setStatus("COMPLETED");
        dataFlowRepository.getSQLManager().update("user.insertDataFlow", dataFlow);
    }

    public void jobWMedicalSDeadInsertDataFlow() {
        //数据流向的插入
        Date date = new Date(System.currentTimeMillis());
        String fromData = new String();
        fromData = "主机号：150.158.97.5  中药库";
        String toData = new String("主机号：101.42.243.67  数据云端");
        String status = SyncConstants.STR_RETURN_RESULT;
        dataFlow.setExecutionTime(date);
        dataFlow.setFromData(fromData);
        dataFlow.setToData(toData);
        dataFlow.setStatus("FAILED");
        dataFlowRepository.getSQLManager().update("user.insertDataFlow", dataFlow);
    }

    public void jobPrescriptionDeadInsertDataFlow() {
        //数据流向的插入
        Date date = new Date(System.currentTimeMillis());
        String fromData = new String();
        fromData = "主机号：124.222.224.173  处方";
        String toData = new String("主机号：101.42.243.67  数据云端");
        String status = SyncConstants.STR_RETURN_RESULT;
        dataFlow.setExecutionTime(date);
        dataFlow.setFromData(fromData);
        dataFlow.setToData(toData);
        dataFlow.setStatus("FAILED");
        dataFlowRepository.getSQLManager().update("user.insertDataFlow", dataFlow);
    }

    public List<DataFlow> getDataFlow(){
        List<DataFlow> dataFlows=new ArrayList<DataFlow>();
        dataFlows=dataFlowRepository.all();
        return dataFlows;
    }
}
