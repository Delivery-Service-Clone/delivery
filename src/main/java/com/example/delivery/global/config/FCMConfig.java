package com.example.delivery.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
public class FCMConfig {

  @Bean
  public FirebaseApp firebaseApp() throws IOException {
    FileInputStream aboutFirebaseFile = new FileInputStream(
        ResourceUtils.getFile("delivery-service-key.json"));

    FirebaseOptions options = FirebaseOptions
        .builder()
        .setCredentials(GoogleCredentials.fromStream(aboutFirebaseFile))
        .build();
    return FirebaseApp.initializeApp(options);
  }

  @Bean
  public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
    return FirebaseMessaging.getInstance(firebaseApp);
  }
}
