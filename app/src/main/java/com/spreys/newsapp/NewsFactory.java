package com.spreys.newsapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vspreys on 9/13/16.
 */

public class NewsFactory {

    public static List<News> GetNewsFromJson(JSONObject json) {
        ArrayList<News> news = new ArrayList<>();

        try {
            JSONObject response = json.getJSONObject("response");
            JSONArray newsArray = response.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {
                news.add(new News((JSONObject)newsArray.get(i)));
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        return news;
    }
}
