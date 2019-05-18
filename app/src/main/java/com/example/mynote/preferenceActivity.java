package com.example.mynote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class preferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(initData.getBgstyle());
        setContentView(R.layout.activity_preference);
    }


    @Override
    protected void onResume() {
        super.onResume();
//加载设置界面
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new Setting())
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



//顶部菜单栏加载
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_preference,menu);
        return true;
    }

    //顶部菜单栏点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_Save:
                Save();
                break;
        }
        return true;
    }


    //保存按钮事件
    void Save(){
        Intent intent= new Intent(preferenceActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
