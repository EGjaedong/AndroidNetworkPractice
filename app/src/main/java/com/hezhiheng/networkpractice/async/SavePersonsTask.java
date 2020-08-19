package com.hezhiheng.networkpractice.async;

import android.os.AsyncTask;

import com.hezhiheng.networkpractice.MyApplication;
import com.hezhiheng.networkpractice.entity.PersonEntity;

public class SavePersonsTask extends AsyncTask<PersonEntity[], Void, Void> {
    @Override
    protected Void doInBackground(PersonEntity[]... personEntities) {
        MyApplication.getLocalDataSource().getPersonDao().insertAll(personEntities[0]);
        return null;
    }
}
