package com.poomdev.mikisetapi.processor;

import com.poomdev.mikisetapi.persistence.SetIndex;
import com.poomdev.mikisetapi.persistence.repository.SetIndexRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;

public class RetrieverProcessor implements Processor {
    private Logger logger = LoggerFactory.getLogger(RetrieverProcessor.class);

    @Autowired
    private SetIndexRepository repository;

    @Override
    public void process(Exchange exchange) throws IOException {
        Document doc = Jsoup.connect("https://marketdata.set.or.th/mkt/sectorialindices.do?language=en&country=US").get();
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
