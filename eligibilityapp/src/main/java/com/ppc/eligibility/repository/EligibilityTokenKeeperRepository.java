package com.ppc.eligibility.repository;

import com.ppc.eligibility.entity.EligibilityTokenKeeper;
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
@Qualifier(value = "eligibilityTokenKeeperRepository")
public interface EligibilityTokenKeeperRepository extends JpaRepository<EligibilityTokenKeeper, Long> {

    @Query(value = "SELECT elig FROM EligibilityTokenKeeper elig WHERE elig.tokenNumber=:token_number")
    List<EligibilityTokenKeeper> getEligibilityTokenKeeper(@Param("token_number") String token_number);

}
