package ru.budgetapteka.pharmacyecosystem.util;

public class Util {

    private Util() {}

    public static class Url {
        public static final String BANK_BASE_URL = "https://bp.open.ru/webapi-2.1/";
        public static final String BANK_POST_STATEMENT_REQUEST = "/accounts/59388/statement?format=JSON&from={from}&to={to}";
        public static final String BANK_GET_STATEMENT_REQUEST = "/accounts/59388/statement/{statementId}/print?print=false";
        public static final String BANK_GET_CHECK_STATEMENT_REQUEST = "/accounts/59388/statement/{statementId}";
        public static final String ONE_C_BASE_URL = "http://192.168.4.7/ut-test/ru_RU/hs/TorgAPI/V1/";
        public static final String ONE_C_GET_DATA_REQUEST = "/GetProfitReport?begin={from}&end={to}";
    }


    public static class Path {

        public static final String BANK_STATEMENT_ID = "/data/statementId";
        public static final String BANK_AMOUNT = "/documentAmount/amount";
        public static final String BANK_CONTRAGENT_NAME = "/beneficiaryInfo/name";
        public static final String BANK_CONTRAGENT_INN = "/beneficiaryInfo/inn";
        public static final String BANK_PAYMENT_PURPOSE = "/paymentPurpose";
        public static final String BANK_START_OF_OPERATIONS = "/account/sendTransactionsToWARequest/paymentDocumentList";
        public static final String ONE_C_TURN_OVER = "/Выручка";
        public static final String ONE_C_GROSS_PROFIT = "/Прибыль";
        public static final String ONE_C_COST_PRICE = "/Себестоимость";
        public static final String MAN_PHOTO_PATH = "src/main/resources/photos/man.png";



    }

    public static class PhInfo {

        public static final int pharmacyNumber = 16;
        public static final Long BUDGET_PHARMACY_INN = 3907029575L;
        public static final Integer OFFICE_NUMBER = 0;
    }



}
