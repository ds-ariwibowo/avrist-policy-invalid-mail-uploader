package com.example.jpa.repository;

import com.example.jpa.model.Detail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetailRepository extends JpaRepository<Detail, Long> {
    Page<Detail> findByMasterId(Long mstId, Pageable pageable);

    @Query(value = "SELECT * FROM t_address_fail_detail t WHERE t.master_id=:mstId ",
    nativeQuery = true)    
    List<Detail> findAllByMasterId(@Param("mstId") Long mstId);

    @Query(value = "SELECT * FROM t_address_fail_detail t WHERE t.policy_Code=:policyCode ",
    nativeQuery = true)    
    Optional<Detail> findByPolicy(@Param("policyCode") String policyCode);
   

    @Query(value = "SELECT policy_code,max(counter) as counter FROM t_address_fail_detail where master_id=:mstId group by policy_code ",
    nativeQuery = true)    
    public List<Detail> findAllMaxCounterByMstId(@Param("mstId") Long mstId);


    @Query(value = "SELECT Count(*) FROM t_address_fail_detail t WHERE t.policy_Code=:policyCode ",
    nativeQuery = true)    
    public Long countByPolicy(@Param("policyCode") String policyCode);

    @Modifying
    @Query(value ="update t_address set mail_failed_times=:counter where address_id="+
                  "(select address_id from t_policy_holder where policy_id=(select policy_id from t_contract_master where policy_code=:policyCode))",
    nativeQuery = true)
    @Transactional
    public void updateMailFailedTimes(@Param("counter") Long counter, @Param("policyCode") String policyCode);

    @Modifying
    @Query(value ="update t_address set address_status=2 where address_id="+
                  "(select address_id from t_policy_holder where policy_id=(select policy_id from t_contract_master where policy_code=:policyCode))",
    nativeQuery = true)
    @Transactional
    public void updateInvalidAddr(@Param("policyCode") String policyCode);
     
}
