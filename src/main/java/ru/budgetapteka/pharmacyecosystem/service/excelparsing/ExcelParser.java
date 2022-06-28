package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Parseable;

public interface ExcelParser {
    void parse1CStatement(MultipartFile file);
    void parseBankStatement(MultipartFile file);
    ParsedResults getParsedResults();
}
