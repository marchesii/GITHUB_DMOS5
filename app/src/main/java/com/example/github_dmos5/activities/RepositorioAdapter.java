package com.example.github_dmos5.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.github_dmos5.R;
import com.example.github_dmos5.model.Repositorio;

import java.util.List;

public class RepositorioAdapter extends RecyclerView.Adapter<RepositorioAdapter.ViewHolder> {

    private List<Repositorio> mRepositorioList;

    public RepositorioAdapter(List<Repositorio> repositorio) {
        this.mRepositorioList = repositorio;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_repositorios, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RepositorioAdapter.ViewHolder holder, int i) {
        holder.nomeRepositorioTextView.setText(mRepositorioList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return mRepositorioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nomeRepositorioTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeRepositorioTextView = itemView.findViewById(R.id.textview_nome_repositorio);

        }
    }
}
