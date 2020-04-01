package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText psd,user;
    CheckBox checkBox;
    SharedPreferences sp;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        psd = findViewById(R.id.psd);
        user = findViewById(R.id.user);
        checkBox = findViewById(R.id.checkBox);

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        //判断记住密码多选框状态
        if(sp.getBoolean("ISCHECK", false))
        {
            //设置默认是记录密码状态
            checkBox.setChecked(true);
            user.setText(sp.getString("USER_NAME", ""));
            psd.setText(sp.getString("PASSWORD", ""));

        }

        //判断自动登陆多选框状态
        if(sp.getBoolean("AUTO_ISCHECK", false))
        {
            //跳转界面
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            LoginActivity.this.startActivity(intent);

        }


        // 登录监听事件  现在默认为用户名为： 密码：
        btnLogin = findViewById(R.id.btnLoad);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String userNameValue = user.getText().toString();
                String passwordValue = psd.getText().toString();
                if(userNameValue.equals(sp.getString("USER_NAME", ""))&&passwordValue.equals(sp.getString("PASSWORD", "")))
                {
                    Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
                    //登录成功和记住密码框为选中状态才保存用户信息
                    if(checkBox.isChecked())
                    {
                        //记住用户名、密码、
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USER_NAME", userNameValue);
                        editor.putString("PASSWORD",passwordValue);
                        editor.commit();
                    }
                    //跳转界面
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                    finish();

                }else{

                    Toast.makeText(LoginActivity.this,"用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
                }
            }
        });

        //监听记住密码多选框按钮事件
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {

                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                }else {

                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

    }
}
