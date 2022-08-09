package ru.budgetapteka.pharmacyecosystem.service.parser;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.rest.Status;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;

import java.util.Set;

@Data
@Service
public class ParsingServiceImpl implements ParsingService {

    private ParsedData parsedData;


    public void parseStatements(Set<AbstractJson> jsonFiles) {
        this.parsedData = new ParsedData();
        Parser parser = new ParserImpl(parsedData);
        jsonFiles.forEach(parser::parse);
        parsedData.setStatus(Status.PARSED);
    }



}
