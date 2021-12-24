package ru.job4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.time.LocalDateTime;
import org.jsoup.nodes.Document.OutputSettings;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

public class PostParser {

    public Post loadPost(String url) throws Exception {

        Document doc = Jsoup.connect(url).get();
        doc.outputSettings(new OutputSettings().prettyPrint(false));

        String title = doc.select(".messageHeader").get(0).text();

        Element desc = doc.select(".msgBody").get(1);
        desc.select("br").after("\\n");
        String str = desc
                .html()
                .replaceAll("\\\\n", "\n")
                .trim();
        String description = Jsoup.clean(str, "", Whitelist.none(), new OutputSettings().prettyPrint(false));

        String date = doc.select(".msgFooter")
                .get(0)
                .text()
                .substring(0, 17);
        LocalDateTime created = new SqlRuDateTimeParser().parse(date);

        return new Post(url, title, description, created);
    }
}
