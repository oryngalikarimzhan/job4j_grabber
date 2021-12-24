package ru.job4j.grabber.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) throws Exception {
        List<Post> rsl = new ArrayList<>();
        for (int i = 1; i != 5; i++) {
            String path = String.format(link + "%s", i);
            Document doc = Jsoup.connect(path).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                String title = href.text();
                if (title.contains("java") && !title.contains("javascript")) {
                    String postUrl = href.attr("href");
                    rsl.add(detail(postUrl));
                }
            }
        }
        return rsl;
    }

    @Override
    public Post detail(String link) throws Exception {
        Document doc = Jsoup.connect(link).get();
        doc.outputSettings(new Document.OutputSettings().prettyPrint(false));

        String title = doc.select(".messageHeader").get(0).text();

        Element desc = doc.select(".msgBody").get(1);
        desc.select("br").after("\\n");
        String str = desc
                .html()
                .replaceAll("\\\\n", "\n")
                .trim();
        String description = Jsoup.clean(str, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));

        String date = doc.select(".msgFooter")
                .get(0)
                .text()
                .substring(0, 17);
        LocalDateTime created = dateTimeParser.parse(date);

        return new Post(link, title, description, created);
    }
}
