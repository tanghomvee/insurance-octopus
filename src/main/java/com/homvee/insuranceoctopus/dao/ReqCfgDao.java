package com.homvee.insuranceoctopus.dao;

import com.homvee.insuranceoctopus.dao.entities.ReqCfg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReqCfgDao extends JpaRepository<ReqCfg, Long>  {
    @Query(value = "select * from t_req_cfg  where state=?1 and yn=?2 limit ?3,100" , nativeQuery = true)
    List<ReqCfg> findByStateAndYn(String state, Integer val, Integer start);

    @Transactional
    @Modifying
    @Query(value = "update t_req_cfg set state=?1,change_time=NOW() where id=?2 and yn =1" , nativeQuery = true)
    Integer updateStateById(String state, Long id);
}
