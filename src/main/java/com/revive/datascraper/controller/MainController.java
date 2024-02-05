package com.revive.datascraper.controller;

import com.revive.datascraper.models.MainModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class MainController {
    private ArrayList<String> messagesArrayList = new ArrayList<>();
    private ArrayList<File> uploadedFiles = new ArrayList<>();
    private File finalFile;

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
    public ResponseEntity<String>  handleFileUpload(@RequestPart("files") MultipartFile[] files, @RequestPart("message") String message) throws IOException, InvalidFormatException {

        clearFolder("src/main/resources/tmp");

        List<String> fileNames = Arrays.stream(files)
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.toList());


        messagesArrayList.add(message);
        int i = 0;
        for (MultipartFile file : files) {
            try {
                String path = "src/main/resources/tmp/" + file.getOriginalFilename();
                File fileTarget = new File(path);

                try (OutputStream os = new FileOutputStream(fileTarget)) {
                    os.write(file.getBytes());
                }

                uploadedFiles.add(fileTarget);




                i++;
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

        MainModel mainModel = new MainModel();
        finalFile = mainModel.calculate(messagesArrayList, uploadedFiles);

        System.out.println("Data is calculated");

        return new ResponseEntity<>("Files uploaded successfully", HttpStatus.OK);
    }

    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(String param) throws IOException {


        InputStreamResource resource = new InputStreamResource(new FileInputStream(finalFile));

        return ResponseEntity.ok()
                .contentLength(finalFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }





    public static void clearFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Recursive call to clear subdirectories
                        clearFolder(String.valueOf(file));
                    } else {
                        // Delete the file
                        file.delete();
                    }
                }
            }
        }
    }







}
