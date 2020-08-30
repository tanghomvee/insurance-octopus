package com.homvee.insuranceoctopus.service;

import com.homvee.insuranceoctopus.dao.entities.ReqCfg;
import com.homvee.insuranceoctopus.enums.StateEnum;

import java.util.List;

public interface ReqCfgService {

    List<ReqCfg> findByState(StateEnum state);

    Integer updateStateById(String state, Long id);
}
