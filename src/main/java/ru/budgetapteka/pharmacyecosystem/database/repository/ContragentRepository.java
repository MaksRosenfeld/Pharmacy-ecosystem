package ru.budgetapteka.pharmacyecosystem.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.budgetapteka.pharmacyecosystem.database.entity.Contragent;

import java.util.Optional;

@Repository
public interface ContragentRepository extends JpaRepository<Contragent, Long> {
    Optional<Contragent> findByInn(Long inn);
}
