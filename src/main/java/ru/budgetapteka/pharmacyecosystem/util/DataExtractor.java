package ru.budgetapteka.pharmacyecosystem.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DataExtractor {


    private static final Logger log = LoggerFactory.getLogger(DataExtractor.class);

    public static LocalDate convertToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    public static Date convertToDateFromYearMonth(String monthYear) {
        YearMonth ym = YearMonth.parse(monthYear, DateTimeFormatter.ofPattern("MM-yyyy"));
        LocalDate localDate = ym.atDay(1);
        return Date.valueOf(localDate);

    }


    public static List<Integer> extractPharmacyNumbers(String purpose) {
        Pattern pattern = Pattern.compile("!!.+!!");
        Matcher matcher = pattern.matcher(purpose);
        if (matcher.find()) {
            String[] pharmacies = purpose.substring(matcher.start() + 2, matcher.end() - 2).split(",");
            return Stream.of(pharmacies)
                    .map(String::strip)
                    .map(Integer::parseInt)
                    .filter(n -> {
                        boolean incorrectNumber = n > 16 || n < 0;
                        if (incorrectNumber) log.info("Некорректная аптека: {}", n);
                        return !incorrectNumber;
                    })
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static String extractMonth(String date) {
        YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("MM-yyyy"));
        LocalDate localDate = yearMonth.atDay(1);
        return localDate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
    }

    public static int extractYear(String date) {
        YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("MM-yyyy"));
        LocalDate localDate = yearMonth.atDay(1);
        return localDate.getYear();
    }
}
