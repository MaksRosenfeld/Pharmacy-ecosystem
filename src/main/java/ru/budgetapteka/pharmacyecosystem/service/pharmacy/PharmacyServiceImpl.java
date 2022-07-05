package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.repository.PharmacyRepository;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    private static final Logger log = LoggerFactory.getLogger(PharmacyServiceImpl.class);

    @Value("${PHOTO_STORAGE}")
    private String photoPath;

    private final PharmacyRepository pharmacyRepository;

    public PharmacyServiceImpl(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public List<Pharmacy> getAll() {
        List<Pharmacy> all = pharmacyRepository.findAll();
        all.sort(Comparator.comparing(Pharmacy::getPharmacyNumber));
        return all;
    }

    @Override
    public Pharmacy getPharmacy(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<Resource> getPhoto(String photoName) {
        Path res = Path.of(photoPath).resolve(photoName).normalize();
        UrlResource urlResource = null;
        try {
            urlResource = new UrlResource(res.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String dispo = """
                 attachment; filename="%s"
                """;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format(dispo, photoName))
                .body(urlResource);

    }
}
