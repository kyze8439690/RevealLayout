package me.yugy.github.reveallayout.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by yugy on 14/11/21.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView list = new ListView(this);
        setContentView(list);
        String[] entries = new String[]{
                "SingleChildActivity",
                "MultiChildActivity",
                "FragmentActivity"
        };
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entries));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, SingleChildActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, MultiChildActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, FragmentSampleActivity.class));
                        break;
                }
            }
        });
    }
}
