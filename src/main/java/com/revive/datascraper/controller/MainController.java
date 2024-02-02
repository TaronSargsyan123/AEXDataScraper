package com.revive.datascraper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @GetMapping("/")
    public String showForm() {
        return "index";
    }

    @PostMapping("/submit")
    public String submitForm(@RequestParam String textFieldValue) {
        System.out.println("Text from textField: " + textFieldValue);
        return "index";
    }

    @PostMapping("/uploadfile")
    public ResponseEntity<String> handleFileUpload(@RequestPart("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            System.out.println("Принят файл: " + fileName);
            return new ResponseEntity<>("Файл успешно принят: " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Ошибка при обработке файла", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestPart("files") MultipartFile[] files, @RequestPart("message") String message) {
        List<String> fileNames = Arrays.stream(files)
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.toList());
        System.out.println(message);
        fileNames.forEach(System.out::println);

        return new ResponseEntity<>("Файлы успешно загружены", HttpStatus.OK);
    }
}
