package com.homvee.insuranceoctopus.dao.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_req_cfg")
@Data
public class ReqCfg extends  BaseEntity{
    /**地址*/
    private String uri;
    /**参数*/
    private String param;
    /**请求方法*/
    private String method;
    /**请求类型*/
    private String contentType;
    /**获取参数*/
    private String respParamNames;
    /**cookie*/
    private String cookie;
    /**执行状态*/
    private String state;
    private String insCompany;



}
