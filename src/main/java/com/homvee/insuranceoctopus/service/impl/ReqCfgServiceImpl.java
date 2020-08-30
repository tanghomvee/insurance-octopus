package com.homvee.insuranceoctopus.service.impl;

import com.homvee.insuranceoctopus.dao.ReqCfgDao;
import com.homvee.insuranceoctopus.dao.entities.ReqCfg;
import com.homvee.insuranceoctopus.enums.StateEnum;
import com.homvee.insuranceoctopus.enums.YNEnum;
import com.homvee.insuranceoctopus.service.ReqCfgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReqCfgServiceImpl implements ReqCfgService {
    @Resource
    private ReqCfgDao reqCfgDao;

    @Override
    public List<ReqCfg> findByState(StateEnum state) {
        return reqCfgDao.findByStateAndYn(state.name() , YNEnum.YES.getVal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateStateById(String state, Long id) {
        return reqCfgDao.updateStateById(state , id);
    }
}
