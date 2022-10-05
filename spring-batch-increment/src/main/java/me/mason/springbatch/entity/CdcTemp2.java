package me.mason.springbatch.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @program: MyEdgeComputing
 * @description
 * @author: LZ
 * @create: 2021-12-21 13:46
 **/
@Entity
@Table(name="cdc_temp2")
@Data
public class CdcTemp2 {
    @Id
    @GeneratedValue
    private Integer id ;
    /**
     前进数(时间单位根据具体sql而定)
     */
    private String aheadStepNum ;
    /**
     回退数(时间单位根据具体sql而定)
     */
    private String rollBackNum ;
    /**
     备注
     */
    private String comment ;
    /**
     作业标识
     */
    private String jobTag ;
    /**
     当前更新时间
     */
    private Date currentUpdateTime ;
    /**
     上次更新的数据最新时间
     */
    private Date lastUpdateTime ;

    /**
     job状态
     */
    private String status;
}
