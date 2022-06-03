package ru.budgetapteka.pharmacyecosystem.service.excelservice;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataExtractor {

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

    static List<Pharmacy> extractPharmacyNumbers(Cell cell) {
        Pattern pattern = Pattern.compile("!!.+!!");
        String stringCellValue = cell.getStringCellValue();
        Matcher matcher = pattern.matcher(stringCellValue);
        if (matcher.find()) {
            String[] pharmacies = stringCellValue.substring(matcher.start() + 2, matcher.end() - 2).split(",");
            return Stream.of(pharmacies)
                    .map(String::strip)
                    .map(p -> new Pharmacy(Long.parseLong(p)))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
