package ru.budgetapteka.pharmacyecosystem.parserJsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JsoupParser {

    @Test
    public void parseTesting() throws IOException {
        Document document = Jsoup.connect("http://www.consultant.ru/law/ref/calendar/proizvodstvennye/2022/").get();
        Element may = document.select("table.cal").stream()
                .filter(x -> x.getElementsContainingText("ноябрь").hasText())
                .findFirst().orElseThrow();
        long count = may.getElementsByTag("td").stream()
                .filter(x -> !x.hasClass("weekend"))
                .filter(x -> !x.hasClass("inactively"))
                .count();
        System.out.println(count);


    }

    @Test
    public void testDate() {


        LocalDate date = LocalDate.of(2022, 9, 1);

        System.out.println(date.getMonth().length(date.isLeapYear()));
    }


}
