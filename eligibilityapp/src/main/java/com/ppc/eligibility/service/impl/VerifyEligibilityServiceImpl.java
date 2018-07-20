/**
 *
 */
package com.ppc.eligibility.service.impl;


import com.ppc.eligibility.entity.EligibilityDataEntity;
import com.ppc.eligibility.entity.EligibilityDataEntityID;
import com.ppc.eligibility.entity.EligibilityRecord;
import com.ppc.eligibility.model.CSEAEligibilityResponse;
import com.ppc.eligibility.model.EligibilityVerificationRequest;
import com.ppc.eligibility.model.EligibilityVerificationResponse;
import com.ppc.eligibility.repository.EligibilityDataRepository;
import com.ppc.eligibility.repository.EligibilityLookupRepo;
import com.ppc.eligibility.service.CSEAEligibilityService;
import com.ppc.eligibility.service.VerifyEligibilityService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
@Service
@Qualifier(value = "verifyEligibilityService")
public class VerifyEligibilityServiceImpl implements VerifyEligibilityService {
    private static final Logger LOG = Logger.getLogger(VerifyEligibilityServiceImpl.class);
    private static final String DIRECT_STATUS = "DIRECT BILL";
    @Autowired
    EligibilityLookupRepo eligibilityLookupRepo;
    @Autowired
    EligibilityDataRepository eligibilityDataRepository;

    @Autowired
    private CSEAEligibilityService cseaEligibilityService;


    @Value("${client.csea.wrapper.id}")
    private String wrapperId;

    @Value("${client.csea.ad.id}")
    private String cseaAdId;

    @Value("${client.csea.pd.id}")
    private String cseaPdId;


    @Value("${eligibility.mode}")
    private String testPropFile;


