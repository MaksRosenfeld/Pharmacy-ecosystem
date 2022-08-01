package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import org.springframework.core.io.Resource;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;

import java.util.List;

public interface PharmacyService {
    List<Pharmacy> getAllPharmacies();
    Resource getPhoto(String photoName);
    Pharmacy findById(int id);
}
