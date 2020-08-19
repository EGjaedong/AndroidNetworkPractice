package com.hezhiheng.networkpractice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Cancellable;
import io.reactivex.rxjava3.functions.Consumer;
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
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                btnGetResponse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emitter.onNext(URL);
                    }
                });

                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Throwable {
                        btnGetResponse.setOnClickListener(null);
                    }
                });
            }
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

        String result = persons.get(0).getName() + " and " + persons.get(1).getName();
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


    Function<String, String> getResponseWithOkHTTP = new Function<String, String>() {
        @Override
        public String apply(String url) throws Throwable {
            return HttpUtils.getResponse(url);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Observable<String> sendRequestObservable = createButtonClickObservable();

        sendRequestObservable
                .observeOn(Schedulers.io())
                .map(getResponseWithOkHTTP)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Throwable {
                        if (result != null)
                            dataHandler(result);
                        else
                            Toast.makeText(NetworkActivity.this, "Response is Empty!",
                                    Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void dataHandler(String result) {
        PersonList personList = gson.fromJson(result, PersonList.class);
        savePerson(personList);
        if (personList.getData().size() > 1) {
            Toast.makeText(NetworkActivity.this, personList.getData().get(0).getName(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void savePerson(PersonList persons) {
        new SavePersonsTask().execute(persons.getData().stream().map(person -> {
            return new PersonEntity(person.getName(), person.getAvatar());
        }).toArray(PersonEntity[]::new));
    }
}