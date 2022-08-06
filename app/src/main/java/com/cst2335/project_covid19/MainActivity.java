package com.cst2335.project_covid19;

import java.util.ArrayList;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<Integer> randomImages = new ArrayList<Integer>();
    SharedPreferences sp;
    EditText country;
    ImageView imageView;
    Toolbar myToolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        sp = getSharedPreferences("SharedFile", MODE_PRIVATE);

        country = findViewById(R.id.country);
        country.setText(sp.getString("country", ""));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);

        drawer = findViewById(R.id.navi_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.hambuger);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.query).setOnClickListener((click) ->{
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("country", country.getText().toString()).apply();
            Intent Query_Result = new Intent(MainActivity.this, query_result.class);
            Query_Result.putExtra("arg_con", country.getText().toString());
            startActivity(Query_Result);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        switch(item.getItemId())
        {
            case R.id.home:
                this.finish();
                this.startActivity(getIntent());
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
                break;
            case R.id.help:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("Help")
                        .setMessage("Type country name")
                        .setNegativeButton(getString(R.string.close), (click, arg) -> {})
                        .create().show();
                break;
        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message = null;
        switch (item.getItemId()){
            case R.id.home:
                this.finish();
                this.startActivity(getIntent());
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
                break;

            case R.id.help:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("Help")
                        .setMessage("Type country name")
                        .setNegativeButton(getString(R.string.close), (click, arg) -> {})
                        .create().show();
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.navi_drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}