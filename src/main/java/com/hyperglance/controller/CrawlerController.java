package com.hyperglance.controller;

import com.hyperglance.model.CrawledURL;
import com.hyperglance.model.WebRequest;
import com.hyperglance.repository.CrawlerRepository;
import com.hyperglance.service.CrawlerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CrawlerController {

    private final CrawlerService crawlerService;

    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @PostMapping(value = "/crawl")
    public ResponseEntity<String> crawl(@RequestBody WebRequest webRequest) {
        crawlerService.crawl(webRequest.getUrl(), webRequest.getDepth());
        return ResponseEntity.ok("Successfully Crawled All Possible Links!");
    }

    @GetMapping(value = "/inventory")
    public ResponseEntity<List<CrawledURL>> inventory() {
        return ResponseEntity.ok(crawlerService.getInventory());
    }
}
