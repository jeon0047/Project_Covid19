package com.cst2335.project_covid19;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

public class query_result extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    MyListAdapter my_Adapter;
    ListView resultListView;
    ArrayList<Covid_Json> covid_case_List = new ArrayList<>();
    TextView covid_cases;
    String arg_country, q_country, country, case_num, case_date;
    ProgressBar progressBar;
    Context context = this;
    JsonFetcher fetch;
    String jsonString = "{\"title\": \"how to \","
            + "\"url\": \"https:\","
            + "\"draft\": false,"
            + "\"star\": 10"
            + "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = findViewById(R.id.navi_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.hambuger);
        navigationView.setNavigationItemSelectedListener(this);

        resultListView = findViewById(R.id.listview);
        resultListView.setAdapter( my_Adapter = new MyListAdapter());

        //String res_url = "https://api.covid19api.com/country/canada/status/confirmed/live?from=2022-07-%2014T00:00:00Z&to=2022-10-15T00:00:00Z";

        Intent thisIntent = getIntent();
        arg_country = thisIntent.getStringExtra("arg_con");
        //Toast.makeText(this, arg_country, Toast.LENGTH_LONG).show();
        String res_url = "https://api.covid19api.com/country/"+arg_country+"/status/confirmed/live?from=2022-07-%2014T00:00:00Z&to=2022-10-15T00:00:00Z";
        Toast.makeText(this, res_url, Toast.LENGTH_LONG).show();
        Log.d("URL", res_url);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        fetch = new JsonFetcher();
        fetch.execute(res_url);
    }


    private class MyListAdapter extends BaseAdapter {
        public int getCount() {
            return covid_case_List.size();
        }

        public Covid_Json getItem(int position) {
            return covid_case_List.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.covid_list, parent, false);

            covid_cases = newView.findViewById(R.id.case_data);
            covid_cases.setText( getItem(position).getCountry() +" " + getItem(position).getCase_date().substring(0,10) +" " + getItem(position).getCase_num() );

            Log.d("MyListAdapter", getItem(position).getCountry() + getItem(position).getCase_num() + getItem(position).getCase_date());

            return newView;
        }
    }

    private class JsonFetcher extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... args) {

            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                if(response == null){
                    return "dataNotFetched";
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder strBuilder = new StringBuilder();

                String line = null;
                while( (line = reader.readLine()) != null ) {
                    strBuilder.append(line).append("\n");
                }
                return strBuilder.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.setVisibility(View.INVISIBLE);

            if( s != null && s.equalsIgnoreCase("Data not fetched") ) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                Log.d("JSON", "NO Data fetch");
            }
            else {
                try {
                    Log.d("JSON", "Data fetch");
                    JSONArray jsonArray = new JSONArray(s);

                    for (int i=0; i < jsonArray.length(); i++) {
                        JSONObject a_case = jsonArray.getJSONObject(i);
                        country = a_case.getString("Country");
                        case_num = a_case.getString("Cases");
                        case_date = a_case.getString("Date");

                        covid_case_List.add(new Covid_Json(country, case_num, case_date));
                        my_Adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
                Intent home = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(home);
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
                break;
            case R.id.help:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("Help")
                        .setMessage("Query result screen")
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
                Intent home = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(home);
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
                break;
            case R.id.help:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("Help")
                        .setMessage("Query result screen")
                        .setNegativeButton(getString(R.string.close), (click, arg) -> {})
                        .create().show();
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.navi_drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}