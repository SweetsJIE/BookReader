package com.sweetsjie.readbook;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sweets on 17/4/15.
 */

public class RegisterAccount extends Activity{
    private EditText accountText;
    private EditText passwordText;
    private Button registerButton;
    private MydatabaseHelper mydatabaseHelper;
    public static String NAME[]=new String[100];
    public static String URL[]=new String[100];
    //public static String[] NAME={"1","2"};
    private int i;
    private boolean isSaved=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);

        accountText = (EditText) findViewById(R.id.accountText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        registerButton = (Button) findViewById(R.id.registerButton);


        mydatabaseHelper = new MydatabaseHelper(this,"book.db",null,1);
        mydatabaseHelper.getWritableDatabase();
        final SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i=0;
                String name = accountText.getText().toString();
                mydatabaseHelper = new MydatabaseHelper(RegisterAccount.this,"book.db",null,1);
                mydatabaseHelper.getWritableDatabase();
                final SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();
                Cursor cursor = db.query("user", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String buf= cursor.getString((cursor.getColumnIndex("account")));
                        if (buf.equals(name)){
                            isSaved = true;
                            break;
                        }
                        i++;
                    } while (cursor.moveToNext());
                }
                cursor.close();


                if (isSaved){
                    Toast.makeText(RegisterAccount.this,"账号已注册",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    ContentValues values = new ContentValues();
                    values.put("account",accountText.getText().toString());
                    values.put("password",passwordText.getText().toString());
                    db.insert("user",null,values);
                    Toast.makeText(RegisterAccount.this,"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });
    }


}
