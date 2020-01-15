package com.example.fcm.controller;

import com.example.fcm.model.Token;
import com.example.fcm.repo.TokenRepo;
import com.example.fcm.service.AndroidPushNotificationsService;
import com.example.fcm.service.AndroidPushPeriodicNotifications;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@RestController
@RequestMapping("api/a")
public class NotificationController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TokenRepo tokenRepo;

    final AndroidPushNotificationsService androidPushNotificationsService;

    public NotificationController(TokenRepo tokenRepo, AndroidPushNotificationsService androidPushNotificationsService) {
        this.tokenRepo = tokenRepo;
        this.androidPushNotificationsService = androidPushNotificationsService;
    }

    @Scheduled(fixedRate = 10000)
    @PostMapping("/notification")
    @ResponseBody
    public ResponseEntity<String> notificationAll(@RequestBody JSONObject reqJson) {
        Boolean agree = (Boolean) reqJson.get("agree");
        String title = reqJson.get("title").toString();
        String body = reqJson.get("body").toString();

        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(tokenRepo.findByAgree(agree), title, body);

        HttpEntity<String> request = new HttpEntity<>(notifications);

        return sending(request);
    }

    @Scheduled(fixedRate = 10000)
    @PostMapping("/notification/type")
    @ResponseBody
    public ResponseEntity<String> notificationType(@RequestBody JSONObject reqJson) {
        String type = reqJson.get("type").toString();
        String title = reqJson.get("title").toString();
        String body = reqJson.get("body").toString();

        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(tokenRepo.findByType(type), title, body);

        HttpEntity<String> request = new HttpEntity<>(notifications);

        return sending(request);
    }

    @PostMapping("/token")
    @ResponseBody
    public ResponseEntity token(@RequestBody JSONObject reqJson) {
        String token = reqJson.get("token").toString();
        String type = reqJson.get("type").toString();
        Boolean agree = (Boolean) reqJson.get("agree");

        Token model = new Token();
        model.setToken(token, type, agree);
        tokenRepo.insert(model);

        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity<String> sending(HttpEntity request) {
        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            logger.debug("got interrupted!");

            e.printStackTrace();
        } catch (ExecutionException e) {
            logger.debug("execution error!");

            e.printStackTrace();
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}

