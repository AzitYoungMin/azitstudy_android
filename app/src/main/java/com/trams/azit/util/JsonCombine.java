package com.trams.azit.util;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015-09-04.
 */
public class JsonCombine {

    public JsonCombine() {
    }

    public JSONObject CombineFromLoginParams(String id, String pw) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
            obj.put("password", pw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
