package ru.job4j.gc;

public class User {
    private String login;
    private String password;
    private int age;
    private boolean allowed;

    public User() {
    }

    public User(String login, String password, int age, boolean allowed) {
        this.login = login;
        this.password = password;
        this.age = age;
        this.allowed = allowed;
    }

    @Override
    public void finalize() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "User{"
                + "login='" + login + '\''
                + ", password='" + password + '\''
                + ", age=" + age
                + ", allowed=" + allowed
                + '}';
    }
}

class Job {
}
