package controllers;

import org.hq.androidtool.services.PlayStoreService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UnitTestPlayStoreService {
    @Test
    public void getUrlPacks() {

        List<String> packs = new ArrayList<>();
        packs.add("com.google.android.youtube");
        packs.add("com.google.android.googlequicksearchbox");
        packs.add("com.google.android.apps.messaging");
        packs.add("com.google.android.deskclock");

        for (String pack : packs) {
            PlayStoreService playStoreService = new PlayStoreService(pack);
            System.out.println(playStoreService.getUrlImgApp());
        }

    }
}
