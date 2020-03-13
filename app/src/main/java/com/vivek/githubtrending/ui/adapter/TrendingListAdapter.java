package com.vivek.githubtrending.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vivek.githubtrending.R;
import com.vivek.githubtrending.data.local.entity.GithubEntity;
import com.vivek.githubtrending.databinding.ListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class TrendingListAdapter extends RecyclerView.Adapter<TrendingListAdapter.TrendingViewHolder> {

    private Context context;
    private List<GithubEntity> items;

    public TrendingListAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }


    @NonNull
    @Override
    public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ListItemBinding itemBinding = ListItemBinding.inflate(layoutInflater, parent, false);

        return new TrendingViewHolder(itemBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public GithubEntity getItem(int position) {
        return items.get(position);
    }

    public void setItems(List<GithubEntity> items) {
        this.items.addAll(items);
    }


    protected class TrendingViewHolder extends RecyclerView.ViewHolder {

        private ListItemBinding binding;

        public TrendingViewHolder(ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(GithubEntity githubEntity) {
            Picasso.get().load(githubEntity.getAvatar())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(binding.itemProfileImg);

            binding.itemTitle.setText(githubEntity.getName());
            binding.itemUsername.setText(githubEntity.getAuthor());

        }

    }
}
