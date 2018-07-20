package com.ppc.eligibility.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppc.eligibility.entity.EligibilityServiceLogEntity;
import com.ppc.eligibility.model.CSEAEligibilityResponse;
import com.ppc.eligibility.repository.EligibilityLookupRepo;
import com.ppc.eligibility.repository.EligibilityServiceLogRepository;
import com.ppc.eligibility.service.CSEAEligibilityService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;


@Service
@Qualifier(value = "cseaEligibilityService")
public class CSEAEligibilityServiceImpl implements CSEAEligibilityService {

    private static final Logger LOG = Logger.getLogger(CSEAEligibilityServiceImpl.class);
    private static final String PPC_STRING = "Purchasing Power";
    private static final String HMACSHA256 = "HmacSHA256";
    private static final String CSEA_CLIENT = "CSEANY";
    private static final String SPLIT_STRING = "\\{\"EligibilityResponse\":";
    private static final String UNWANTED_STRING = "} } }";
    private static final String DESIRED_STRING = "} }";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("MMddyyyyHHmmss");
    @Autowired
    EligibilityLookupRepo eligibilityLookupRepo;
    @Autowired
    EligibilityServiceLogRepository eligibilityServiceLogRepository;
    /*@PersistenceContext
    private EntityManager em;*/


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${eligibility.service.readtimeout}")
    private int readtimeout;
    @Value("${eligibility.service.connecttimeout}")
    private int connecttimeout;
    @Value("${eligibility.csea.url}")
    private String cseaUrl;

    @Value("${eligibility.mode}")
    private String eligibilityMode;

    @Value("${eligibility.csea.secretKey}")
    private String cseaSHAKey;

    @Override
    public CSEAEligibilityResponse checkCSEAEligibility(String uID) {


        LOG.info("#### About to call CSEA service for Customer-" + uID);

        CSEAEligibilityResponse eligibilityResponse = null;

        try {
            eligibilityResponse = getCSEAEligibility(uID);
        } catch (ClientHandlerException che) {
            if (che.getCause() instanceof SocketTimeoutException) {

                LOG.error("##Timeout Exception while accessing CSEA service, trying one more time##");
                try {
                    eligibilityResponse = getCSEAEligibility(uID);
                } catch (Exception exp2) {
                    LOG.error("##Error while accessing CSEA service##" + exp2);
                }
            } else {
                LOG.error("##Error while accessing CSEA service##" + che);
            }
        } catch (Exception exp1) {
            LOG.error("##Error while accessing CSEA service##" + exp1);
        }


        return eligibilityResponse;
    }

    @Override
    public void testdb() {

        BigDecimal tablesCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*)From tab", BigDecimal.class);

        System.out.print(tablesCount);



    }


    private CSEAEligibilityResponse getCSEAEligibility(String uID) throws Exception {

        CSEAEligibilityResponse eligibilityResponse = null;

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        client.setReadTimeout(Integer.valueOf(readtimeout));
        client.setConnectTimeout(Integer.valueOf(connecttimeout));
        WebResource webResource = client.resource(UriBuilder.fromUri(cseaUrl).build());
        MultivaluedMap<String, String> formData = new MultivaluedMapImpl();

        String token = String.valueOf(getUniqueToken());


        LOG.info("#### value of Token-" + token);
        String currentDate = DATE_FORMATTER.print(new Date().getTime());

        String encodedHashString = encodedString(token, currentDate);

        LOG.info("#### value of Token-" + token);
        formData.add("ClientID", CSEA_CLIENT);
        formData.add("UID", uID);
        formData.add("token", token);
        formData.add("timestamp", currentDate);
        if ("test".equalsIgnoreCase(eligibilityMode)) {
            formData.add("mode", "test");
        }
        formData.add("APIUser", PPC_STRING);

        ClientResponse response = webResource.header(HttpHeaders.AUTHORIZATION, encodedHashString).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);

        String result = response.getEntity(String.class);
        String[] JsonData = result.split(SPLIT_STRING);
        String JsonString = JsonData[1].replace(UNWANTED_STRING, DESIRED_STRING);

        ObjectMapper objectMapper = new ObjectMapper();

        eligibilityResponse = objectMapper.readValue(JsonString, CSEAEligibilityResponse.class);


        saveEligibilityLogObject(eligibilityResponse, result, token, currentDate, uID);

        return eligibilityResponse;
    }

    private String encodedString(String token, String timestamp) {
        String encodedString = null;
        try {

            String message = PPC_STRING + ":" + token + ":" + timestamp;

            Mac sha256_HMAC = Mac.getInstance(HMACSHA256);
            // String cseaSHAKey = ConfigPropertyValues.getPropertyValue("eligibility.csea.secretKey");
            SecretKeySpec secret_key = new SecretKeySpec(cseaSHAKey.getBytes(),
                    HMACSHA256);
            sha256_HMAC.init(secret_key);

            encodedString = new String(Base64.encodeBase64(sha256_HMAC
                    .doFinal(message.getBytes())), StandardCharsets.UTF_8);
        } catch (Exception exp) {
            LOG.error("Error while preparing digest hash.", exp);
        }
        return encodedString;

    }


    private void saveEligibilityLogObject(CSEAEligibilityResponse cseaResponse,
                                          String result, String token, String timestamp, String uID) {

        EligibilityServiceLogEntity logEntity = new EligibilityServiceLogEntity();
        logEntity.setId(generateUUID());
        logEntity.setClientID(cseaResponse.getEligibilityData().getClientID());
        logEntity.setCreateDate(new Date());
        logEntity.setCreatedBy("Eligibility Service");
        logEntity.setCustomerFirstName(cseaResponse.getEligibilityData()
                .getFirstName());
        logEntity.setCustomerLastName(cseaResponse.getEligibilityData()
                .getLastName());
        logEntity.setIneligibilityReasonCode(cseaResponse.getEligibilityData()
                .getIneligibilityCode());
        logEntity.setRawResponse(result);
        logEntity.setSystemErrorCode(cseaResponse.getSystemError()
                .getErrorCode());
        logEntity.setTimestamp(timestamp);
        logEntity.setToken(token);
        logEntity.setuID(uID);
        logEntity.setCreatedBy("Eligibility Service");
        logEntity.setUpdateDate(new Date());
        logEntity.setUpdatedBy("Eligibility Service");
        eligibilityServiceLogRepository.save(logEntity);

    }

    private String generateUUID() {
        // Generate transaction id
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }

    public long getUniqueToken() {
        return jdbcTemplate.queryForObject(
                "select CSEA_UNIQUE_TOKEN.nextval as num from dual", Long.class);


    }

}
