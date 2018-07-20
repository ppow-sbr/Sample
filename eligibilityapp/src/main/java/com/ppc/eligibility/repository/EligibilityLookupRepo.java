package com.ppc.eligibility.repository;

import com.ppc.eligibility.entity.EligibilityRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Yelleshc on 3/22/2018.
 */
@Repository
@Qualifier(value = "eligibilityLookupRepo")
public interface EligibilityLookupRepo extends JpaRepository<EligibilityRecord, Long> {

    @Query(value = "SELECT elig FROM EligibilityRecord elig WHERE  (upper(elig.customerId)  = upper(:eligibility_id)) AND elig.clientId=:client_id")
    EligibilityRecord getEligibilityRecord(@Param("eligibility_id") String eligibility_id, @Param("client_id") BigDecimal client_id);

    @Override
    List<EligibilityRecord> findAll();

    List<EligibilityRecord>  findByClientId(BigDecimal hostId);


   /*@Query(value = "select CSEA_UNIQUE_TOKEN.nextval from dual")
    long getUniqueToken();*/


}
