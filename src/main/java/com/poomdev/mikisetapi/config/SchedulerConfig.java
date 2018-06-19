package com.poomdev.mikisetapi.config;

import com.poomdev.mikisetapi.persistence.SetIndex;
import com.poomdev.mikisetapi.persistence.repository.SetIndexRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Date;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    private Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    @Value("${retriever.url}")
    private String url;

    @Autowired
    private SetIndexRepository repository;

    @Scheduled(fixedRateString = "${retriever.schedule.period}")
    public void retreiver() throws IOException {
        logger.info("[Scheduler] retrieve from url: " + url);
        Document doc = Jsoup.connect(url).get();
        Elements tBodyList = doc.getElementById("maincontent").getElementsByTag("tbody");
        Elements trList = tBodyList.get(0).getElementsByTag("tr");
        for (Element tr: trList) {
            Elements tdList = tr.getElementsByTag("td");
            switch (tdList.get(0).text()) {
                case "SET":
                case "SET50":
                case "SET100":
                    SetIndex index = new SetIndex();
                    index.setName(tdList.get(0).text());
                    index.setLast(Double.parseDouble(tdList.get(1).text().replace(",","")));
                    index.setDate(new Date());
                    repository.save(index);

                    logger.info(new StringBuilder()
                            .append("[Scheduler] ")
                            .append("Index: ")
                            .append(tdList.get(0).text())
                            .append("\t")
                            .append("Last: ")
                            .append(tdList.get(1).text())
                            .toString());
            }
        }
    }
}
