package ru.budgetapteka.pharmacyecosystem.service.parsing;

import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;

import java.util.List;

public interface ParsingService {

    public List<RawAbstract> parse(AbstractJson abstractJson);


}
