package com.example.fcm.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;


public class AndroidPushPeriodicNotifications {
    public static String PeriodicNotificationJson() {
        LocalDate localDate = LocalDate.now();
        String[] tokenList = {"d2B1A-1kbhw:APA91bHTQh8vtHH7SGycDspUnjTpoQdf9AwwhHIPkVhHUNM0o6wro9grZJrz0HtBswRhSHuGbhzGMd0CQsEVN2xiNcevYpFaWQQ3A0ycTrmfKCur94j0RalypCxbblCV8v47D2Vkxq6L",
                "eHYFA7QRl_o:APA91bFKvExlUVp4Euwxt-cLrJSOmmE6YEBvJdU4kXyVXpEYd4BqTJu2eUaKrbPAnxPRSKKUEjGojrwMvXbfumzUWNJ6lca44mVgsmN5XWuy1zstFw3KOh5TNs5fpK5zcUdEsnOAvy4h",
                "d12q29r-Q_M:APA91bE10d-7fZ-f6PokMQZMBWgaHqtcTCX3_TmtZsNQkW12-3NG4lwBjURAer7Iv-XkJq0vXGs2wJVt0kmIBXZmu6MrptDGBtFUHJcq3sA7JXHAYKQjQDSbcD2YGl4tdyQAP981ezbE",
                "dOjuS1Zxmio:APA91bGityEx0FXfOGER8thku2Oahl8HDda9EieTudFTuOi-xONIokUTmyJ0TbmG8fhwolqmCG-S1h3AcbA7NPJyld29dlRYWORbv7gdPJ0Frq1TJG3-2q7UqeD52WQ7H6682ZCvkMWZ",
                "c8ty-cJUKPk:APA91bHiHs99xoJc4K3iE8TC2cKAMdm-lQ5N6vf44oLfUqIiUz4d7BSJgUITVdWLl-f3iIIQsve35ku8t-AKAUvac8XxvNKAFCzNYe_KgVLZUGGJC0_irMs34oLVlA95jOkxhSDgE1E0",
                "dLgSW9AKBo0:APA91bFQ4eJfWpuTEoKZkB4A3vqCGarz-I3bARwcgRinNCOPXJGJvgCeg_WlL4K_961Jqqv-rpNvsMchgkuj1yCCmTg7Ca5KRgHUCW38jx0MNrl4JmNiJ43nYDFUVZXhZi7ahPeyDZNA"};
        JSONArray array = new JSONArray();

        for (String token : tokenList) {
            array.put(token);
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
