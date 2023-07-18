package com.axay.gittrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.axay.gittrackr.db.RepositoryDatabaseHelper;
import com.axay.gittrackr.model.GitHubAPIService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class RepoActivity extends AppCompatActivity {

    static String OWNER_NAME = "";
    static String REPO_NAME = "";

    TextInputEditText ownerName, repoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ownerName = findViewById(R.id.ownerEditText);
        repoName = findViewById(R.id.repoEditText);

        MaterialButton save = findViewById(R.id.repoSave);
        save.setOnClickListener(v -> {
            HitGitHub();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void HitGitHub() {
        if ((Objects.requireNonNull(ownerName.getText()).toString().isEmpty()) || (Objects.requireNonNull(repoName.getText()).toString().isEmpty())) {
            Snackbar.make(repoName, "Please Enter Owner name and Repo name", BaseTransientBottomBar.LENGTH_SHORT)
                    .show();
        } else {
            OWNER_NAME = Objects.requireNonNull(ownerName.getText()).toString();
            REPO_NAME = Objects.requireNonNull(repoName.getText()).toString();

            GitHubAPIService.getRepository(OWNER_NAME, REPO_NAME, new GitHubAPIService.GitHubRepoCallback() {
                @Override
                public void onSuccess(String url, String _repoName, String description) {
                    runOnUiThread(() -> {
                        // Store the repository in the database
                        RepositoryDatabaseHelper dbHelper = new RepositoryDatabaseHelper(getApplicationContext());
                        dbHelper.insertRepository(url, _repoName, description);

                        ownerName.setText("");
                        repoName.setText("");
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    runOnUiThread(() -> {
                        // Handle the failure to retrieve the repository
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to retrieve repository", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

}