package com.kennyc.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kennyc.view.MultiStateView;


public class MainActivity extends AppCompatActivity implements MultiStateView.StateListener {

    private TestHandler mHandler = new TestHandler();

    private MultiStateView mMultiStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMultiStateView = (MultiStateView) findViewById(R.id.multiStateView);
        mMultiStateView.setStateListener(this);
        mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.retry)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                        Toast.makeText(getApplicationContext(), "Fetching Data", Toast.LENGTH_SHORT).show();
                        Message msg = mHandler.obtainMessage();
                        msg.obj = mMultiStateView;
                        mHandler.sendMessageDelayed(msg, 3000);
                    }
                });

        ListView list = (ListView) mMultiStateView.findViewById(R.id.list);

        String[] data = new String[100];
        for (int i = 0; i < 100; i++) {
            data[i] = "Row " + i;
        }

        list.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, data));
    }

    @Override
    protected void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.error:
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                return true;

            case R.id.empty:
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                return true;

            case R.id.content:
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                return true;

            case R.id.loading:
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStateChanged(@MultiStateView.ViewState int viewState) {
        Log.v("MSVSample", "onStateChanged; viewState: " + viewState);
    }

    @Override
    public void onStateInflated(@MultiStateView.ViewState int viewState, @NonNull View view) {
        Log.v("MSVSample", "onStateInflated; viewState: " + viewState + ", view: " + view.toString());
    }

    private static class TestHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof MultiStateView) {
                ((MultiStateView) msg.obj).setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }

            super.handleMessage(msg);
        }
    }
}
