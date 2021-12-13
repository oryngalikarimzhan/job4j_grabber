package ru.job4j.grabber.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.Post;
import ru.job4j.grabber.utils.PostParser;

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
        Document doc = Jsoup.connect(link).get();
        int max = Integer.parseInt(
                doc.getElementsByTag("tbody")
                        .get(3)
                        .child(0)
                        .child(0)
                        .child(10)
                        .text()
        );
        for (int i = 1; i != max; i++) {
            String path = String.format(link + "%s", i);
            doc = Jsoup.connect(path).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                String postUrl = td.child(0).attr("href");
                rsl.add(detail(postUrl));
            }
        }
        return rsl;
    }

    @Override
    public Post detail(String link) throws Exception {
        return new PostParser().loadPost(link);
    }
}
