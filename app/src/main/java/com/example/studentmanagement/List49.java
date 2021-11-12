package com.example.studentmanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class List49 extends Activity {

    // имена атрибутов для Map
    final String ATTRIBUTE_NAME_ID = "ids";
    final String ATTRIBUTE_NAME_NAME = "names";
    final String ATTRIBUTE_NAME_MASA = "masa";
    final String ATTRIBUTE_NAME_VALUE = "values";
    final String ATTRIBUTE_NAME_CAT = "cat";

    // картинки для отображения динамики
    final int positive = android.R.drawable.btn_plus;
    final int negative = android.R.drawable.btn_minus;

    ListView lvSimple;

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list49);

        Intent intent2 = getIntent();
        ArrayList<Integer> ids = intent2.getIntegerArrayListExtra("ids");
        ArrayList<String> names = intent2.getStringArrayListExtra("names");
        ArrayList<String> masa = intent2.getStringArrayListExtra("masa");
        ArrayList<Integer> values = intent2.getIntegerArrayListExtra("values");
        ArrayList<Integer> cat = intent2.getIntegerArrayListExtra("cat");

        ArrayList<Map<String, Object>> data = new ArrayList<>(values.size());
        Map<String, Object> m;
        int img;
        for (int i = 0; i < values.size(); i++) {
            m = new HashMap<>();
            m.put(ATTRIBUTE_NAME_ID, ids.get(i));
            m.put(ATTRIBUTE_NAME_NAME, names.get(i));
            m.put(ATTRIBUTE_NAME_MASA, masa.get(i));
            m.put(ATTRIBUTE_NAME_VALUE, values.get(i));
            m.put(ATTRIBUTE_NAME_CAT, cat.get(i));

            data.add(m);
        }

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = {ATTRIBUTE_NAME_ID, ATTRIBUTE_NAME_NAME, ATTRIBUTE_NAME_MASA, ATTRIBUTE_NAME_VALUE,
                ATTRIBUTE_NAME_CAT};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = {R.id.id, R.id.tvText, R.id.masa, R.id.tvValue, R.id.cat};

        // создаем адаптер
        MySimpleAdapter sAdapter = new MySimpleAdapter(this, data,
                R.layout.item_list49, from, to);

        // определяем список и присваиваем ему адаптер
        lvSimple = (ListView) findViewById(R.id.lvSimple);
        lvSimple.setAdapter(sAdapter);
    }

    class MySimpleAdapter extends SimpleAdapter {

        public MySimpleAdapter(Context context,
                               List<? extends Map<String, ?>> data, int resource,
                               String[] from, int[] to) {
            super(context, data, resource, from, to);
        }


    }
}
