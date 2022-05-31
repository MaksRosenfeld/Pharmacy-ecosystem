package ru.budgetapteka.pharmacyecosystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.repository.ContragentRepository;

import java.util.List;

@Service
public class ContragentServiceImpl implements ContragentService{

    @Autowired
    public ContragentRepository contragentRepository;


    @Override
    public List<ContragentNew> getAllContragents() {
        return contragentRepository.findAll();
    }

    @Override
    public boolean isExistingContragent(Cost cost) {
        return getAllContragents().stream()
                .anyMatch(c -> c.getInn().equals(cost.getInn()));
    }

    @Override
    public void saveNewContragent(ContragentNew contragent) {
        contragentRepository.save(contragent);
    }
}
