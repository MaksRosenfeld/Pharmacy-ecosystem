package ru.budgetapteka.pharmacyecosystem.service.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class DataExtractor {


    private static final Logger log = LoggerFactory.getLogger(DataExtractor.class);

    static LocalDate convertToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    static List<Integer> extractPharmacyNumbers(String purpose) {
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
}
