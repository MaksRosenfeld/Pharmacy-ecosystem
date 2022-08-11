package ru.budgetapteka.pharmacyecosystem.service.parser;

import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ParsingService {

    public List<RawAbstract> parse(AbstractJson abstractJson);


}
