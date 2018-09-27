package com.fouomene.popularmovies.app;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.fouomene.popularmovies.app.utils.Utility;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // to display Icon launcher
        ActionBar actionBar = getSupportActionBar();
        //icon back
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_title_main);


        TextView sortOrd = (TextView) findViewById(R.id.action_bar_sort_order);

        if ("mostpopular".equals(Utility.getPreferredSortOrder(getApplicationContext()))) {
            sortOrd.setText(getResources().getString(R.string.most_popular));
        }else{
            sortOrd.setText(getResources().getString(R.string.top_rated));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_most_popular) {

            Utility.setPreferredSortOrder(getApplicationContext(),"mostpopular");
            Utility.setPreferredCurrentVisiblePosition(getApplicationContext(),"0");
            finish();
            startActivity(getIntent());

            return true;
        }

        if (id == R.id.action_top_rated) {

            Utility.setPreferredSortOrder(getApplicationContext(),"toprated");
            Utility.setPreferredCurrentVisiblePosition(getApplicationContext(),"0");
            finish();
            startActivity(getIntent());

            return true;
        }

        if (id == R.id.action_favorite) {
            Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
