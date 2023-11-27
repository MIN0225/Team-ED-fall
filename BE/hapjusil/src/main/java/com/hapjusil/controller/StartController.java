package com.hapjusil.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hapjusil.dto.CrawlerResultDto;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@RestController
@CrossOrigin("*")
public class StartController {


    @PostMapping("/start")
    public ResponseEntity<CrawlerResultDto[]> startCrawler(@RequestBody CrawlerRequest request) {
        try {
            CrawlerResultDto[] results = runCrawler(request.getCommonAddress(), request.getDate());
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Getter
    @Setter
    static class CrawlerRequest {
        private String commonAddress;
        private String date;
    }

    private CrawlerResultDto[] runCrawler(String commonAddress, String date) throws IOException, InterruptedException {
        String crawlerPath = "/Users/macbookpro/Downloads/Team-ED-fall-develop/crawler";
        String crawlerScript = "realtime-crawler-parent.js";
        String resultsFilePath = crawlerPath + "/results.json";
        String command = "node " + crawlerScript + " " + commonAddress + " " + date;

        ProcessBuilder builder = new ProcessBuilder(command.split(" "));
        builder.directory(new File(crawlerPath));
        Process process = builder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        process.waitFor();

        // 크롤링 결과 파일을 읽어서 문자열로 반환
        File resultsFile = new File(resultsFilePath);
        String resultsJson = FileUtils.readFileToString(resultsFile, StandardCharsets.UTF_8);

        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(resultsJson, CrawlerResultDto[].class);
    }

}
