package com.hezhiheng.networkpractice.domain;

public class Person {
    private String name;
    private String avatar;

    public Person() {
    }

    public Person(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
