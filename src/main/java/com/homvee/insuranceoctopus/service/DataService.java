package com.homvee.insuranceoctopus.service;

import com.homvee.insuranceoctopus.dao.entities.ReqCfg;

public interface DataService {
    void exec(ReqCfg reqCfg,String result);
    boolean me(ReqCfg reqCfg);
}
