package ru.budgetapteka.pharmacyecosystem.service.parser;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.rest.ApiService;

@Data
@Service
public class ParsedDataServiceImpl implements ParsedDataService {

    private final ApiService apiService;
    private ParsedData parsedData;
    private FinancialResults financialResults;

    public ParsedDataServiceImpl(ApiService apiService) {
        this.apiService = apiService;
        this.parsedData = apiService.getParsedData();
    }
}
