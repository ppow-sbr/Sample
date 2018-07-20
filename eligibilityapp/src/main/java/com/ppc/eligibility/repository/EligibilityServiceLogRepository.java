package com.ppc.eligibility.repository;

import com.ppc.eligibility.entity.EligibilityServiceLogEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Yelleshc on 3/22/2018.
 */

@Repository
@Qualifier(value = "eligibilityServiceLogRepository")
public interface EligibilityServiceLogRepository extends JpaRepository<EligibilityServiceLogEntity, Long> {
}