    @Override
    public EligibilityVerificationResponse checkEligibility(EligibilityVerificationRequest eligVerifyRequest) {

        List<String> csea_client_ids = new ArrayList<String>();
        csea_client_ids.add(wrapperId);
        csea_client_ids.add(cseaAdId);
        csea_client_ids.add(cseaPdId);
        LOG.info("/* 1. Inside VerifyEligibilityServiceImpl.checkEligibility(()");
        EligibilityVerificationResponse eligVerifyResponse = new EligibilityVerificationResponse();
        ArrayList<String> koReasonsList = new ArrayList<String>();
        eligVerifyResponse.setKickoutReasons(koReasonsList);

        //if its CSEA customer call CSEA eligibility service.
        if (csea_client_ids.contains(eligVerifyRequest.getHostID())) {
            //check if payrollID is null/empty return RC205.
            if (StringUtils.isEmpty(eligVerifyRequest.getPayrollID())) {
                koReasonsList.add(ReasonCodesEnum.RC205.name());
                eligVerifyResponse.setKickoutReasons(koReasonsList);
                LOG.info("/* 2. null payroll ID recieved, responding with RC205 for Customer ID->" + eligVerifyRequest.getCustomerID());
                return eligVerifyResponse;
            }

            /**
             * ET-3 : For 2404-CSEA NY PD, check if the status is 'DIRECT BILL'. if yes, set kickout reason RC206 (Not eligible) and return.
             */
            LOG.info("/***************************************************************************************");
            LOG.info("/* 3. Check to see if the client id is 2404");
            if (cseaPdId.equalsIgnoreCase(eligVerifyRequest.getHostID())) {
                String tempCustID = eligVerifyRequest.getPayrollID();
                String custID = tempCustID.replaceFirst("^0+(?!$)", "");
                EligibilityDataEntityID entityId = new EligibilityDataEntityID(eligVerifyRequest.getHostID(), custID);
                LOG.info("/* 4. Check to see if the customer already exists in the ELIGIBILITY table");

                EligibilityDataEntity eligDataEntity = eligibilityDataRepository.getEligibilityDataEntry(entityId.getHostid(), entityId.getCustid()).get(0);

                if (eligDataEntity != null) {
                    LOG.info("/* 5. Customer is present in database. Check to see if the status is DIRECT BILL");
                    if (DIRECT_STATUS.equalsIgnoreCase(eligDataEntity.getStatus())) {
                        koReasonsList.add(ReasonCodesEnum.RC206.name());
                        eligVerifyResponse.setKickoutReasons(koReasonsList);
                        LOG.info("/* 6. Customer is ineligible as the customer is a direct bill customer, responding with RC206 for Customer ID->" + eligVerifyRequest.getCustomerID());
                        return eligVerifyResponse;
                    }
                }
            }

            LOG.info("/* 7. Before calling CSEA API.");
            CSEAEligibilityResponse cseaEligResponse = cseaEligibilityService.checkCSEAEligibility(eligVerifyRequest.getPayrollID());

            //if response is null send RC301 Error Code.
            if (cseaEligResponse == null) {
                koReasonsList.add(ReasonCodesEnum.RC301.name());
                eligVerifyResponse.setKickoutReasons(koReasonsList);
                return eligVerifyResponse;
            } else {
                LOG.info("/* 8. eligVerifyResponse-Ineligibility Code ->" + cseaEligResponse.getEligibilityData().getIneligibilityCode());
                LOG.info("/* 9. eligVerifyResponse - Error Code->" + cseaEligResponse.getSystemError().getErrorCode());

            }

            //check the different response Error codes.
            if ("000".equals(cseaEligResponse.getSystemError().getErrorCode())) {//if the request to CSEA API processed successfully.
                if (CSEAResponseCode.IR000.name().equalsIgnoreCase(cseaEligResponse.getEligibilityData().getIneligibilityCode())) {
                    //check first name and last name.
                    /**
                     * HDT-2248 : apply same regex for last name
                     */
                    String cseaLastName = cseaEligResponse.getEligibilityData().getLastName().replaceAll("[^A-Za-z]", "");

                    if (!isNameMatch(eligVerifyRequest.getCustLastName(), cseaLastName)) {
                        koReasonsList.add(ReasonCodesEnum.RC207.name());

                    }

                    //check the tenure
                    String kickOutReasonString = checkTenure(eligVerifyRequest.getClientMinTenure(), eligVerifyRequest.getSelfReportedHireDate());
                    if (kickOutReasonString != null) {
                        koReasonsList.add(kickOutReasonString);
                        eligVerifyResponse.setKickoutReasons(koReasonsList);
                    }
                    LOG.info("/* 10. CSEAResponseCode:IR000, customer is eligible. Before saving the response");
                    //time to log eligibility response data in eligibility table.
                    saveEligibilityRespnseData(eligVerifyRequest, cseaEligResponse, "ACT");
                } else if (CSEAResponseCode.IR004.name().equalsIgnoreCase(cseaEligResponse.getEligibilityData().getIneligibilityCode())) { //not found in CSEA DB.
                    koReasonsList.add(ReasonCodesEnum.RC205.name());

                } else if (CSEAResponseCode.IR003.name().equalsIgnoreCase(cseaEligResponse.getEligibilityData().getIneligibilityCode())) {//not active in CSEA DB. //check it
                    koReasonsList.add(ReasonCodesEnum.RC206.name());
                    LOG.info("/* 10. CSEAResponseCode:IR003, customer is ineligible. Before saving the response");
                    //time to log eligibility response data in eligibility table.
                    saveEligibilityRespnseData(eligVerifyRequest, cseaEligResponse, "INELIGIBLE");
                }
            } else {
                koReasonsList.add(ReasonCodesEnum.RC301.name()); //Error code for system issues in CSEA API. For Error Codes like  001, 002, 004, 005.
                cseaEligResponse.getEligibilityData();
            }
            eligVerifyResponse.setKickoutReasons(koReasonsList);
            eligVerifyResponse.setPayrollId(eligVerifyRequest.getPayrollID());
            return eligVerifyResponse;
        }
        //check for non CSEA customers.
        final EligibilityRecord entry = getEligibilityRecord(eligVerifyRequest.getHostID(), eligVerifyRequest.isPayrollIdReq(),
                eligVerifyRequest.getSsn(), eligVerifyRequest.getPayrollID());


        //LOG.info("Name-->"+entry.getFirstName());
        if (entry == null) {
            koReasonsList.add(ReasonCodesEnum.RC205.name());
            eligVerifyResponse.setKickoutReasons(koReasonsList);
            return eligVerifyResponse;
        }

        // check name
        if (!isNameMatch(eligVerifyRequest.getCustLastName(), entry.getLastName())) {
            koReasonsList.add(ReasonCodesEnum.RC207.name());
            eligVerifyResponse.setKickoutReasons(koReasonsList);
        }

        // Check Status
        if (!"ACT".equalsIgnoreCase(entry.getStatus())) {
            koReasonsList.add(ReasonCodesEnum.RC206.name());
            eligVerifyResponse.setKickoutReasons(koReasonsList);
        }

        // Check File Age
        if (eligVerifyRequest.getEligDataExpirationInDays() != null && eligVerifyRequest.getEligDataExpirationInDays().intValue() > 0) {
            LOG.info("/* File Age Step1 :  Check Initiated  ");
            /**
             * HDT-654 :
             */
            boolean fileAgeThresholdProvided = false;
            int clientFileExpirationInDays = 0;
            if (eligVerifyRequest.getFileAgeThreshold().intValue() > 1) {
                clientFileExpirationInDays = eligVerifyRequest.getEligDataExpirationInDays().intValue() * eligVerifyRequest.getFileAgeThreshold().intValue();
                LOG.info("/* File Age Step2 : File Age Threshold provided and value is " + eligVerifyRequest.getFileAgeThreshold().intValue());
                fileAgeThresholdProvided = true;
            } else {
                clientFileExpirationInDays = eligVerifyRequest.getEligDataExpirationInDays().intValue();
                LOG.info("/* File Age Step2 : No File Age Threshold  " + eligVerifyRequest.getFileAgeThreshold().intValue());
            }

            // Add one day to above so that User is allowed to place an order when Verification date
            // is equal to File age
            clientFileExpirationInDays = clientFileExpirationInDays + 1;

            LOG.info("/* File Age Step3 : Verification Date  " + entry.getVerificationDate() + " and File Age Threshold calculated date : " +
                    (new Date()).after(DateUtils.addDays(entry.getVerificationDate(), clientFileExpirationInDays)));

            final DateTime fileDate = new DateTime(entry.getVerificationDate());
            final Days days = Days.daysBetween(fileDate, new DateTime());

            if (entry.getVerificationDate() == null
                    || (new Date()).after(DateUtils.addDays(entry.getVerificationDate(), clientFileExpirationInDays))) {
                if (fileAgeThresholdProvided) {
                    LOG.info("/* File Age Step4 : Kicked out due 304  as the file age expiration is " + days.getDays());
                    koReasonsList.add(ReasonCodesEnum.RC304.name());
                } else {
                    LOG.info("/* File Age Step4 : Kicked out due 204  as the file age expiration is " + days.getDays());
                    koReasonsList.add(ReasonCodesEnum.RC204.name());
                }
                eligVerifyResponse.setKickoutReasons(koReasonsList);
                eligVerifyResponse.setDaysFileLastVerified(days.getDays());
            }

            //HDT-1590
            if (fileAgeThresholdProvided && entry.getVerificationDate() == null ||
                    (new Date()).before(DateUtils.addDays(entry.getVerificationDate(), clientFileExpirationInDays)) &&
                            (new Date()).after(DateUtils.addDays(entry.getVerificationDate(), eligVerifyRequest.getEligDataExpirationInDays().intValue()))) {

                LOG.info("/* File Age Step4 : Kicked out due 204 as the file age expiration is " + days.getDays());
                koReasonsList.add(ReasonCodesEnum.RC204.name());
                eligVerifyResponse.setKickoutReasons(koReasonsList);
                eligVerifyResponse.setDaysFileLastVerified(days.getDays());
            }
        }


        // Check tenure requirements
        if (StringUtils.isNotBlank(entry.getHireDate()))

        {
            final Date hireDate = parseDate(entry.getHireDate());
            if (hireDate == null
                    || DateUtils.addMonths(hireDate, eligVerifyRequest.getClientMinTenure().intValue()).after(new Date())) {
                final String kickOutReasonString = checkTenure(eligVerifyRequest.getClientMinTenure(), hireDate);
                if (kickOutReasonString != null) {
                    koReasonsList.add(kickOutReasonString);
                    eligVerifyResponse.setKickoutReasons(koReasonsList);
                }
            }
        }

        //Add the values retrieved from file to the response object.
        eligVerifyResponse.setEligibilityFileDate(entry.getVerificationDate());
        eligVerifyResponse.setEmploymentType(entry.getEmploymentType());
        //
        if (StringUtils.isNotEmpty(entry.getHireDate())) {
            eligVerifyResponse.setVerifiedHireDate(parseDate(entry.getHireDate()));
        }
        //
        eligVerifyResponse.setVerifiedPayDate(entry.getVerificationDate());
        eligVerifyResponse.setVerifiedSalary(entry.getSalary() != null ? Double.valueOf(entry.getSalary()) : 0.0);
        eligVerifyResponse.setVerifiedSalaryDate(new Date());
        eligVerifyResponse.setPayFrequency(entry.getPayFrequency());
        eligVerifyResponse.setPayrollId(entry.getPayrollId());

        return eligVerifyResponse;
    }

