package com.starwar.warrior.dashboard.presenter;

import android.util.Log;

import com.starwar.warrior.dashboard.modal.Result;
import com.starwar.warrior.dashboard.modal.StarWarCharacter;
import com.starwar.warrior.dashboard.view.DashboardContract;
import com.starwar.warrior.service.StarWarApiFactory;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

public class DashboardPresenter {

    private DashboardContract view;
    private List<Result> results;


    public DashboardPresenter(DashboardContract view) {
        this.view = view;
        this.results = new ArrayList<>();
    }


    public void fetchCharacterName(int pageNumber){

        StarWarApiFactory.getInstance().getCharacters(pageNumber).subscribe(new Observer<StarWarCharacter>() {
            @Override
            public void onCompleted() {
                Log.d("***","onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("***","Error");
                if(e instanceof ConnectException){
                    view.showToast("Check you Internet Connectivity!!");
                }else{
                    view.showToast("Some Error Occured!!");
                }
                view.showTryAgain();
                view.hideProgress();
            }

            @Override
            public void onNext(StarWarCharacter starWarCharacter) {
                Log.d("***","onNext " + starWarCharacter.getResults().size());
                view.hideProgress();
                view.setAdapter(starWarCharacter.getResults());
            }
        });

//
//        StarWarApiFactory.getInstance().getCharacters(1)
//                .concatMap(new Func1<StarWarCharacter, Observable<?>>() {
//                    @Override
//                    public Observable<?> call(StarWarCharacter starWarCharacter) {
//                        return Observable.from(starWarCharacter.getResults());
//                    }
//                }).subscribe(new rx.Observer<Object>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object o) {
//                if(o instanceof Result){
//                    Result ob = (Result) o;
//                    results.add(ob);
//                    view.hideProgress();
//                    view.setAdapter(results);
//
//                }
//            }
//        });
    }

    public void startLoading(int pageNumber) {
        view.showProgress();
        Log.d("***", "fetchCharacterName, " + pageNumber);
        fetchCharacterName(pageNumber);
    }
}
