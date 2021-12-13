package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        String url = "https://www.sql.ru/forum/job-offers/";
        List<Element> rowList = new ArrayList<>();
        Document doc;
        Elements row;
        for (int i = 1; i != 6; i++) {
            url = url + i;
            doc = Jsoup.connect(url).get();
            row = doc.select(".postslisttopic");
            row.stream().skip(4).forEach(rowList::add);
            url = url.substring(0, url.length() - 1);
        }
        for (Element td : rowList) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
        }
    }

}
