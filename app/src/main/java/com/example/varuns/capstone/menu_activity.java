package com.example.varuns.capstone;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.ArtisanItem;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class menu_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
         SearchView.OnQueryTextListener {

    private ListView artisanList;
    static TextView artName;
    private static ArtisanAdapter artisanAdapterGlobal;
    private Integer[] artisanImages = { R.drawable.maria, R.drawable.native5, R.drawable.native3 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
           toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        artisanList = (ListView)findViewById(R.id.artisanList);

        getArtisans();
        artisanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Artisan artisan = (Artisan) parent.getAdapter().getItem(position);
                Intent intent = new Intent(menu_activity.this, ScrollingActivity.class);
                intent.putExtra("artisanId", artisan.getArtisanId());
                artName = (TextView) view.findViewById(R.id.artisanName);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        //search submission is ignored since every text change applies filter
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        //whenever the user changes the text within the search bar, filter artisans
        artisanAdapterGlobal.getFilter().filter(query);

        return true;
    }

    String itemNameT = "Teddy Bear";
    String itemDescT = "A lovable stuffed bear";

    private String[] nameDB = {
        "Martha",
        "Maria",
        "Sofia",
        "Camila",
        "Emma",
        "Sara",
        "Gabriela",
        "Elena",
        "Victoria",
        "Emilia",
        "Natalia"
    };

    private String[] nameDBLast = {
        "Hernandez",
        "Garcia",
        "Lopez",
        "Martinez",
        "Rodriguez",
        "Gonzalez",
        "Perez",
        "Sanchez",
        "Gomez",
        "Flores",
        "Jiminez"
    };

    private String placeHolderText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";

    public void getArtisansNoDB() {
        List<Artisan> artisans = new ArrayList<Artisan>();
        //Integer artisanId, String firstName, String lastName, String bio, List<ArtisanItem> artisanItems
        for (int i = 0; i < nameDB.length; i++) {
            ArrayList<ArtisanItem> items = new ArrayList<>();
            items.add(new ArtisanItem(i, 0, itemNameT, itemDescT));
            artisans.add(new Artisan(i, nameDB[i], nameDBLast[i], placeHolderText, items));
        }

        menu_activity.ArtisanAdapter artisanAdapter = new menu_activity.ArtisanAdapter(artisans);
        artisanAdapterGlobal = artisanAdapter;
        artisanList.setAdapter(artisanAdapter);
    }

    public void getArtisans() {
        Call<RestfulResponse<List<Artisan>>> call = ApiService.artisanService().getAllArtisans();
        //handle the response
        call.enqueue(new Callback<RestfulResponse<List<Artisan>>>() {
            @Override
            public void onResponse(Call<RestfulResponse<List<Artisan>>> call, Response<RestfulResponse<List<Artisan>>> response) {
                List<Artisan> artisans = response.body().getData();
                Toast.makeText(menu_activity.this, "success", Toast.LENGTH_SHORT).show();
                menu_activity.ArtisanAdapter artisanAdapter = new menu_activity.ArtisanAdapter(artisans);
                artisanList.setAdapter(artisanAdapter);
                artisanAdapterGlobal = artisanAdapter;
            }

            @Override
            public void onFailure(Call<RestfulResponse<List<Artisan>>> call, Throwable t) {
                Toast.makeText(menu_activity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } 

        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } 

        else if (id == R.id.nav_info) {

        } 

        else if (id == R.id.nav_send) {
            Intent myintent = new Intent(menu_activity.this, Send_message.class);
            startActivity(myintent);
            return true;
        } 

        else if (id == R.id.nav_tasks) {

        } 

        else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }



    public static ArtisanAdapter getAdapter() {
        return artisanAdapterGlobal;
    }

    private static final String LOG_TAG =
            menu_activity.class.getSimpleName();

    public void addNewArtisan(View view) {
        Intent intent = new Intent(this, AddArtisanActivity.class);
        startActivity(intent);
    }

    class ArtisanAdapter extends BaseAdapter implements Filterable {

        List<Artisan> artisans;
        List<Artisan> filteredArtisans;
        private ArtisanFilter artisanFilter;

        private class ArtisanFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                //case: user has queried a value
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<Artisan> filtered = new ArrayList<>();

                    constraint = constraint.toString().toLowerCase();
                    String[] separated = ((String) constraint).split(" ");

                    // search content in friend list
                    for (Artisan a : artisans) {
                        String first = a.getFirstName().toLowerCase();
                        String last = a.getLastName().toLowerCase();

                        //user only has searched whitespace
                        if (separated.length == 0)
                            break;

                        boolean firstValid = true, lastValid = true;
                        for (int i = 0; i < separated[0].length(); i++) {
                            //check first whitespace separated query against first name
                            if (first.length() <= i || 
                                (separated[0].charAt(i) != first.charAt(i))) {
                                firstValid = false;
                            }

                            //check first whitespace separated query against last name
                            if (last.length() <= i || 
                                (separated[0].charAt(i) != last.charAt(i))) {
                                lastValid = false;
                            }
                        }

                        //user searched 2 words - this assumes second word is last name
                        if (separated.length >= 2) {
                            lastValid = true;
                            for (int i = 0; i < separated[1].length(); i++) {
                                //check first whitespace separated query against last name
                                if (last.length() <= i || 
                                    (separated[1].charAt(i) != last.charAt(i))) {
                                    lastValid = false;
                                }
                            }
                        }

                        if (firstValid || lastValid) {
                            filtered.add(a);
                        }
                    }

                    filterResults.count = filtered.size();
                    filterResults.values = filtered;
                }

                //case: search query is empty
                else {
                    filterResults.count = artisans.size();
                    filterResults.values = artisans;
                }

                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredArtisans = (ArrayList<Artisan>) results.values;
                notifyDataSetChanged();
            }
        }

        @Override
        public Filter getFilter() {
            if (artisanFilter == null) {
                artisanFilter = new ArtisanFilter();
            }

            return artisanFilter;
        }

        public ArtisanAdapter(List<Artisan> artisans) {
            this.artisans = artisans;
            this.filteredArtisans = artisans;
            getFilter();
        }

        public void addArtisan(Artisan a) {
            artisans.add(a);
        }

        public List<Artisan> getArtisans() {
            return artisans;
        }

        public int getCount() {
            return filteredArtisans.size();
        }

        public Artisan getItem(int i) {
            return filteredArtisans.get(i);
        }

        public long getItemId(int i) {
            return 0;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.artisan_list_layout, null);
            ImageView artisanImage = (ImageView)view.findViewById(R.id.artisanImage);

            TextView artisanName = (TextView)view.findViewById(R.id.artisanName);
            artisanName.setText(filteredArtisans.get(i).getFirstName() + " " + filteredArtisans.get(i).getLastName());
            artisanImage.setImageResource(artisanImages[i%3]);

            return view;
        }
    }
}