    /**
     * ET-3 :Test method for ET-3.
     */
    @Override
    public void testDb() {
        LOG.info("/* 1. inside testDb method");
        EligibilityDataEntityID entityId = new EligibilityDataEntityID("2404", "845673435");


        EligibilityDataEntity eligEntity = eligibilityDataRepository.getEligibilityDataEntry(entityId.getHostid(), entityId.getCustid()).get(0);


        if (eligEntity == null) {
            eligEntity = new EligibilityDataEntity();
            eligEntity.setId(entityId);
            eligEntity.setAgency("AGENCY");
            eligEntity.setPayrollID("845673435");
            eligEntity.setFirstName("David");
            eligEntity.setLastName("Beckham");
            eligEntity.setMiddleName("M");
            eligEntity.setLocal("LOCAL");
            eligEntity.setCreationDate(new Date());
            eligEntity.setModificationDate(new Date());
            eligEntity.setVerificationDate(new Date());
            //eligEntity.setSno(new BigDecimal("883838383888"));
            eligEntity.setCreateUser("Eligibility Service");
            //eligEntity.setPayFrequency(cseaEligResponse.getEligibilityData().getPayFrequency());
            eligEntity.setUid("davidbeckham@csea.com");
            eligEntity.setStatus("ACT");
            eligibilityDataRepository.save(eligEntity);
            //  eligDataEntryDao.saveEligibilityDataEntry(eligEntity);
        } else {
            eligEntity.setFirstName("DAVID");
            eligEntity.setLastName("BECKHAM");
            eligEntity.setModificationDate(new Date());
            eligEntity.setVerificationDate(new Date());
            eligEntity.setAgency("agency");
            eligEntity.setPayrollID("845673435");
            eligEntity.setLocal("local");

            eligEntity.setCreateUser("Eligibility Service");
            eligEntity.setStatus("INELIGIBLE");
            eligibilityDataRepository.save(eligEntity);
            // eligDataEntryDao.updateEligibilityDataEntry(eligEntity);
        }
        LOG.info("/* 2. After data is updated.");
        //return eligDataEntryDao.getEligibilityDataEntry(entityId);
    }


