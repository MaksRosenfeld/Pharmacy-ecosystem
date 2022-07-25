package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;

import java.util.List;

public interface PharmacyService {
    List<Pharmacy> getAll();
    Resource getPhoto(String photoName);
    Pharmacy findById(int id);
}
