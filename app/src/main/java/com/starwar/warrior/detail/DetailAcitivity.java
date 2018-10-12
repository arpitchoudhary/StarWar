package com.starwar.warrior.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.starwar.warrior.BaseActivity;
import com.starwar.warrior.R;
import com.starwar.warrior.dashboard.modal.Result;
import com.starwar.warrior.dashboard.ui.DashboardActivity;

public class DetailAcitivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Result result ;
        if(getIntent() !=null){
           result =  getIntent().getParcelableExtra(DashboardActivity.RESULT);

            ((TextView)findViewById(R.id.name)).setText("Name :" + result.getName());
            ((TextView)findViewById(R.id.height)).setText("Height :" + result.getHeight());
            ((TextView)findViewById(R.id.mass)).setText("Mass :" + result.getMass());
            ((TextView)findViewById(R.id.hairColor)).setText("HairColor :" + result.getHairColor());
            ((TextView)findViewById(R.id.skinColor)).setText("SkinColor :" +result.getSkinColor());
            ((TextView)findViewById(R.id.birYr)).setText("BirthYear :" + result.getBirthYear());

        }
    }
}
