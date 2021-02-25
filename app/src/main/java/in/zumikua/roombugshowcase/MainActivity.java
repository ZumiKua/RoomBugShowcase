package in.zumikua.roombugshowcase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private AppDatabase mDatabase;
    private TextView mFirst;
    private TextView mSecond;
    private CompositeDisposable mDisposables;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler(Looper.getMainLooper());
        mDisposables = new CompositeDisposable();
        mDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db" + new Random().nextInt())
                .build();
        mFirst = findViewById(R.id.first);
        mSecond = findViewById(R.id.second);
        findViewById(R.id.button).setOnClickListener(v -> start());

    }

    private void start() {
        startTransaction();
        mHandler.postDelayed(this::startObserve, 500L);
    }

    private void startObserve() {
        Disposable d1 = mDatabase.dao().observerItem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> mFirst.setText("" + item.size()));
        Disposable d2 = mDatabase.dao().observerItem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> mSecond.setText("" + item.size()));
        mDisposables.addAll(d1, d2);
    }

    private void startTransaction() {
        Disposable d = Completable.fromAction(() -> mDatabase.runInTransaction(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mDatabase.dao().insert(new Item(1, 10));
        })).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mDisposables.add(d);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
    }
}