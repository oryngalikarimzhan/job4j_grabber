package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    private int id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime created;

    public Post(String title, String link, String description, LocalDateTime created) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    public Post(int id, String title, String link, String description, LocalDateTime created) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
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
        return id == post.id && Objects.equals(title, post.title) && Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, link);
    }

    @Override
    public String toString() {
        String ln = System.lineSeparator();
        return "Post{" + ln
                + "___________________id_________________" + ln + id + ln
                + "__________________title_______________" + ln + title + ln
                + "__________________link________________" + ln + link + ln
                + "________________description___________" + ln + description + ln
                + "_________________created______________" + ln + created + ln
                + '}';
    }

}
