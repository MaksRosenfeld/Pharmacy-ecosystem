package ru.budgetapteka.pharmacyecosystem.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ExcelHandler {

    public static Sheet getSheet(String excelFilePath) {
        try (InputStream file = Files.newInputStream(Path.of(excelFilePath))) {
            Workbook workbook = new XSSFWorkbook(file);
            return workbook.getSheetAt(0);
        } catch (IOException e) {
            System.out.println("Problem with file");
            return null;
        }
    }


    public static void main(String[] args) {
        getWorkBook("C:\\JavaProjects\\Pharmacy-ecosystem\\src\\main\\resources\\excel_files\\Выписка_01.01.2022-31.01.2022_1543.xlsx");
    }
}
