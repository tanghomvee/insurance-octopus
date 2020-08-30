package com.homvee.insuranceoctopus.dao.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *连续几年未出事故
 */
@Entity
@Table(name = "t_data_last_ins_info")
@lombok.Data
public class DataLastInsInfo extends  BaseEntity{
    private Long reqId;
    /**车主身份证号*/
    private String ownerIdNo     ;
    /**车主地址*/
    private String ownerAddr  ;
    /**车主姓名*/
    private String ownerName  ;
    /**车主电话*/
    private String ownerMobile    ;
    /**车辆品牌*/
    private String carBrand   ;
    /**车辆初等日期*/
    private String carEnrollDate     ;
    /**车架号*/
    private String carFrameNo    ;
    /**车牌号*/
    private String carLicenseNo  ;
    /**新车购置价*/
    private String carPurchasePremium  ;
    /**交强险到期日*/
    private String ciEndDate     ;
    /**商业险到期日*/
    private String biEndDate     ;
    /**返回结果*/
    private String context;
    /**排量*/
    private String displacement;
}
