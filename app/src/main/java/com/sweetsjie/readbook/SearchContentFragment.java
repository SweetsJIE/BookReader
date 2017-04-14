package com.sweetsjie.readbook;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by sweets on 17/4/12.
 */

public class SearchContentFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Button netSearchButton;
    private Button localSearchButton;
    private EditText bookName;
    private Bundle bundle;
    private MydatabaseHelper mydatabaseHelper;
    public static String NAME[]=new String[100];
    public static String URL[]=new String[100];
    //public static String[] NAME={"1","2"};
    private int i;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_content_frag, container, false);

        netSearchButton = (Button) view.findViewById(R.id.NetSearchButton);
        localSearchButton = (Button) view.findViewById(R.id.LocalSearchButton);
        bookName = (EditText) view.findViewById(R.id.BookName);

        netSearchButton.setOnClickListener(this);
        localSearchButton.setOnClickListener(this);

        return view;
    }

    public void openUrl(String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    //按键监听事件
    @Override
    public void onClick(View v) {
        //获取组件的资源id
        int id = v.getId();
        switch (id) {
            case R.id.NetSearchButton:
                openUrl("http://m.qidian.com/search?kw="+bookName.getText().toString());
                break;
            case R.id.LocalSearchButton:
                i=0;
                String name = bookName.getText().toString()+".txt";
                mydatabaseHelper = new MydatabaseHelper(view.getContext(),"book.db",null,1);
                mydatabaseHelper.getWritableDatabase();
                final SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();
                Cursor cursor = db.query("localbook", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String buf= cursor.getString((cursor.getColumnIndex("bookname")));
                        NAME[i] = buf;
                        buf= cursor.getString((cursor.getColumnIndex("bookurl")));
                        URL[i] = buf;
                        if (NAME[i].equals(name)){
                            openFile(URL[i],NAME[i]);
                        }
                        i++;
                    } while (cursor.moveToNext());
                }
                cursor.close();
                break;
            default:
                break;
        }
    }

    //打开TXT文件
    public void openFile(String bookurl,String bookname){
        Intent intent = new Intent(view.getContext(), ViewFile.class);
        bundle = new Bundle();
        bundle.putString("fileName", bookurl);
        bundle.putString("bookname",bookname);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

}
