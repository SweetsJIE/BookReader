package com.sweetsjie.readbook;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.idescout.sql.SqlScoutServer;

public class MainActivity extends AppCompatActivity {

    private HomeContentFragment homeContentFragment;
    private MeContentFragment meContentFragment;
    private SearchContentFragment searchContentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        SqlScoutServer.create(this,getPackageName());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //初始化默认Fragment
        setDefaultFragment();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //从新加载Fragment
        setDefaultFragment();
    }

    //BottomNavigationButtom监听事件
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    homeContentFragment = new HomeContentFragment();
                    transaction.replace(R.id.content,homeContentFragment);
                    break;
                case R.id.navigation_search:
                    searchContentFragment = new SearchContentFragment();
                    transaction.replace(R.id.content,searchContentFragment);
                    break;
                case R.id.navigation_me:
                    meContentFragment = new MeContentFragment();
                    transaction.replace(R.id.content,meContentFragment);
                    break;
                default:
                    return false;
            }
            transaction.commit();
            return true;
        }

    };

    public void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        homeContentFragment = new HomeContentFragment();
        transaction.replace(R.id.content, homeContentFragment);
        transaction.commit();
    }

}
