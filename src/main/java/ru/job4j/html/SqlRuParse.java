package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".forumTable").get(0).children().get(0).children();
        for (Element tr : row) {
            Elements parseLink = tr.select(".postslisttopic");
            for (Element td : parseLink) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
            }
            System.out.println(tr.child(5).text());
        }
    }
}
