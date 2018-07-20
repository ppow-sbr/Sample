package com.ppc.eligibility.service.impl;

import com.ppc.eligibility.entity.EligibilityTokenKeeper;
import com.ppc.eligibility.repository.EligibilityTokenKeeperRepository;
import com.ppc.eligibility.service.EligibilityAuthService;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Qualifier(value = "eligiblityAuthService")
public class EligiblityAuthServiceImpl implements EligibilityAuthService {


    private static final Logger LOG = Logger.getLogger(EligiblityAuthServiceImpl.class);
    private static final String PPC_STRING = "Purchasing Power";
    private static final String HMACSHA256 = "HmacSHA256";


    @Autowired
    EligibilityTokenKeeperRepository eligibilityTokenKeeperRepository;


    @Value("${eligibility.internal.secretKey}")
    private String shaKey;


    @Override
    public boolean userAuthenticated(String authHeader, String token, String timestamp) {
        boolean authenticated = false;
        //check if token was already used.
        List<EligibilityTokenKeeper> tokenKeeper = eligibilityTokenKeeperRepository.getEligibilityTokenKeeper(token);

        if (tokenKeeper.size() == 0) {

            String encodedString = encodedString(token, timestamp);
            authenticated = authHeader.equals(encodedString);
            EligibilityTokenKeeper tokenObj = new EligibilityTokenKeeper();
            tokenObj.setTokenNumber(token);
            eligibilityTokenKeeperRepository.save(tokenObj);

            //eligibilityLookupDAO.saveEligHashStringToken(token);
        } else {
            LOG.error("##Token already Used->" + token);
        }

        return authenticated;
    }

    private String encodedString(String token, String timestamp) {
        String encodedString = null;
        try {

            String message = PPC_STRING + ":" + token + ":" + timestamp;
            //String shaKey = ConfigPropertyValues.getPropertyValue("eligibility.internal.secretKey");

            Mac sha256_HMAC = Mac.getInstance(HMACSHA256);
            SecretKeySpec secret_key = new SecretKeySpec(shaKey.getBytes(), HMACSHA256);
            sha256_HMAC.init(secret_key);

            encodedString = new String(Base64.encodeBase64(sha256_HMAC.doFinal(message.getBytes())), StandardCharsets.UTF_8);
        } catch (Exception exp) {
            LOG.error("Error while preparing digest hash.", exp);
        }
        return encodedString;

    }

}
