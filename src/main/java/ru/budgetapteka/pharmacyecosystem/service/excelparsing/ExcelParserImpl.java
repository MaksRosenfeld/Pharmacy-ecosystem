package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyCostService;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyResultService;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Getter
@Service
public class ExcelParserImpl implements ExcelParser {

    private static final Logger log = getLogger(ExcelParserImpl.class);
    private final PharmacyService pharmacyService;
    private final PharmacyResultService pharmacyResultService;
    private final ContragentService contragentService;
    private final PharmacyCostService costService;

    private List<Row> rowsWithTypos;
    private final ParsedResults parsedResults;

    private List<Pharmacy> allPharmacies;
    private List<ContragentNew> allContragents;

    public ExcelParserImpl(ParsedResults keepResultsIn,
                           PharmacyService pharmacyService,
                           PharmacyResultService pharmacyResultService,
                           ContragentService contragentService,
                           PharmacyCostService costService) {
        this.parsedResults = keepResultsIn;
        this.pharmacyService = pharmacyService;
        this.pharmacyResultService = pharmacyResultService;
        this.contragentService = contragentService;
        this.costService = costService;
        log.info("Парсер создан");
    }

    @Override
    public void parse1CStatement(MultipartFile file) {
        try {
            ExcelFile1C file1C = new ExcelFile1C(file.getInputStream());
            Sheet sheet = file1C.getWorkbook().getSheetAt(0);
            this.allPharmacies = pharmacyService.getAll();
            Cell cellWithDate = sheet.getRow(file1C.getDATE_ROW()).getCell(file1C.getDATE_COLUMN());
            parsedResults.setDate(DataExtractor.extractDate(cellWithDate)); // дата выписки
            for (Row row : sheet) {
                // исключаем ненужные ряды
                if (row.getRowNum() >= file1C.getSTART_ROW()
                        && row.getRowNum() < sheet.getLastRowNum()) {
                    parseDataForEachPharmacy_1C(file1C, row, this.allPharmacies);
                } else if (row.getRowNum() == sheet.getLastRowNum()) {
                    parseLastRow_1C(file1C, row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseBankStatement(MultipartFile file) {
        ExcelFileBankStatement bankStatement;
        this.rowsWithTypos = new ArrayList<>();
        this.allContragents = contragentService.getAllContragents();
        try {
            bankStatement = new ExcelFileBankStatement(file.getInputStream());
            Sheet sheet = bankStatement.getWorkbook().getSheetAt(0);
            List<Cost> costs = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() >= bankStatement.getCOST_START() // расходы начинаются стандартно с 13 ряда
                        && row.getCell(bankStatement.getDEBIT_COLUMN()).getCellType() == CellType.NUMERIC) {
                    Cost cost = parseForCosts_BS(bankStatement, row);
                    costs.add(cost);
                }
            }
            parsedResults.setCosts(costs);
            log.info("Количество расходов: {}", parsedResults.getCosts().size());
//       Опечатки сохраняем только в случае, если они были
            if (rowsWithTypos.size() > 0) {
                Map<Workbook, List<Row>> mapWithTypos = new HashMap<>();
                mapWithTypos.put(bankStatement.getWorkbook(), rowsWithTypos);
                parsedResults.setCellsWithTypos(mapWithTypos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //    Собираем данные из 1С выписки по аптекам
    private void parseDataForEachPharmacy_1C(ExcelFile1C oneC, Row row, List<Pharmacy> pharmacies) {
        Cell cellWithName = row.getCell(oneC.getNAME_COLUMN());
        Integer id = DataExtractor.extractNumberOfPharmacy(cellWithName); // Забираем номер аптеки
        log.info("Парсим данные для аптеки номер {}", id);
        Pharmacy pharmacy = pharmacies.stream().filter(p -> p.getPharmacyNumber().equals(id)).findFirst().orElseThrow();
        BigDecimal turnOver = BigDecimal.valueOf(row.getCell(oneC.getTURN_OVER_COLUMN()).getNumericCellValue());
        BigDecimal grossProfit = BigDecimal.valueOf(row.getCell(oneC.getGROSS_PROFIT_COLUMN()).getNumericCellValue());
        BigDecimal costPrice = BigDecimal.valueOf(row.getCell(oneC.getCOST_PRICE_COLUMN()).getNumericCellValue());
        PharmacyResult pharmacyResult = pharmacyResultService.createPharmacyResult(pharmacy, parsedResults.getDate(), turnOver, grossProfit, costPrice);
        parsedResults.savePharmacyResult(pharmacyResult);

    }

    // Парсим последний ряд для общих данных (выручка, валовая прибыль, себестоимость)
    private void parseLastRow_1C(ExcelFile1C oneC, Row lastRow) {
        log.info("Парсим последний ряд из 1С выписки");
        BigDecimal totalTurnOver = BigDecimal.valueOf(lastRow.getCell(oneC.getTURN_OVER_COLUMN()).getNumericCellValue());
        parsedResults.setTotalTurnOver(totalTurnOver);
        BigDecimal totalGrossProfit = BigDecimal.valueOf(lastRow.getCell(oneC.getGROSS_PROFIT_COLUMN()).getNumericCellValue());
        parsedResults.setTotalGrossProfit(totalGrossProfit);
        BigDecimal totalCostPrice = BigDecimal.valueOf(lastRow.getCell(oneC.getCOST_PRICE_COLUMN()).getNumericCellValue());
        parsedResults.setTotalCostPrice(totalCostPrice);
    }

    private Cost parseForCosts_BS(ExcelFileBankStatement bs, Row row) {
        String inn = row.getCell(bs.getINN_COLUMN()).getStringCellValue();
        String name = row.getCell(bs.getNAME_COLUMN()).getStringCellValue();
        BigDecimal amount = BigDecimal.valueOf(row.getCell(bs.getDEBIT_COLUMN()).getNumericCellValue());
        Cell description = row.getCell(bs.getDESCRIPTION_COLUMN());
        Cost cost = null;
        if (!inn.isBlank() && !amount.toString().isBlank()) {
            cost = new Cost(Long.parseLong(inn));
            cost.setName(name);
            cost.setAmount(amount);
            List<Integer> belongingCosts = DataExtractor.extractPharmacyNumbers(description);
            cost.setBelongingCosts(belongingCosts);
            cost.distributeToPharmacies(this.allPharmacies,
                    this.allContragents,
                    this.parsedResults,
                    this.costService);
        } else {
            log.error("Ошибка в банковской выписке в ряде: {}", row.getRowNum());
            rowsWithTypos.add(row);
        }
        return cost;

    }
}
