package com.ppc.eligibility.service;


import com.ppc.eligibility.model.CSEAEligibilityResponse;

public interface CSEAEligibilityService {

    CSEAEligibilityResponse checkCSEAEligibility(String uid);

    void testdb();

}
