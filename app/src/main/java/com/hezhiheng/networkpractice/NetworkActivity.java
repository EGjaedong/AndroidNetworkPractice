package com.hezhiheng.networkpractice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hezhiheng.networkpractice.async.FindAllPersonTask;
import com.hezhiheng.networkpractice.async.SavePersonsTask;
import com.hezhiheng.networkpractice.databaseWapper.LocalDataSource;
import com.hezhiheng.networkpractice.domain.PersonList;
import com.hezhiheng.networkpractice.entity.PersonEntity;
import com.hezhiheng.networkpractice.httpUtils.HttpUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NetworkActivity extends AppCompatActivity {
    public static final String URL = "https://twc-android-bootcamp.github.io/fake-data/data/default.json";
    private static final int DEFAULT_OPEN_TIMES = 0;
    private static final int OPEN_TIMES_STEP = 1;
    private SharedPreferences sharedPreferences;
    private LocalDataSource localDataSource;
    private Gson gson = new Gson();

    @BindView(R.id.btn_get_response)
    Button btnGetResponse;

    @BindView(R.id.btn_get_open_count)
    Button btnGetOpenTimes;

    @BindString(R.string.count_open_times_file)
    String openTimesFileName;

    @BindString(R.string.count_open_times_key)
    String openTimesKey;

    private Observable<String> createButtonClickObservable() {
        return Observable.create(emitter -> {
            btnGetResponse.setOnClickListener(v -> emitter.onNext(URL));
            emitter.setCancellable(() -> btnGetResponse.setOnClickListener(null));
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(openTimesFileName, Context.MODE_PRIVATE);
        ButterKnife.bind(this);
        setOpenTimes();
        localDataSource = MyApplication.getLocalDataSource();
    }

    private void setOpenTimes() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(openTimesKey, getOpenTimes() + OPEN_TIMES_STEP);
        edit.apply();
    }

    @OnClick({R.id.btn_get_open_count, R.id.btn_get_person_from_database})
    void buttonClick(Button button) {
        switch (button.getId()) {
            case R.id.btn_get_open_count:
                Toast.makeText(NetworkActivity.this, String.valueOf(getOpenTimes()),
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_person_from_database:
                showPersons();
                break;
        }
    }

    private void showPersons() {
        List<PersonEntity> persons = getPersonFromDataBase();
        String result = null;
        if (persons != null && persons.size() >= 2)
            result = persons.get(0).getName() + " and " + persons.get(1).getName();
        Toast.makeText(NetworkActivity.this, result, Toast.LENGTH_SHORT).show();
    }

    private List<PersonEntity> getPersonFromDataBase() {
        try {
            return new FindAllPersonTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    int getOpenTimes() {
        return sharedPreferences.getInt(openTimesKey, DEFAULT_OPEN_TIMES);
    }


    Function<String, String> getResponseWithOkHTTP = HttpUtils::getResponse;

    @Override
    protected void onStart() {
        super.onStart();
        createButtonClickObservable()
                .observeOn(Schedulers.io())
                .map(getResponseWithOkHTTP)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::dataHandler);
    }

    private void dataHandler(String result) {
        PersonList personList = gson.fromJson(result, PersonList.class);
        if (personList == null) {
            List<PersonEntity> personFromDataBase = getPersonFromDataBase();
            if (personFromDataBase != null)
                Toast.makeText(NetworkActivity.this, getPersonFromDataBase().get(0).getName(),
                        Toast.LENGTH_SHORT).show();
            return;
        }
        List<PersonEntity> personEntitiesFromNetwork = personList.getData().stream().
                map(person -> new PersonEntity(person.getName(), person.getAvatar()))
                .collect(Collectors.toList());
        savePerson(personEntitiesFromNetwork);
        if (personList.getData().size() >= 1) {
            Toast.makeText(NetworkActivity.this, personEntitiesFromNetwork.get(0).getName(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void savePerson(List<PersonEntity> personEntitiesFromNetwork) {
        List<PersonEntity> personEntitiesInDatabase = getPersonFromDataBase();
        if (personEntitiesInDatabase != null) {
            PersonEntity[] personEntitiesToSave = personEntitiesFromNetwork.stream()
                    .filter(personEntity -> (!personEntitiesInDatabase.contains(personEntity)))
                    .toArray(PersonEntity[]::new);
            if (personEntitiesToSave.length > 0)
                new SavePersonsTask().execute(personEntitiesToSave);
        }
    }
}