package com.revive.datascraper.controller;

import com.revive.datascraper.models.MainModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;


import static com.revive.datascraper.models.MainModel.clearFolder;

@Controller
public class MainController {
    private ArrayList<String> messagesArrayList = new ArrayList<>();
    private ArrayList<File> uploadedFiles = new ArrayList<>();
    private File finalFile;

    @Value("${file.download.path}")
    private String downloadPath;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/")
    public String showForm() {
        return "index";
    }


    @PostMapping("/upload")
    public ResponseEntity<String>  handleFileUpload(@RequestPart("files") MultipartFile[] files, @RequestPart("message") String message) {
        messagesArrayList.add(message);

        for (MultipartFile file : files) {
            try {
                String path = downloadPath + file.getOriginalFilename();
                File fileTarget = new File(path);

                try (OutputStream os = new FileOutputStream(fileTarget)) {
                    os.write(file.getBytes());
                }

                uploadedFiles.add(fileTarget);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

        String successMessage = messageSource.getMessage("upload.success", null, "Default Success Message", null);

        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

    @RequestMapping(path = "/calculate", method = RequestMethod.GET)
    public ResponseEntity<String> calculate() throws IOException, InvalidFormatException {


        MainModel mainModel = new MainModel();
        finalFile = mainModel.calculate(messagesArrayList, uploadedFiles);

        System.out.println("Data is calculated");

        String successMessage = messageSource.getMessage("calculate.success", null, "Default Success Message", null);


        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }


    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download() throws IOException {


        InputStreamResource resource = new InputStreamResource(new FileInputStream(finalFile));

        clearFolder(new File(downloadPath));

        return ResponseEntity.ok()
                .contentLength(finalFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }













}
