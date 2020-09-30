package com.hezhiheng.networkpractice;

import com.hezhiheng.networkpractice.domain.PersonList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    @GET("fake-data/data/default.json")
    Call<PersonList> getUsers();
}
