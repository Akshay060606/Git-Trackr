package com.axay.gittrackr.model;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GitHubAPIService {
    private static final String GITHUB_API_BASE_URL = "https://api.github.com/";

    public interface GitHubRepoCallback {
        void onSuccess(String url, String repoName, String description);
        void onFailure(Exception e);
    }

    public static void getRepository(String owner, String repoName, GitHubRepoCallback callback) {
        OkHttpClient client = new OkHttpClient();

        String url = GITHUB_API_BASE_URL + "repos/" + owner.trim() + "/" + repoName.trim();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String repositoryData = response.body().string();
                    try {
                        // Parse the JSON response
                        JSONObject jsonData = new JSONObject(repositoryData);

                        // Extract URL, repoName, and description
                        String url = jsonData.getString("html_url");
                        String repoName = jsonData.getString("name");
                        String description = jsonData.getString("description");

                        // Pass the extracted information to the callback
                        callback.onSuccess(url, repoName, description);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    callback.onFailure(new Exception("Failed to retrieve repository"));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }
        });

    }
}
