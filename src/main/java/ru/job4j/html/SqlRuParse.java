package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.util.List;
import java.util.stream.Collectors;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.getElementsByTag("tbody").get(2).children();
        List<Element> rowList = row.stream().skip(1).collect(Collectors.toList());
        for (Element tr : rowList) {
            Elements post = tr.select(".postslisttopic");
            for (Element td : post) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
            }
            System.out.println(new SqlRuDateTimeParser().parse(tr.child(5).text()));
        }
    }
}
