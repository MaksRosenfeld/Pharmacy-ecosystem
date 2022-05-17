package ru.budgetapteka.pharmacyecosystem.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.budgetapteka.pharmacyecosystem.exceptions.WrongInnException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ExcelHandler {

    private final String filePath;
    private final int debetStroke = 7;
    private final int costStart = 13;
    private final int innStroke = 4;
    private final int descriptionStroke = 9;

    public ExcelHandler(String filePath) {
        this.filePath = filePath;
    }

    private Sheet getSheet() throws IOException {
        try (FileInputStream file = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(file);
            return workbook.getSheetAt(0);
        } catch (IOException e) {
            System.out.println("Problem with file");
            throw new IOException();
        }
    }

    public List<Cost> getAllCosts() {
        List<Cost> costList = new ArrayList<>();
        Sheet excelPage;
        try {
            excelPage = getSheet();
            for (Row row : excelPage) {
                if (row.getRowNum() >= this.costStart // расходы начинаются стнадартно с 13 ряда
                        && row.getCell(this.debetStroke).getCellType() == CellType.NUMERIC) {
                    String inn = row.getCell(this.innStroke).getStringCellValue();
                    double amount = row.getCell(this.debetStroke).getNumericCellValue();
                    String description = row.getCell(this.descriptionStroke).getStringCellValue();
                    try {
                        Cost cost = new Cost(inn, amount, description);
                        costList.add(cost);
                    } catch (WrongInnException e) {}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return costList;
    }

        public static void main(String[] args) {
        ExcelHandler excelHandler = new ExcelHandler("C:\\JavaProjects\\Pharmacy-ecosystem\\src\\main\\resources\\excel_files\\Выписка_01.12.2021-31.12.2021_1543.xlsx");
        for (Cost cost : excelHandler.getAllCosts()) {
            System.out.println(cost);
        }

    }
}
