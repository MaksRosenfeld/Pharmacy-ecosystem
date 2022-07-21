package ru.budgetapteka.pharmacyecosystem.service.parser;

import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

@Data
public class ExcelFileBankStatement implements Parseable {

    private static final Logger log = LoggerFactory.getLogger(ExcelFileBankStatement.class);

//    Поля данных
    private final int COST_START = 13; // строка начала расходов в листе
    private final int DEBIT_COLUMN = 7; // № столбца с суммой расходов
    private final int NAME_COLUMN = 3; // № столбца с названием фирмы
    private final int INN_COLUMN = 4; // № столбца с ИНН
    private final int DESCRIPTION_COLUMN = 9; // № столбца с описанием

    private Workbook workbook;

    public ExcelFileBankStatement(InputStream streamForWorkbook) {
        log.info("Загружаем выписку из банка");
        try {
            this.workbook = new XSSFWorkbook(streamForWorkbook);
        } catch (IOException e) {
            log.error("Невозможно загрузить выписку из банка");
            e.printStackTrace();
        }
    }

    @Override
    public String getData() {
        return null;
    }
}
