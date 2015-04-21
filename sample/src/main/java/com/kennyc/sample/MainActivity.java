package com.kennyc.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kennyc.view.MultiStateView;


public class MainActivity extends ActionBarActivity {

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
                    }
                });

        ListView list = (ListView) mMultiStateView.findViewById(R.id.list);

        String[] data = new String[100];
        for (int i = 0; i < 100; i++) {
            data[i] = String.valueOf(i);
        }

        list.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, data));
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
}
