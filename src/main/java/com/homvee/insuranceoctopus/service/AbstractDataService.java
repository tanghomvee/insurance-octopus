package com.homvee.insuranceoctopus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.homvee.insuranceoctopus.dao.entities.ReqCfg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
@Slf4j
public abstract class AbstractDataService implements DataService {
    @Override
    public void exec(ReqCfg reqCfg, String result) {
        if (StringUtils.isEmpty(result)){
            return;
        }
        JSONObject json = JSON.parseObject(result);
        if (json.containsKey("errorMsg")){
            log.error("处理URI返回数据错误：{}，{}", JSONObject.toJSON(reqCfg) , result);
            return;
        }
        run(reqCfg , json);
    }

   public abstract void run(ReqCfg reqCfg, JSONObject data);
}
