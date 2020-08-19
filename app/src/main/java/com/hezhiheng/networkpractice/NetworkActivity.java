package com.hezhiheng.networkpractice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hezhiheng.networkpractice.domain.DataList;
import com.hezhiheng.networkpractice.httpUtils.HttpUtils;

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
    @BindView(R.id.btn_get_response)
    Button btnGetResponse;

    Function<String, String> getResponseWithOkHTTP = new Function<String, String>() {
        @Override
        public String apply(String url) throws Throwable {
            return HttpUtils.getResponse(url);
        }
    };

    public static final String URL = "https://twc-android-bootcamp.github.io/fake-data/data/default.json";

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
        ButterKnife.bind(this);
    }

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
        Gson gson = new Gson();
        DataList dataList = gson.fromJson(result, DataList.class);
        if (dataList.getData().size() > 1) {
            Toast.makeText(NetworkActivity.this, dataList.getData().get(0).getName(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}