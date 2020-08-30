package com.homvee.insuranceoctopus.dao.entities;



import com.homvee.insuranceoctopus.enums.YNEnum;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-01-04 18:30
 */
@MappedSuperclass
@Data
public class BaseEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    protected Long id;

    /**
     * 是否有效
     * 1:有效,-1无效
     */
    protected Integer yn;

    /**
     * 创建人
     */
    protected String creator;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 修改人
     */
    protected String changer;

    /**
     * 修改时间
     */
    protected Date changeTime;

    public BaseEntity() {
        this.createTime = new Date();
        this.yn = YNEnum.YES.getVal();
    }
}
