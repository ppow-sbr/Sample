package com.ppc.eligibility.service;


import com.ppc.eligibility.model.EligibilityVerificationRequest;
import com.ppc.eligibility.model.EligibilityVerificationResponse;

public interface VerifyEligibilityService {

    public EligibilityVerificationResponse checkEligibility(EligibilityVerificationRequest eligVerifyRequest);

    public void testDb();



    public String testProp();


}
