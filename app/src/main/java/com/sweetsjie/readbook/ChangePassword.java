package com.sweetsjie.readbook;

import android.app.Activity;
import android.content.ContentValues;
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

public class ChangePassword extends Activity{
    private EditText passwordText1;
    private EditText passwordText2;
    private Button changePasswordButton;
    private MydatabaseHelper mydatabaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_password);

        passwordText1 = (EditText) findViewById(R.id.changepasswordText1);
        passwordText2 = (EditText) findViewById(R.id.changepasswordText2);
        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);

        mydatabaseHelper = new MydatabaseHelper(this,"book.db",null,1);
        mydatabaseHelper.getWritableDatabase();
        final SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordText1.getText().toString().equals(passwordText2.getText().toString())){
                    ContentValues values = new ContentValues();
                    values.put("password",passwordText1.getText().toString());
                    db.update("user",values,"account=?",new  String[]{"jie"});
                    Toast.makeText(ChangePassword.this,"密码更改成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(ChangePassword.this,"两次输入密码不正确",Toast.LENGTH_SHORT).show();
                    passwordText1.setText("");
                    passwordText2.setText("");
                }
            }
        });

    }
}
