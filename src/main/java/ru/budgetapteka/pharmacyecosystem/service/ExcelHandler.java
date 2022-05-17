package ru.budgetapteka.pharmacyecosystem.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.budgetapteka.pharmacyecosystem.exceptions.WrongInnException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelHandler {

    private final String filePath;
    private final int debetColumn = 7; // № столбца с суммой расходов
    private final int costStart = 13; // строка начала расходов в листе
    private final int innColumn = 4; // № столбца с ИНН
    private final int descriptionColumn = 9; // № столбца с описанием

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
                        && row.getCell(this.debetColumn).getCellType() == CellType.NUMERIC) {
                    String inn = row.getCell(this.innColumn).getStringCellValue();
                    double amount = row.getCell(this.debetColumn).getNumericCellValue();
                    String description = row.getCell(this.descriptionColumn).getStringCellValue();
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
        ExcelHandler excelHandler = new ExcelHandler("C:\\JavaProjects\\Pharmacy-ecosystem\\src\\main\\resources\\excel_files\\Выписка_01.01.2022-31.01.2022_1543.xlsx");
        double total = 0;
        for (Cost cost : excelHandler.getAllCosts()) {
            total += cost.getAmount();
        }
        System.out.println(Math.round(total));

    }
}
