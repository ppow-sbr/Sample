package com.ppc.eligibility.repository;

import com.ppc.eligibility.entity.EligibilityDataEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Yelleshc on 3/22/2018.
 */

@Repository
@Qualifier(value = "eligibilityDataRepository")
public interface EligibilityDataRepository extends JpaRepository<EligibilityDataEntity, Long> {


    @Query(value = "SELECT elig FROM EligibilityDataEntity elig WHERE elig.id.hostid  = :host_id and elig.id.custid =:cust_id")
    List<EligibilityDataEntity> getEligibilityDataEntry(@Param("host_id") String host_id, @Param("cust_id") String cust_id);


}
