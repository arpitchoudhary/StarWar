package com.starwar.warrior.dashboard.view;

import com.starwar.warrior.dashboard.modal.Result;

import java.util.List;

public interface DashboardContract {
    void setAdapter(List<Result> results);

    void showProgress();

    void hideProgress();

    void showToast(String msg);

    void showTryAgain();
}
