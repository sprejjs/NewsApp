package com.spreys.newsapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vspreys on 9/13/16.
 */

public class News {
    private String title;
    private String type;
    private String url;

    public News(JSONObject json) {
        try {
            this.title = json.getString("webTitle");
            this.url = json.getString("webUrl");
            String type = json.getString("type");

            //Capitalise the first letter of the type
            this.type = type.substring(0, 1).toUpperCase() + type.substring(1);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }

    public String getType() {
        return this.type;
    }
}
