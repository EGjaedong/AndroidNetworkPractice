package com.hezhiheng.networkpractice.async;

import android.os.AsyncTask;

import com.hezhiheng.networkpractice.MyApplication;
import com.hezhiheng.networkpractice.entity.PersonEntity;

import java.util.List;

public class FindAllPersonTask extends AsyncTask<Void, Void, List<PersonEntity>> {
    @Override
    protected List<PersonEntity> doInBackground(Void... voids) {
        return MyApplication.getLocalDataSource().getPersonDao().getAll();
    }
}
