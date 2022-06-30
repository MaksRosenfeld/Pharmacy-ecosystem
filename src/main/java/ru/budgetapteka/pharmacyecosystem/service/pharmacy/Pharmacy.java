//package ru.budgetapteka.pharmacyecosystem.service.pharmacy;
//
//import lombok.Data;
//import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
//import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Cost;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//
//@Data
//public class Pharmacy {
//
//    private Integer id;
//    private BigDecimal turnOver;
//    private BigDecimal grossProfit;
//    private BigDecimal costPrice;
//    private Map<CategoryNew, BigDecimal> costSum; // Название расхода по категории - сумма
//
//    public Pharmacy(Integer id) {
//        this.id = id;
//    }
//
//
//    @Override
//    public String toString() {
//        return "Pharmacy{" +
//                "id=" + id +
//                ", turnOverForMonth=" + turnOver +
//                ", grossProfit=" + grossProfit +
//                ", costPrice=" + costPrice +
//                '}';
//    }
//}
