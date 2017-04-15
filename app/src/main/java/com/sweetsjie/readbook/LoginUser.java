package com.sweetsjie.readbook;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sweets on 17/4/15.
 */

public class LoginUser extends Activity{
    private EditText loginAccountText;
    private EditText loginPasswordText;
    private Button loginButton;
    private  MydatabaseHelper mydatabaseHelper;

    public static String ACCOUNT[]=new String[100];
    public static String PASSWORD[]=new String[100];
    private int i;
    private boolean isTrue = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginuser);

        loginAccountText = (EditText) findViewById(R.id.loginAccountText);
        loginPasswordText = (EditText) findViewById(R.id.loginPasswordText);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydatabaseHelper = new MydatabaseHelper(LoginUser.this, "book.db", null, 1);
                mydatabaseHelper.getWritableDatabase();
                SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();

                Cursor cursor = db.query("user", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String buf= cursor.getString((cursor.getColumnIndex("account")));
                        ACCOUNT[i] = buf;
                        buf= cursor.getString((cursor.getColumnIndex("password")));
                        PASSWORD[i] = buf;
                        if (ACCOUNT[i].equals(loginAccountText.getText().toString())
                                && PASSWORD[i].equals(loginPasswordText.getText().toString()))
                        {
                            isTrue = true;
                            break;
                        }
                        i++;
                    } while (cursor.moveToNext());
                }
                cursor.close();

                if (isTrue){
                    ContentValues values = new ContentValues();
                    values.put("account",loginAccountText.getText().toString());
                    values.put("password",loginPasswordText.getText().toString());
                    db.insert("loginuser",null,values);
                    Toast.makeText(LoginUser.this,"登录成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(LoginUser.this,"账户密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
