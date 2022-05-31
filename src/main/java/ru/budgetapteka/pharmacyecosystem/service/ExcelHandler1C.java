package ru.budgetapteka.pharmacyecosystem.service;

import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.PatternDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Data
public class ExcelHandler1C {

    private InputStream file;
    private BigDecimal totalTurnOver; // выручка
    private BigDecimal totalGrossProfit; // валовая прибыль
    private List<Pharmacy> pharmaciesData; // список аптек с данными по отчету
    private LocalDate dateOf1CStatement; // дата выписки


    private final int rowStart = 5; // строка начала выручек у аптек
    private final int costPriceColumn = 4; // № столбца с себестоимостью продукции
    private final int turnOverColumn = 6; // № столбца с выручкой аптеки за месяц
    private final int grossProfitColumn = 7; // № столбца с ИНН
    private final int nameColumn = 0; // № столбца с описанием

    public void getDataFrom1CExcel() {
        this.pharmaciesData = new ArrayList<>();
        this.totalTurnOver = BigDecimal.ZERO;
        this.totalGrossProfit = BigDecimal.ZERO;
        Sheet excel1CFile;
        try {
            excel1CFile = getSheet();
            String stringCellValue = excel1CFile.getRow(3).getCell(2).getStringCellValue();
            this.dateOf1CStatement = getDateOfStatement(stringCellValue);
            for (Row row : excel1CFile) {
                if (row.getRowNum() > rowStart) {
                    String pharmacyText = row.getCell(nameColumn).getStringCellValue();
                    BigDecimal costPrice = BigDecimal.valueOf(row.getCell(costPriceColumn).getNumericCellValue());
                    BigDecimal turnOver = BigDecimal.valueOf(row.getCell(turnOverColumn).getNumericCellValue());
                    BigDecimal grossProfit = BigDecimal.valueOf(row.getCell(grossProfitColumn).getNumericCellValue());
                    Integer id = getPharmacyNumber(pharmacyText);
                    if (id != null) {
                        Pharmacy pharmacy = new Pharmacy(id, turnOver, grossProfit, costPrice);
                        this.totalTurnOver = this.totalTurnOver.add(turnOver);
                        this.totalGrossProfit = this.totalGrossProfit.add(grossProfit);
                        pharmaciesData.add(pharmacy);

                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Sheet getSheet() throws IOException {
        try (Workbook workbook = new HSSFWorkbook(file)) {
            return workbook.getSheetAt(0);
        } catch (IOException e) {
            System.out.println("Problem with file");
            throw new IOException();
        }
    }

    private Integer getPharmacyNumber(String text) {
        Pattern pattern = Pattern.compile("№\\d+");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String pharmacyString = text.substring(matcher.start() + 1, matcher.end());
            return Integer.parseInt(pharmacyString);
        }
        return null;
    }
    private LocalDate getDateOfStatement(String cellWithDate) {
        String date = cellWithDate.split("-")[1].strip();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, formatter);
    }


}
