package com.kennyc.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kennyc.view.MultiStateView;


public class MainActivity extends AppCompatActivity {

    private TestHandler mHandler = new TestHandler();

    private MultiStateView mMultiStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMultiStateView = (MultiStateView) findViewById(R.id.multiStateView);
        mMultiStateView.getView(MultiStateView.ViewState.ERROR).findViewById(R.id.retry)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMultiStateView.setViewState(MultiStateView.ViewState.LOADING);
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
                mMultiStateView.setViewState(MultiStateView.ViewState.ERROR);
                return true;

            case R.id.empty:
                mMultiStateView.setViewState(MultiStateView.ViewState.EMPTY);
                return true;

            case R.id.content:
                mMultiStateView.setViewState(MultiStateView.ViewState.CONTENT);
                return true;

            case R.id.loading:
                mMultiStateView.setViewState(MultiStateView.ViewState.LOADING);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class TestHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof MultiStateView) {
                ((MultiStateView) msg.obj).setViewState(MultiStateView.ViewState.CONTENT);
            }

            super.handleMessage(msg);
        }
    }
}
