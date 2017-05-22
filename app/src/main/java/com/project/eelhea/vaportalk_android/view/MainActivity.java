package com.project.eelhea.vaportalk_android.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.eelhea.vaportalk_android.AddFriendActivity;
import com.project.eelhea.vaportalk_android.CameraStartActivity;
import com.project.eelhea.vaportalk_android.contract.MainContract;
import com.project.eelhea.vaportalk_android.adapter.MainPagerAdapter;
import com.project.eelhea.vaportalk_android.R;
import com.project.eelhea.vaportalk_android.presenter.MainPresenter;

/**
 * Created by eelhea on 2017. 4. 3..
 */

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private static final String TAG = "MainActivity";

    private TabLayout bottom_tablayout;
    private ViewPager main_viewpager;
    private MainPagerAdapter mainPagerAdapter;

    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter();
        presenter.attachView(this);

        Toolbar toolbar_main = (Toolbar)findViewById(R.id.toolbar_tab_main);
        setSupportActionBar(toolbar_main);
        toolbar_main.setNavigationIcon(R.drawable.ic_send);
        toolbar_main.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraStartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "Handlee-Regular.ttf");
        TextView tv_title = (TextView)findViewById(R.id.tv_toolbar_title);
        tv_title.setTypeface(typeface);

        bottom_tablayout = (TabLayout)findViewById(R.id.bottom_tablayout);
        main_viewpager = (ViewPager)findViewById(R.id.main_viewpager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), bottom_tablayout.getTabCount());
        main_viewpager.setAdapter(mainPagerAdapter);
        main_viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(bottom_tablayout));
        bottom_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                main_viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addfriend_icon){
            Intent intent = new Intent(getApplicationContext(), AddFriendActivity.class);
            startActivity(intent);
            finish();

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
