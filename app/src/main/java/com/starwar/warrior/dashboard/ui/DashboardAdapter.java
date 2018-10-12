package com.starwar.warrior.dashboard.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starwar.warrior.R;
import com.starwar.warrior.dashboard.modal.Result;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.CharacterViewHolder> {

    private List<Result> result;
    private Context context;
    private ItemClickListener clickListener;

    interface ItemClickListener{
        void onItemClick(Result result);
    }

    DashboardAdapter(Context context, List<Result> result, ItemClickListener clickListener){
        this.context = context;
        this.result = result;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_recycler_item, viewGroup, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder characterViewHolder, int i) {
        characterViewHolder.holder(result.get(i), clickListener);

    }

    @Override
    public int getItemCount() {
        if(result.size() == 0){
            return 0;
        }
        return result.size() - 1;
    }

    public void setData(List<Result> results) {
        this.result.addAll(results);
        notifyDataSetChanged();
    }

    static class CharacterViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
         CharacterViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textCharacterName);
        }

        void holder(final Result result, final ItemClickListener clickListener) {
            String name = result.getName();
            textView.setText(name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(result);
                }
            });
        }
    }
}
