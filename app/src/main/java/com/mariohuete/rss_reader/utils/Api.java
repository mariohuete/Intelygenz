package com.mariohuete.rss_reader.utils;

import com.mariohuete.rss_reader.models.Model;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;


/**
 * Created by mariobama on 09/02/15.
 */
public interface Api {
    @GET("/feeds/flowers.json")
    public void getList(Callback<List<Model>> response);
}