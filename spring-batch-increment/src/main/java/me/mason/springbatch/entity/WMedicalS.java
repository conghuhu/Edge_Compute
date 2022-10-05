package me.mason.springbatch.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/6 13:37
 */
@Component
@Data
@Table(name = "west_medical_storehouse")
public class WMedicalS {
    @Id
    @GeneratedValue
    @Column
    private int id;

    @Column
    private String warehouseNumberName;

    @Column
    private String drugNumber;

    @Column
    private String drugName;

    @Column
    private String description;

    @Column
    private String manufacturerId;

    @Column
    private String manufacturer;

    @Column
    private String batchNo;

    @Column
    private String packingUnit;

    @Column
    private String supplier;

    @Column
    private String supplierName;

    @Column(name = "sys_create_time")
    private Date sysCreateTime;

    @Column(name = "sys_update_time")
    private Date sysUpdateTime;


}
