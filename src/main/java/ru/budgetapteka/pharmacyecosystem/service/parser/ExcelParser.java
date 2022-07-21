package ru.budgetapteka.pharmacyecosystem.service.parser;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelParser {
    void parse1CStatement(MultipartFile file);
    void parseBankStatement(MultipartFile file);
    ParsedResults getParsedResults();
}
