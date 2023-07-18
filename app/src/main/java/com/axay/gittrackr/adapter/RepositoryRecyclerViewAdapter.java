package com.axay.gittrackr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axay.gittrackr.R;
import com.axay.gittrackr.model.ShareRepository;
import com.axay.gittrackr.model.URLOpener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class RepositoryRecyclerViewAdapter extends RecyclerView.Adapter<RepositoryRecyclerViewAdapter.ViewHolder>{

    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    private Context context;
    private List<String[]> repositoryList;

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.onDeleteButtonClickListener = listener;
    }

    public RepositoryRecyclerViewAdapter(Context context, List<String[]> repositoryList) {
        this.context = context;
        this.repositoryList = repositoryList;
    }

    @NonNull
    @Override
    public RepositoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.repo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryRecyclerViewAdapter.ViewHolder holder, int position) {
        String[] repositoryData = repositoryList.get(position);
        holder.buttonUrl.setOnClickListener(v -> {
            String url = repositoryData[0];
            URLOpener.openUrlInBrowser(v.getContext(), url);
        });
        holder.buttonShare.setOnClickListener(v -> {
            String url = repositoryData[0];
            String name = repositoryData[1];
            ShareRepository.ShareRepo(v.getContext(), name, url);
        });
        holder.repoNameTextView.setText(repositoryData[1]);
        holder.repoDescriptionTextView.setText(repositoryData[2]);
        holder.delete.setOnClickListener(v -> {
            if (onDeleteButtonClickListener != null) {
                onDeleteButtonClickListener.onDeleteButtonClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repositoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView repoNameTextView;
        MaterialTextView repoDescriptionTextView;
        MaterialButton buttonUrl, buttonShare;
        ShapeableImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonUrl = itemView.findViewById(R.id.takeALook);
            repoNameTextView = itemView.findViewById(R.id.repoNameTextView);
            repoDescriptionTextView = itemView.findViewById(R.id.repoDescriptionTextView);
            delete = itemView.findViewById(R.id.delete);
            buttonShare = itemView.findViewById(R.id.shareButton);
        }
    }
}
