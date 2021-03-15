package com.hyperglance.service;

import com.hyperglance.model.ChildURL;
import com.hyperglance.model.CrawledURL;
import com.hyperglance.repository.CrawlerRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrawlerService {

    private final CrawlerRepository crawlerRepository;
    private List<String> visited = new ArrayList<>();

    public CrawlerService(CrawlerRepository crawlerRepository) {
        this.crawlerRepository = crawlerRepository;
    }

    public void crawl(String url, int depth) {

        try {
            if (!visited.contains(url)) {

                CrawledURL crawledURL = new CrawledURL();
                crawledURL.setUrl(url);

                Document document = request(url);
                if (ObjectUtils.isNotEmpty(document)) {
                    List<ChildURL> urlList = document.select("a[href]").stream().limit(depth).map(e -> new ChildURL(e.absUrl("href"))).collect(Collectors.toList());
                    crawledURL.setChildURL(urlList);
                }

                crawlerRepository.save(crawledURL);

                for(ChildURL childURL: crawledURL.getChildURL()) {
                    crawl(childURL.getUrl(), depth);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<CrawledURL> getInventory() {
        return crawlerRepository.findAll();
    }

    private Document request(String url) {
        try {
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            if (connection.response().statusCode() == 200) {
                System.out.println("Link: " + url);
                System.out.println(document.title());
                visited.add(url);
                return document;
            }

            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
