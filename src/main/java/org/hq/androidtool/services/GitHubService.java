package org.hq.androidtool.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.hq.androidtool.utils.GitHubParsers;

public class GitHubService {
    private static final String REPO_URL = "https://github.com/HugoQuinn2/android-tool.git";
    private HttpClient client;
    private HttpRequest request;
    private GitHubParsers gitHubParsers;

    public GitHubService() {
        gitHubParsers = new GitHubParsers();

        try {
            client = HttpClient.newHttpClient();
            request = HttpRequest
                    .newBuilder()
                    .uri(new URI(REPO_URL))
                    .header("Accept", "application/vnd.github.v3+json")
                    .build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String getLatestVersion(){
        try {
            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            return gitHubParsers.parseLatestVersion(response.body());
        } catch (Exception e){
            return null;
        }
    }

    public String getUrlVersion() {
        return null;
    }

}