    public String testProp() {

        return testPropFile;
    }


    public EligibilityRecord getEligibilityRecord(String hostId, final boolean payrollIdReq, String ssn, String payrollId) {

        String eligibilityId;
        if (payrollIdReq) {
            eligibilityId = payrollId;
        } else {
            eligibilityId = ssn;
        }
        if (eligibilityId != null) {
            eligibilityId = eligibilityId.replaceFirst("^0+(?!$)", "");
        } else {
            eligibilityId = "";
        }
        eligibilityId = eligibilityId.replaceFirst("^0+(?!$)", "");

        BigDecimal hostIdBig = new BigDecimal(hostId);

        return eligibilityLookupRepo.getEligibilityRecord(eligibilityId, hostIdBig);

        //return eligibilityLookupDAO.getEligibilityRecord(hostId, payrollIdReq, ssn, payrollId);
    }

    // Check minimum tenure. If failure, will return kickout reason. If passed, will return null
    public String checkTenure(final Integer minTenure, final Date hireDate) {
        if (minTenure != null && minTenure.intValue() > 0) {
            if (hireDate == null
                    || DateUtils.addMonths(hireDate, minTenure.intValue()).after(new Date())) {

                return ReasonCodesEnum.RC201.name();

            }
        }
        return null;
    }

    private boolean isNameMatch(final String CustProfileLastName, final String eligibilityRecordLastName) {

        // The check for a name match should only compare the first letter of firstname and
        // the first four of the last name.

        //trim last name for leading or trailing spaces.
        final String profileLastName = StringUtils.trim(CustProfileLastName);

        final String lastNameFirstFourCharacters = StringUtils.substring(profileLastName, 0, 4);


        final String entryLastNameFirstFourCharacters = StringUtils.substring(eligibilityRecordLastName, 0, 4);

        if (lastNameFirstFourCharacters.equalsIgnoreCase(entryLastNameFirstFourCharacters)) {
            return true;
        }
        return false;
    }


    public Date parseDate(final String strDate) {

        final String[] parsePatterns = {"yyyyMMdd", "MMddyyyy", "dd-MMM-yy", "MM/dd/yyyy"};

        try {
            return DateUtils.parseDate(strDate, parsePatterns);


        } catch (final ParseException e) {

            LOG.error("Error parsing the date " + strDate);
        }
        return null;
    }


