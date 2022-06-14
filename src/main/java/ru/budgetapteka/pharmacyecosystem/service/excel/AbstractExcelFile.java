package ru.budgetapteka.pharmacyecosystem.service.excel;

import lombok.Data;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;

@Data
public abstract class AbstractExcelFile {

    private Workbook workbook;


    // получаем первый лист Excel файла
    Sheet createSheetForParsing() {
        return this.workbook.getSheetAt(0);
    }

    public abstract void parse(InputStream inputStream);

}
