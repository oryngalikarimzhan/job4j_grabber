package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    private int id;
    private String name;
    private String link;
    private String text;
    private LocalDateTime created;

    public Post(String name, String link, String text, LocalDateTime created) {
        this.name = name;
        this.link = link;
        this.text = text;
        this.created = created;
    }

    public Post(int id, String name, String text, String link, LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.text = text;
        this.created = created;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id && Objects.equals(name, post.name) && Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, link);
    }

    @Override
    public String toString() {
        String ln = System.lineSeparator();
        return "Post{" + ln
                + "___________________id_________________" + ln + id + ln
                + "__________________name________________" + ln + name + ln
                + "__________________link________________" + ln + link + ln
                + "__________________text________________" + ln + text + ln
                + "_________________created______________" + ln + created + ln
                + '}';
    }

}
