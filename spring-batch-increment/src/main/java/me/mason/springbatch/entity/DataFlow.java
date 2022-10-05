package me.mason.springbatch.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/5 16:20
 */
@Component
@Data
@Table(name = "data_flow")
public class DataFlow {

    @Id
    @GeneratedValue
    @Column
    private int id;

    @Column
    private Date executionTime;

    @Column
    private String fromData;

    @Column
    private String toData;

    @Column
    private String status;

}
