package ru.budgetapteka.pharmacyecosystem.service.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class WorkingDaysParser {

    private static final String url = "https://www.consultant.ru/law/ref/calendar/proizvodstvennye/%s";

    private WorkingDaysParser() {}

    public static long getWorkingDays(int year, String month) {
        String urlWithYear = String.format(url, year);
        try {
            Document document = Jsoup.connect(urlWithYear).get();
            Element element = document.select("table.cal").stream()
                    .filter(x -> x.getElementsContainingText(month).hasText())
                    .findFirst().orElseThrow();
            return element.getElementsByTag("td").stream()
                    .filter(x -> !x.hasClass("weekend"))
                    .filter(x -> !x.hasClass("inactively"))
                    .count();
        } catch (IOException e) {
            throw new RuntimeException("Невозможно подключиться к сайту для парсинга рабочих дней");
        }

    }

}
