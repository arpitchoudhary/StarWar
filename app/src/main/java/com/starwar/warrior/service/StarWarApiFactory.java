package com.starwar.warrior.service;

import android.util.Log;

import com.starwar.warrior.dashboard.modal.StarWarCharacter;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class StarWarApiFactory extends BaseFactorySetup<StarWarService> {

    private static final String BASE_URL = "https://swapi.co/api/";

    private static StarWarApiFactory instance;
    private int nextPage;

    private StarWarApiFactory() {
        super(BASE_URL);
    }

    public static synchronized StarWarApiFactory getInstance() {
        if (instance == null) {
            instance = new StarWarApiFactory();
        }
        return instance;
    }

//
//    public Observable<StarWarCharacter> getCharacters(final int pageNumber) {
//        nextPage = pageNumber;
//        return prepareObservable((getService().getCharacter(pageNumber)).concatMap(new Func1<StarWarCharacter, Observable<? extends StarWarCharacter>>() {
//            @Override
//            public Observable<? extends StarWarCharacter> call(StarWarCharacter starWarCharacter) {
//                // Terminal case.
//                if (starWarCharacter.getNext() == null) {
//                    return Observable.just(starWarCharacter);
//                }
//
//                nextPage++;
//                return Observable.just(starWarCharacter)
//                        .concatWith(getCharacters(nextPage));
//            }
//        }));
//    }

    public Observable<StarWarCharacter> getCharacters(int pageNumber) {
        nextPage = pageNumber;
        final int exitCondition = pageNumber +3;
        return prepareObservable((getService().getCharacter(pageNumber)).concatMap(new Func1<StarWarCharacter, Observable<? extends StarWarCharacter>>() {
            @Override
            public Observable<? extends StarWarCharacter> call(StarWarCharacter starWarCharacter) {
                // Terminal case.

                Log.d("***", "Condition " + nextPage + " " + exitCondition);
                if (starWarCharacter.getNext() == null || nextPage == exitCondition) {
                    Log.d("***", "Exit Condition");
                    return Observable.just(starWarCharacter);
                }

                Log.d("***", "fetching Page" + nextPage);

                nextPage++;
                return  Observable.zip(
                        Observable.just(starWarCharacter),
                        getCharacters(nextPage),
                        new Func2<StarWarCharacter, StarWarCharacter, StarWarCharacter>() {
                            @Override
                            public StarWarCharacter call(StarWarCharacter starWarCharacter, StarWarCharacter starWarCharacter2) {
                                starWarCharacter.getResults().addAll(starWarCharacter2.getResults());
                                return starWarCharacter;
                            }
                        });
            }
        }));
    }
}
