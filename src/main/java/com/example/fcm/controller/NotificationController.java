package com.example.fcm.controller;

import com.example.fcm.model.Token;
import com.example.fcm.repo.TokenRepo;
import com.example.fcm.service.AndroidPushNotificationsService;
import com.example.fcm.service.AndroidPushPeriodicNotifications;
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

    @PostMapping("/update/agree")
    @ResponseBody
    public ResponseEntity updateAgree(@RequestBody JSONObject reqJson) {
        System.out.println(reqJson.toString());
        Token model = tokenRepo.findByToken(reqJson.get("token").toString());
        if (model == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        model.setAgree(reqJson.get("agree").toString());
        tokenRepo.save(model);

        return new ResponseEntity(HttpStatus.OK);
    }

    @Scheduled(fixedRate = 10000)
    @PostMapping("/notification")
    @ResponseBody
    public ResponseEntity<String> notificationAll(@RequestBody JSONObject reqJson) {
        String title = reqJson.get("title").toString();
        String body = reqJson.get("body").toString();

        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(tokenRepo.findByAgree("true"), title, body);

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

    @PostMapping("/send/token")
    @ResponseBody
    public ResponseEntity sendToken(@RequestBody JSONObject reqJson) {
        System.out.println(reqJson.toString());
        String token = reqJson.get("token").toString();
        String type = reqJson.get("type").toString();
        String agree = reqJson.get("agree").toString();

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

