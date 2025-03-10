package Homework.H3W3Database.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${info.app.name}")
    private String appName;

    @GetMapping("/port")
    public ResponseEntity<String> getServerPort() {
        return ResponseEntity.ok("The application '" + appName + "' is running on port: " + serverPort);
    }
}

//java -jar target/H3W3Database-0.0.1-SNAPSHOT.jar  - запуск основной конфигурации
//java -jar target/H3W3Database-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev - запуск с профилем dev
//java -jar target/H3W3Database-0.0.1-SNAPSHOT.jar --spring.profiles.active=test - - запуск с профилем test