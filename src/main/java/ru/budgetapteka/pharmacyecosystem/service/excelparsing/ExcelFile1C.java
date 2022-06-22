package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.InputStream;


@Data
public class ExcelFile1C implements Parseable {

    private static final Logger log = LoggerFactory.getLogger(ExcelFile1C.class);

    private Workbook workbook;

    //    Поля данных
    private final int START_ROW = 6; // строка начала выручек у аптек
    private final int COST_PRICE_COLUMN = 4; // № столбца с себестоимостью продукции
    private final int TURN_OVER_COLUMN = 6; // № столбца с выручкой аптеки за месяц
    private final int GROSS_PROFIT_COLUMN = 7; // № столбца с ИНН
    private final int NAME_COLUMN = 0; // № столбца с описанием

    //    Поля даты
    private final int DATE_ROW = 3; // ряд даты
    private final int DATE_COLUMN = 2; // колонка даты

    public ExcelFile1C(InputStream streamForWorkBook) {
        log.info("Загружаем выписку из 1С");
        try {
            this.workbook = new HSSFWorkbook(streamForWorkBook);
        } catch (IOException e) {
//            TODO: придумать обработку на странице
            log.error("Невозможно загрузить выписку 1С");
            e.printStackTrace();
        }

    }
}
