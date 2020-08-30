package com.homvee.insuranceoctopus.dao.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *连续几年未出事故
 */
@Entity
@Table(name = "t_data_no_dam")
@lombok.Data
public class DataNoDam extends  BaseEntity{
    private Long reqId;
    /**年*/
    private Integer years;
    /**返回结果*/
    private String context;
}
