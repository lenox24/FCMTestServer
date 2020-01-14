package com.example.fcm.controller;

import com.example.fcm.model.Token;
import com.example.fcm.repo.TokenRepo;
import com.example.fcm.service.AndroidPushNotificationsService;
import com.example.fcm.service.AndroidPushPeriodicNotifications;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/send")
    @ResponseBody
    public ResponseEntity<String> send(@RequestBody String type) {
        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(tokenRepo.findByType(type));

        HttpEntity<String> request = new HttpEntity<>(notifications);

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

    @PostMapping("/token")
    @ResponseBody
    public ResponseEntity token(@RequestBody JSONObject reqJson) {
        String token = reqJson.get("token").toString();
        String type = reqJson.get("type").toString();
        Token model = new Token();
        model.setToken(token, type);
        tokenRepo.insert(model);

        return new ResponseEntity(HttpStatus.OK);
    }
}

