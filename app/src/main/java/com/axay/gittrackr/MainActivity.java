package com.axay.gittrackr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.axay.gittrackr.adapter.RepositoryRecyclerViewAdapter;
import com.axay.gittrackr.db.RepositoryDatabaseHelper;
import com.axay.gittrackr.model.ShareRepository;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RepositoryRecyclerViewAdapter adapter;
    private List<String[]> repositoryList;
    private RepositoryDatabaseHelper dbHelper;

    ShapeableImageView imageView;
    MaterialTextView textView;
    MaterialButton addNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadRepositoryData();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repositoryList = new ArrayList<>();
        adapter = new RepositoryRecyclerViewAdapter(this, repositoryList);
        recyclerView.setAdapter(adapter);

        dbHelper = new RepositoryDatabaseHelper(this);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_add) {
                Intent intent = new Intent(this, RepoActivity.class);
                startActivity(intent);
                return true;
            } else
                return false;
        });

        addNow = findViewById(R.id.addNow);
        addNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, RepoActivity.class);
            startActivity(intent);
        });

        imageView = findViewById(R.id.illustration);
        textView = findViewById(R.id.textView);

        adapter.setOnDeleteButtonClickListener(position -> deleteRepository(position));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadRepositoryData() {
        if (repositoryList != null) {
            repositoryList.clear();
            repositoryList.addAll(dbHelper.getAllRepositories());
            adapter.notifyDataSetChanged();

            if (repositoryList.isEmpty()) {
                recyclerView.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                addNow.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                addNow.setVisibility(View.INVISIBLE);
            }
        } else {
            repositoryList = new ArrayList<>();
        }
    }

    private void deleteRepository(int position) {
        String[] repositoryData = repositoryList.get(position);
        String url = repositoryData[0];

        dbHelper.deleteRepository(url);

        repositoryList.remove(position);
        adapter.notifyItemRemoved(position);

        loadRepositoryData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRepositoryData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("repositoryList", new ArrayList<>());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}