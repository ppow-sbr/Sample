package com.ppc.eligibility.controller;

import com.ppc.eligibility.model.EligibilityVerificationRequest;
import com.ppc.eligibility.model.EligibilityVerificationResponse;
import com.ppc.eligibility.service.CSEAEligibilityService;
import com.ppc.eligibility.service.EligibilityAuthService;
import com.ppc.eligibility.service.VerifyEligibilityService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * Created by Yelleshc on 3/22/2018.
 */

@Controller
@RequestMapping(value = "eligibility")
public class PPCEligibilityServiceController {

    public static final Logger logger = Logger.getLogger(PPCEligibilityServiceController.class);
    private static final String AUTH_TOKEN = "token";
    private static final String AUTH_TIMESTAMP = "authTimestamp";

    @Autowired
    private VerifyEligibilityService verifyEligibilityService;

    @Autowired
    private EligibilityAuthService authenticationService;

    @Autowired
    private CSEAEligibilityService cseaEligibilityService;

    @RequestMapping(method = RequestMethod.GET, value = "/myTestEligibilityService", produces = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<String> getExecutedTests()
            throws Exception {
        String response = null;

        try {
            logger.info("@@@Inside myTestEligibilityService()");
            response = "Hello There, This is a test response from eligibility service";
            System.out.print(response);
            logger.info("/myTestEligibilityService");
        } catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(response, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/testCSEAEligibilityService", produces = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<String> testCSEAEligibilityService()
            throws Exception {
        String response = null;

        try {

            logger.info("@@@Inside testCSEAEligibilityService()");
            cseaEligibilityService.testdb();

            response = "This is a test response from CSEA eligibility service";

            response=verifyEligibilityService.testProp();
            //System.out.print(response);
        } catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(response, HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.POST, value = "/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<EligibilityVerificationResponse> verifyEligibility(@RequestHeader("Authorization") String Authorization, @RequestHeader("token")
            String token, @RequestHeader("authTimestamp") String authTimestamp, @RequestBody EligibilityVerificationRequest eligRequest)
            throws Exception {
        EligibilityVerificationResponse verificationResponse = null;

        try {
            if (authenticationService.userAuthenticated(Authorization, token, authTimestamp)) {//User authorized.
                logger.info("User authorized");

                verificationResponse = verifyEligibilityService.checkEligibility(eligRequest);


                if (verificationResponse != null && verificationResponse.getKickoutReasons() != null && verificationResponse.getKickoutReasons().size() > 0) {

                    logger.info(verificationResponse.getKickoutReasons().get(0));
                }

            } else {
                verificationResponse = new EligibilityVerificationResponse();
                ArrayList<String> koReasonsList = new ArrayList<String>();
                verificationResponse.setResponseCode("401-Unauthorized");
                logger.info("Reason Code -" + "401-Unauthorized");
                koReasonsList.add("RC301");
                verificationResponse.setKickoutReasons(koReasonsList);

            }
        } catch (Exception e) {
            return new ResponseEntity<EligibilityVerificationResponse>(verificationResponse, HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<EligibilityVerificationResponse>(verificationResponse, HttpStatus.OK);

    }


}
