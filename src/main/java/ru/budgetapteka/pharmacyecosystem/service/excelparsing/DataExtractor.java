package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class DataExtractor {


    private static final Logger log = LoggerFactory.getLogger(DataExtractor.class);

    static LocalDate extractDate(Cell cell) {
        String cellString = cell.getStringCellValue();
        String date = cellString.split("-")[1].strip();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, formatter);
    }

    static Integer extractNumberOfPharmacy(Cell cell) {
        Pattern pattern = Pattern.compile("№\\d+");
        String stringCellValue = cell.getStringCellValue();
        Matcher matcher = pattern.matcher(stringCellValue);
        if (matcher.find()) {
            String pharmacyString = stringCellValue.substring(matcher.start() + 1, matcher.end());
            return Integer.parseInt(pharmacyString);
        }
        return null;
    }

    static List<Integer> extractPharmacyNumbers(Cell cell) {
        Pattern pattern = Pattern.compile("!!.+!!");
        String stringCellValue = cell.getStringCellValue();
        Matcher matcher = pattern.matcher(stringCellValue);
        if (matcher.find()) {
            String[] pharmacies = stringCellValue.substring(matcher.start() + 2, matcher.end() - 2).split(",");
            return Stream.of(pharmacies)
                    .map(String::strip)
                    .map(Integer::parseInt)
                    .filter(n -> {
                        boolean incorrectNumber = n > 15 || n < 0;
                        if (incorrectNumber) log.info("Некорректная аптека: {}", n);
                        return !incorrectNumber;
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }
}
