package com.revive.datascraper.controller;

import com.revive.datascraper.models.MainModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
    private ArrayList<File> filesArrayList = new ArrayList<>();
    private ArrayList<String> messagesArrayList = new ArrayList<>();

    @GetMapping("/")
    public String showForm() {
        return "index";
    }

    @PostMapping("/submit")
    public String submitForm(@RequestParam String textFieldValue) {
        System.out.println("Text from textField: " + textFieldValue);
        return "index";
    }



    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestPart("files") MultipartFile[] files, @RequestPart("message") String message) throws IOException, InvalidFormatException {
        List<String> fileNames = Arrays.stream(files)
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.toList());

        System.out.println(message);
//        fileNames.forEach(System.out::println);

        messagesArrayList.add(message);

        for (MultipartFile file : files) {
            try {
                File convertedFile = convertMultipartFileToFile(file);
                filesArrayList.add(convertedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MainModel mainModel = new MainModel();
        File finalFile = mainModel.calculate(messagesArrayList, filesArrayList);

        return new ResponseEntity<>("Files uploaded successfully", HttpStatus.OK);
    }



    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }

    @PostMapping("/uploadMultipart")
    @ResponseBody
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        // Process the file (e.g., save it to a database or perform some other operation)
        // For simplicity, let's just print the file name here.
        System.out.println("Received file: " + file.getOriginalFilename());

        // You can return a success message or any other response as needed.
        return ResponseEntity.ok("File uploaded successfully");
    }

//    @GetMapping("/downloadFile")
//    public ResponseEntity<ByteArrayResource> downloadFile() throws IOException, InvalidFormatException {
//
////        byte[] fileContent = new byte[(int) finalFile.length()];
////
////        ByteArrayResource resource = new ByteArrayResource(fileContent);
////
////        HttpHeaders headers = new HttpHeaders();
////        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filename.xlsx");
////
////        return ResponseEntity.ok()
////                .headers(headers)
////                .contentLength(fileContent.length)
////                .contentType(MediaType.APPLICATION_OCTET_STREAM)
////                .body(resource);
//    }

}
