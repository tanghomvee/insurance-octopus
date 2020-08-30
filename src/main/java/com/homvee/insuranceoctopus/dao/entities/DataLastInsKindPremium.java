package com.homvee.insuranceoctopus.dao.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *连续几年未出事故
 */
@Entity
@Table(name = "t_data_last_ins_kind_premium")
@lombok.Data
public class DataLastInsKindPremium extends  BaseEntity{
    private Long lastInsInfoId ;
    /**险种代码*/
    private String kindCode;
    /**险种名称*/
    private String kindName;
    /**险种费用*/
    private String premium;

    private String discount;
}
