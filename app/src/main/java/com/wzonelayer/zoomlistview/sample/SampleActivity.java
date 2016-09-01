package com.wzonelayer.zoomlistview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import com.wzonelayer.zoomlistview.ZoomScrollListView;
import java.util.ArrayList;

public class SampleActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ZoomScrollListView
            listView = (ZoomScrollListView) findViewById(R.id.listview);
        View headerLayout = getLayoutInflater().inflate(R.layout.header, listView, false);
        View headerView = headerLayout.findViewById(R.id.header_logo);
        listView.setHeadViewBeforeSetAdapter(headerLayout, headerView);

        ArrayList<String> FAKES = new ArrayList<String>();
        for (int i = 0; i < 1000; i++) {
            FAKES.add("entry " + i);
        }
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FAKES));
    }
}
