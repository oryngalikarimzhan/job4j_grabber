package ru.job4j;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document.OutputSettings;

public class PostParser {
    public static void main(String[] args) throws Exception {
        String url =
                "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";
        List<Element> description = new ArrayList<>();
        List<Element> dateTime = new ArrayList<>();

        Document doc = Jsoup.connect(url).get();
        doc.outputSettings(new OutputSettings().prettyPrint(false));

        Elements row1 = doc.select(".msgBody");
        row1.select("br").after("\\n");
        row1.stream().skip(1).limit(1).forEach(description::add);
        String str = description.get(0).html().replaceAll("\\\\n", "\n").trim();
        System.out.println(Jsoup.clean(str, "", Whitelist.none(), new OutputSettings().prettyPrint(false)));

        Elements row2 = doc.select(".msgFooter");
        row2.stream().limit(1).forEach(dateTime::add);
        System.out.println(dateTime.get(0).text().substring(0, 17));
    }
}
