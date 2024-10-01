package org.hq.androidtool.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PlayStoreService {
    private static final String PS_URL_APP = "https://play.google.com/store/apps/details?id=%s";
    private static final String imgClass = "T75of";
    private String pack;

    private static final Logger logger = LoggerFactory.getLogger(PlayStoreService.class);

    public PlayStoreService(String pack){
        this.pack = pack;
    }

    public String getUrlImgApp() {
        Document doc = getHtml();

        if (doc == null) {
            return null;
        }

        Elements imgElements = doc.select(String.format(".%s", imgClass));

        if (!imgElements.isEmpty()) {
            for (Element imgElement : imgElements) {
                logger.info("Se pudo obtener url de aplicacion: " + imgElement.absUrl("src"));
                return imgElement.absUrl("src");
            }
        }

        logger.info("No se pudo obtener el link de icono de aplicacion: {}", pack);
        return null;
    }

    private Document getHtml() {
        String url = String.format(PS_URL_APP, pack);
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }
    }
}
