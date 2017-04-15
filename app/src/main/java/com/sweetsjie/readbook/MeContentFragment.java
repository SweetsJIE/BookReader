package com.sweetsjie.readbook;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sweets on 17/4/12.
 */

public class MeContentFragment extends Fragment {
    private View view;
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private TextView username;
    private MydatabaseHelper mydatabaseHelper;
    private Intent intent;

    public static String localUserName[] = new String[100];
    public static String localUserPassword[] = new String[100];
    private String bookname;
    private int i;

    private int[] icon = {R.mipmap.collect, R.mipmap.nightmode, R.mipmap.register, R.mipmap.loginuser,R.mipmap.changepassword, R.mipmap.close};
    private String[] iconName = {"收藏夹", "夜间模式", "注册账号","登录账号", "更改密码", "注销账户"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.me_content_frag, container, false);

        username = (TextView) view.findViewById(R.id.userName);

        refreshGridView();

        //设置GridView短按监听事件
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent = new Intent(view.getContext(), CollectBook.class);
                        startActivityForResult(intent, 0);
                        break;

                    case 2:
                        intent = new Intent(view.getContext(), RegisterAccount.class);
                        startActivityForResult(intent, 0);
                        break;
                    case 3:
                        intent = new Intent(view.getContext(), LoginUser.class);
                        startActivityForResult(intent, 0);
                        break;
                    case 4:
                        intent = new Intent(view.getContext(), ChangePassword.class);
                        startActivityForResult(intent, 0);
                        break;
                    case 5:
                        mydatabaseHelper = new MydatabaseHelper(view.getContext(), "book.db", null, 1);
                        mydatabaseHelper.getWritableDatabase();
                        SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();
                        String account = username.getText().toString();
                        db.delete("loginuser","account=?", new String[]{account});
                        Toast.makeText(view.getContext(),"账户注销成功",Toast.LENGTH_SHORT).show();
                        username.setText("立即注册");
                        break;
                    default:
                        break;
                }
            }
        });

        //设置账户登录点击事件
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("立即注册")) {
                    intent = new Intent(view.getContext(), RegisterAccount.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        //读取用户信息
        mydatabaseHelper = new MydatabaseHelper(view.getContext(), "book.db", null, 1);
        mydatabaseHelper.getWritableDatabase();
        SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("loginuser", null, null, null, null, null, null);
        Log.d("TAG", "onResume");
        if (cursor.getCount() == 0) {
            username.setText("立即注册");
        } else {
            cursor.moveToFirst();
            String buf = cursor.getString((cursor.getColumnIndex("account")));
            username.setText(buf);
        }
        cursor.close();

        super.onResume();

    }

    public List<Map<String, Object>> getData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }

    //GridView刷新界面
    public void refreshGridView() {
        gview = (GridView) view.findViewById(R.id.meGridView);
        //textView = (TextView) view.findViewById(R.id.GVBookName);
        //textView.setTextColor(Color.parseColor("#000000"));
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String[] from = {"image", "text"};
        int[] to = {R.id.GVBookImage, R.id.GVBookName};
        sim_adapter = new SimpleAdapter(view.getContext(), data_list, R.layout.book_item, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
    }
}
