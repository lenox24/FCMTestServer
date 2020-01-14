package com.example.fcm.service;

import com.example.fcm.model.Token;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.List;


public class AndroidPushPeriodicNotifications {
    public static String PeriodicNotificationJson(List<Token> tokenList) {
        LocalDate localDate = LocalDate.now();
        JSONArray array = new JSONArray();

        for (Token token : tokenList) {
            array.put(token.getToken());
        }

        JSONObject body = new JSONObject();
        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        notification.put("title", "hello");
        notification.put("body", "Today is " + localDate.getDayOfWeek().name());

        body.put("notification", notification);

        System.out.printf(body.toString());

        return body.toString();
    }
}
