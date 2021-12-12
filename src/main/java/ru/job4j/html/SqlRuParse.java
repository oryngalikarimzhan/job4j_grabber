package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        String url = "https://www.sql.ru/forum/job-offers";
        List<Element> rowList = new ArrayList<>();
        Document doc;
        Elements row;
        int linesToSkip = 1;
        for (int i = 1; i != 6; i++) {
            if (i > 1) {
                linesToSkip = 4;
            }
            url = url + "/" + i;
            doc = Jsoup.connect(url).get();
            row = doc.getElementsByTag("tbody").get(2).children();
            row.stream().skip(linesToSkip).forEach(rowList::add);
            url = url.substring(0, url.length() - 2);
        }

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
