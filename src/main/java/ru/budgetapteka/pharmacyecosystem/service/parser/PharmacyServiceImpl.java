package ru.budgetapteka.pharmacyecosystem.service.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.repository.PharmacyRepository;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@Service
@Scope("session")
public class PharmacyServiceImpl implements PharmacyService {

    private static final Logger log = LoggerFactory.getLogger(PharmacyServiceImpl.class);

    @Value("${my.vars.photo.storage}")
    private String photoPath;

    private final PharmacyRepository pharmacyRepository;


    public PharmacyServiceImpl(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public List<Pharmacy> getAllPharmacies() {
        log.info("Находим все аптеки в базе");
        List<Pharmacy> allPharmacies = pharmacyRepository.findAll();
        allPharmacies.sort(Comparator.comparing(Pharmacy::getPharmacyNumber));
        return allPharmacies;
    }

    @Override
    public Resource getPhoto(String photoName) {
        Path res = Path.of(photoPath).resolve(photoName).normalize();
        UrlResource urlResource = null;
        try {
            urlResource = new UrlResource(res.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urlResource;

//
//        String dispo = """
//                 attachment; filename="%s"
//                """;
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        String.format(dispo, photoName))
//                .body(urlResource);

    }

    @Override
    public Pharmacy findByNumber(Integer number, List<Pharmacy> allPharmacies) {
        return allPharmacies.stream()
                .filter(ph -> number.equals(ph.getPharmacyNumber()))
                .findFirst().orElseThrow();
    }
}
