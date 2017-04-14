package com.sweetsjie.readbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sweets on 17/4/14.
 */

public class CollectBook extends Activity{

    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private Bundle bundle;
    private MydatabaseHelper mydatabaseHelper;
    public  String NAME[]=new String[100];
    public  String URL[]=new String[100];
    private int i=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.collect_book);



        mydatabaseHelper = new MydatabaseHelper(this,"book.db",null,1);
        mydatabaseHelper.getWritableDatabase();
        final SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("collect", null, null, null, null, null, null);
        //Log.d("MainActivity", "book name is " + cursor);
        if (cursor.moveToFirst()) {
            do {
                String buf= cursor.getString((cursor.getColumnIndex("bookname")));
                NAME[i] = buf;
                buf= cursor.getString((cursor.getColumnIndex("bookurl")));
                URL[i] = buf;
                //Log.d("MainActivity", "book name is " + buf);
                i++;
            } while (cursor.moveToNext());
        }
        i=0;
        cursor.close();


        refreshGridView();

        //设置GridView短按监听事件
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openFile(URL[position], NAME[position]);
            }
        });



    }

    @Override
    protected void onRestart() {
        super.onRestart();

        i=0;
        mydatabaseHelper = new MydatabaseHelper(this,"book.db",null,1);
        mydatabaseHelper.getWritableDatabase();
        final SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();
        Cursor cs = db.query("collect", null, null, null, null, null, null);
        //Log.d("MainActivity", "book name is " + cursor);
        if (cs.moveToFirst()) {
            do {
                String buf= cs.getString((cs.getColumnIndex("bookname")));
                NAME[i] = buf;
                buf= cs.getString((cs.getColumnIndex("bookurl")));
                URL[i] = buf;
                Log.d("MainActivity", "book name is " + NAME[i]);
                Log.d("MainActivity", "book url is " + URL[i]);
                i++;
            } while (cs.moveToNext());
        }
        i=0;
        cs.close();

        refreshGridView();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    //GridView刷新界面
    public void refreshGridView(){
        gview = (GridView) findViewById(R.id.collectGridView);
        //textView = (TextView) view.findViewById(R.id.GVBookName);
        //textView.setTextColor(Color.parseColor("#000000"));
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        try {
            getData();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.GVBookImage,R.id.GVBookName};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.book_item, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
    }


    //打开TXT文件
    public void openFile(String bookurl,String bookname){
        Intent intent = new Intent(this, ViewFile.class);
        bundle = new Bundle();
        bundle.putString("fileName", bookurl);
        bundle.putString("bookname",bookname);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    public List<Map<String, Object>> getData() throws UnsupportedEncodingException {
        for(int i=0;i<NAME.length;i++){
            if(NAME[i]==null)break;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", R.mipmap.bookicon);
            map.put("text", NAME[i]);
            data_list.add(map);
        }

        return data_list;
    }



    }