    /**
     * ET-3 :Primary key constraint is no more present on Eligibility table. Updated saveEligibilityResponseData() to 1st find the record and then update if present, or insert if not present.
     * This also resolves creation date and modification dates being the same.
     */

    private void saveEligibilityRespnseData(EligibilityVerificationRequest VerifyRequest, CSEAEligibilityResponse cseaEligResponse, String status) {

        LOG.info("/***************************************************************************************");
        LOG.info("/* 11. Inside saveEligibilityRespnseData() method");
        String tempCustID = VerifyRequest.getPayrollID();
        String custID = tempCustID.replaceFirst("^0+(?!$)", "");
        EligibilityDataEntityID entityId = new EligibilityDataEntityID(VerifyRequest.getHostID(), custID);
        LOG.info("/* 12. Check to see if the customer already exists in the ELIGIBILITY table");
        EligibilityDataEntity eligEntity = eligibilityDataRepository.getEligibilityDataEntry(entityId.getHostid(), entityId.getCustid()).get(0);
        // EligibilityDataEntity eligEntity = eligDataEntryDao.getEligibilityDataEntry(entityId);

        if (eligEntity != null) {
            LOG.info("/* 13. The customer is already present in database, so update the record");
            eligEntity.setAgency(cseaEligResponse.getEligibilityData().getAgency());
            eligEntity.setPayrollID(VerifyRequest.getPayrollID());
            eligEntity.setFirstName(cseaEligResponse.getEligibilityData().getFirstName());
            eligEntity.setLastName(cseaEligResponse.getEligibilityData().getLastName());
            eligEntity.setMiddleName(StringUtils.isNotEmpty(cseaEligResponse.getEligibilityData().getMiddleName()) ? StringUtils.upperCase(cseaEligResponse.getEligibilityData().getMiddleName().substring(0, 1)) : null);
            eligEntity.setLocal(cseaEligResponse.getEligibilityData().getLocal());
            eligEntity.setModificationDate(new Date());
            eligEntity.setVerificationDate(new Date());
            eligEntity.setCreateUser("Eligibility Service");
            eligEntity.setPayFrequency(cseaEligResponse.getEligibilityData().getPayFrequency());
            eligEntity.setUid(VerifyRequest.getUid());
            eligEntity.setStatus(status);
            eligibilityDataRepository.save(eligEntity);
            // eligDataEntryDao.updateEligibilityDataEntry(eligEntity);
            LOG.info("/* 14. Record updated for customer: " + VerifyRequest.getCustomerID());
        } else {
            LOG.info("/* 13. The customer is not present in database, so create a new record");
            eligEntity = new EligibilityDataEntity();
            eligEntity.setId(entityId);
            eligEntity.setAgency(cseaEligResponse.getEligibilityData().getAgency());
            eligEntity.setPayrollID(VerifyRequest.getPayrollID());
            eligEntity.setFirstName(cseaEligResponse.getEligibilityData().getFirstName());
            eligEntity.setLastName(cseaEligResponse.getEligibilityData().getLastName());
            eligEntity.setMiddleName(StringUtils.isNotEmpty(cseaEligResponse.getEligibilityData().getMiddleName()) ? StringUtils.upperCase(cseaEligResponse.getEligibilityData().getMiddleName().substring(0, 1)) : null);
            eligEntity.setLocal(cseaEligResponse.getEligibilityData().getLocal());
            eligEntity.setCreationDate(new Date());
            eligEntity.setModificationDate(new Date());
            eligEntity.setVerificationDate(new Date());
            //eligEntity.setSno(new BigDecimal("883838383888"));
            eligEntity.setCreateUser("Eligibility Service");
            eligEntity.setPayFrequency(cseaEligResponse.getEligibilityData().getPayFrequency());
            eligEntity.setUid(VerifyRequest.getUid());
            eligEntity.setStatus(status);
            eligibilityDataRepository.save(eligEntity);
            //eligDataEntryDao.saveEligibilityDataEntry(eligEntity);
            LOG.info("/* 14. Record created for customer: " + VerifyRequest.getCustomerID());
        }


    }


    public static enum ReasonCodesEnum {

        RC201,
        RC204,
        RC205,
        RC206,
        RC207,
        RC301,
        RC304

    }

    public static enum CSEAResponseCode {

        IR000,
        IR003,
        IR004

    }
}
