package com.hezhiheng.networkpractice.domain;

import java.util.List;

public class PersonList {
    private List<Person> data;

    public PersonList() {
    }

    public PersonList(List<Person> data) {
        this.data = data;
    }

    public List<Person> getPersons() {
        return data;
    }
}
