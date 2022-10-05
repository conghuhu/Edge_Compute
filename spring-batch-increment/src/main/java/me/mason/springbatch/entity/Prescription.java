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
 * @time: 2022/3/6 18:20
 */
@Component
@Data
@Table(name = "west_medical_storehouse")
public class Prescription {
    @Id
    @GeneratedValue
    @Column
    private int id;

    @Column
    private String prescriptionNumber;

    @Column
    private String prescriptionName;

    @Column
    private String prescriptionDescription;

    @Column
    private String prescriberId;

    @Column
    private String prescriberName;

    @Column(name = "sys_create_time")
    private Date sysCreateTime;

    @Column(name = "sys_update_time")
    private Date sysUpdateTime;


}