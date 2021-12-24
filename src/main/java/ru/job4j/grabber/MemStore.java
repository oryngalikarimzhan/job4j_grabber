package ru.job4j.grabber;

import java.util.ArrayList;
import java.util.List;

public class MemStore implements Store {
    private List<Post> posts = new ArrayList<>();

    @Override
    public void save(Post post) {
        posts.add(post);
    }

    @Override
    public List<Post> getAll() {
        return posts;
    }

    @Override
    public Post findById(int id) {
        return indexOf(id) == id ? posts.get(id) : null;
    }

    private int indexOf(int id) {
        int rsl = -1;
        for (int index = 0; index < posts.size(); index++) {
            if (posts.get(index).getId() == id) {
                rsl = index;
                break;
            }
        }
        return rsl;
    }
}
