package com.starwar.warrior.dashboard.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


import com.starwar.warrior.BaseActivity;
import com.starwar.warrior.R;
import com.starwar.warrior.dashboard.modal.Result;
import com.starwar.warrior.dashboard.presenter.DashboardPresenter;
import com.starwar.warrior.dashboard.view.DashboardContract;
import com.starwar.warrior.detail.DetailAcitivity;

import java.util.ArrayList;
import java.util.List;


public class DashboardActivity extends BaseActivity implements DashboardContract, DashboardAdapter.ItemClickListener{

    public static final String RESULT = "result";
    private DashboardPresenter presenter;
    private ProgressDialog dialog;
    private RecyclerView recyclerView;
    private DashboardAdapter adapter;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisible;
    private int totalCount;
    private LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        adapter = new DashboardAdapter(this, new ArrayList<Result>(), this);

        recyclerView = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        presenter = new DashboardPresenter(this);
        presenter.startLoading(1);

        recyclerView.setAdapter(adapter);


       recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
           public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
               super.onScrolled(recyclerView, dx, dy);
               totalCount = layoutManager.getItemCount();
               lastVisible = layoutManager.findLastVisibleItemPosition();

               if(!isLoading && totalCount <= lastVisible + visibleThreshold){
                   isLoading = true;
                   presenter.startLoading(4);
               }
           }
       });

    }



    @Override
    public void setAdapter(List<Result> results) {
        isLoading = false;
        adapter.setData(results);
    }

    @Override
    public void showProgress() {
        recyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.retryLayout).setVisibility(View.GONE);
        dialog.show();
    }

    @Override
    public void hideProgress() {
        if(dialog !=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }


    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTryAgain() {
        recyclerView.setVisibility(View.GONE);
        findViewById(R.id.retryLayout).setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(Result result) {
        Intent intent = new Intent(DashboardActivity.this, DetailAcitivity.class);
        intent.putExtra(DashboardActivity.RESULT, result);
        startActivity(intent);
    }
}
