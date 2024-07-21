package org.hq.androidtool.utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GitHubParsers {

    public String parseLatestVersion(String json) {
        JsonArray releases = JsonParser.parseString(json).getAsJsonArray();

        if (releases.size() > 0) {
            JsonObject latestRelease = releases.get(0).getAsJsonObject();
            return latestRelease.get("tag_name").getAsString();
        }

        return null;
    }

}
