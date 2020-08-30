package com.homvee.insuranceoctopus.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homvee.insuranceoctopus.dao.DataNoDamDao;
import com.homvee.insuranceoctopus.dao.ReqCfgDao;
import com.homvee.insuranceoctopus.dao.entities.DataNoDam;
import com.homvee.insuranceoctopus.dao.entities.ReqCfg;
import com.homvee.insuranceoctopus.enums.StateEnum;
import com.homvee.insuranceoctopus.service.AbstractDataService;
import com.homvee.insuranceoctopus.service.DataNoDamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class DataNoDamServiceImpl extends AbstractDataService implements DataNoDamService {
    @Resource
    private DataNoDamDao dataNoDamDao;
    @Resource
    private ReqCfgDao reqCfgDao;

    void save(List<DataNoDam> datas){
        dataNoDamDao.saveAll(datas);
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void run(ReqCfg reqCfg, JSONObject data) {
        JSONArray rows = data.getJSONArray("rows");
        DataNoDam dataNoDam = new DataNoDam();
        dataNoDam.setReqId(reqCfg.getId());
        dataNoDam.setContext(data.toJSONString());
        Integer years = 0;
        if (rows != null && rows.size() > 0){
            years =rows.getJSONObject(0).getInteger("noDamYearsBI");
        }
        dataNoDam.setYears(years == null ? 0 : years) ;
        dataNoDamDao.save(dataNoDam);
        reqCfgDao.updateStateById(StateEnum.FINISH.name() , reqCfg.getId());
    }

    @Override
    public boolean me(ReqCfg reqCfg) {
        return "/khyx/qth/price/quoteRenew.do".equals(reqCfg.getUri());
    }
}
