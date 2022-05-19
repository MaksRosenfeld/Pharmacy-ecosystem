package ru.budgetapteka.pharmacyecosystem.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.exceptions.WrongInnException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class ExcelHandler {

    private InputStream file;

//    @Value("C:\\JavaProjects\\Pharmacy-ecosystem\\src\\main\\resources\\excel_files\\Выписка_01.01.2022-31.01.2022_1543.xlsx")
    private String filePath;
    private final int debetColumn = 7; // № столбца с суммой расходов
    private final int costStart = 13; // строка начала расходов в листе
    private final int innColumn = 4; // № столбца с ИНН
    private final int descriptionColumn = 9; // № столбца с описанием

    private List<Cost> missingInn = new ArrayList<>();


    @Autowired
    private ContragentServiceImpl contragentService;



    private Sheet getSheet() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(this.file)) {
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
                if (row.getRowNum() >= this.costStart // расходы начинаются стандартно с 13 ряда
                        && row.getCell(this.debetColumn).getCellType() == CellType.NUMERIC) {
                    String inn = row.getCell(this.innColumn).getStringCellValue();
                    double amount = row.getCell(this.debetColumn).getNumericCellValue();
                    String description = row.getCell(this.descriptionColumn).getStringCellValue();
                    try {
                        Cost cost = new Cost(inn, amount, description);
                        System.out.println(cost.getInn());
                        costList.add(cost);
                        if (!contragentService.isExistingContragent(cost)) {
                            System.out.println(cost.getInn());
                            missingInn.add(cost);
                        }
                    } catch (WrongInnException e) {

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return costList;
    }

    public List<Cost> getMissingInn() {
        return missingInn;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }


}
