package ru.budgetapteka.pharmacyecosystem.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataExtractorTest {

    @Test
    public void checkMonthLocale() {
        String month = DataExtractor.extractMonth("12-2022");
        assertEquals("декабрь", month);
    }

}