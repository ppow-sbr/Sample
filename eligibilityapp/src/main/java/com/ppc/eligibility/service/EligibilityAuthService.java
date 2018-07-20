package com.ppc.eligibility.service;

/**
 * Created by Yelleshc on 3/22/2018.
 */
public interface EligibilityAuthService {

    boolean userAuthenticated(String authHeader, String token, String timestamp);
}
