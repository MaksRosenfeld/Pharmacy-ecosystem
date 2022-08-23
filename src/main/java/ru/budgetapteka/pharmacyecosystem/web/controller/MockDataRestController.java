package ru.budgetapteka.pharmacyecosystem.web.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.budgetapteka.pharmacyecosystem.database.entity.CostCategory;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("mock/api")
public class MockDataRestController {

    @PostMapping("/order_bank_statements")
    public ResponseEntity<?> orderMockStatements() {
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @GetMapping("/all_costs")
    public List<PharmacyCost> showAllCosts() {
        Pharmacy mockPh = new Pharmacy();
        mockPh.setPharmacyNumber(7);
        return List.of(
                PharmacyCost.builder().inn(12342L).name("ДистриФарм")
                        .categoryId(CostCategory.builder().category("Аренда").build())
                        .amount(new BigDecimal("23421"))
                        .pharmacy(mockPh).build(),
                PharmacyCost.builder().inn(98765L).name("Барбарис")
                        .categoryId(CostCategory.builder().category("Банк").build())
                        .amount(new BigDecimal("43212"))
                        .pharmacy(mockPh).build());
    }
}
