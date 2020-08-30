package com.homvee.insuranceoctopus.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homvee.insuranceoctopus.dao.DataLastInsInfoDao;
import com.homvee.insuranceoctopus.dao.DataLastInsKindPremiumDao;
import com.homvee.insuranceoctopus.dao.ReqCfgDao;
import com.homvee.insuranceoctopus.dao.entities.DataLastInsInfo;
import com.homvee.insuranceoctopus.dao.entities.DataLastInsKindPremium;
import com.homvee.insuranceoctopus.dao.entities.ReqCfg;
import com.homvee.insuranceoctopus.enums.StateEnum;
import com.homvee.insuranceoctopus.service.AbstractDataService;
import com.homvee.insuranceoctopus.service.DataLastInsKindPremiumService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataLastInsKindPremiumServiceImpl  extends AbstractDataService implements DataLastInsKindPremiumService {
    @Resource
    private DataLastInsInfoDao dataLastInsInfoDao;
    @Resource
    private DataLastInsKindPremiumDao dataLastInsKindPremiumDao;
    @Resource
    private ReqCfgDao reqCfgDao;

    @Transactional(rollbackFor = Exception.class)
    public void  save(DataLastInsInfo dataLastInsInfo , List<DataLastInsKindPremium> dataLastInsKindPremiums){
        dataLastInsInfo =  dataLastInsInfoDao.save(dataLastInsInfo);
        for (DataLastInsKindPremium dataLastInsKindPremium : dataLastInsKindPremiums){
            dataLastInsKindPremium.setLastInsInfoId(dataLastInsInfo.getId());
        }
        dataLastInsKindPremiumDao.saveAll(dataLastInsKindPremiums);
        reqCfgDao.updateStateById(StateEnum.FINISH.name() , dataLastInsInfo.getReqId());
    }



    @Override
    public void run(ReqCfg reqCfg, JSONObject data) {
        DataLastInsInfo dataLastInsInfo = new DataLastInsInfo();
        dataLastInsInfo.setReqId(reqCfg.getId());
        //车主信息
        JSONObject owner = (JSONObject) data.getJSONArray("renewInsuredVoList").get(0);
        if (owner == null || owner.isEmpty()){
            return;
        }
        makeOwnerInfo(dataLastInsInfo , owner);
        //车信息
        JSONObject car = data.getJSONObject("renewItemCarVo");
        if (car == null || car.isEmpty()){
            return;
        }
        makeCarInfo(dataLastInsInfo , car);
        //险种信息
        JSONArray kinds =  data.getJSONArray("renewItemKindVoList");
        if (kinds == null || kinds.isEmpty()){
            return;
        }
        List<DataLastInsKindPremium> dataLastInsKindPremiums = makeKindInfo(kinds);
        save(dataLastInsInfo , dataLastInsKindPremiums);
    }

    private  List<DataLastInsKindPremium> makeKindInfo(JSONArray kinds) {
        List<DataLastInsKindPremium> kindPremiums = Lists.newArrayList();
        for (Object kindObj : kinds){
            JSONObject kindJSON = (JSONObject) kindObj;
            DataLastInsKindPremium kindPremium = new DataLastInsKindPremium();
            kindPremiums.add(kindPremium);
            kindPremium.setKindCode(kindJSON.getString("kindCode"));
            kindPremium.setKindName(kindJSON.getString("kindName"));
            kindPremium.setPremium(kindJSON.getString("premium"));
            kindPremium.setDiscount(kindJSON.getString("discount"));
        }
        return  kindPremiums;
    }

    private void makeCarInfo(DataLastInsInfo dataLastInsInfo, JSONObject car) {
        dataLastInsInfo.setCarBrand(car.getString("brandName"));
        dataLastInsInfo.setCarEnrollDate(car.getString("enrollDate"));
        dataLastInsInfo.setCarFrameNo(car.getString("frameNo"));
        dataLastInsInfo.setCarLicenseNo(car.getString("licenseNo"));
        dataLastInsInfo.setCarPurchasePremium(car.getString("purchasePrice"));
        dataLastInsInfo.setDisplacement(car.getString("displacement"));
    }

    private void makeOwnerInfo(DataLastInsInfo dataLastInsInfo, JSONObject owner) {
        dataLastInsInfo.setContext(owner.toJSONString());
        dataLastInsInfo.setOwnerAddr(owner.getString("insuredAddress"));
        dataLastInsInfo.setOwnerIdNo(owner.getString("insuredIdentity"));
        dataLastInsInfo.setOwnerMobile(owner.getString("mobile"));
        dataLastInsInfo.setOwnerName(owner.getString("insuredName"));
    }

    @Override
    public boolean me(ReqCfg reqCfg) {
        return "/khyx/qth/price/quotePolicy.do".equals(reqCfg.getUri());
    }
}
