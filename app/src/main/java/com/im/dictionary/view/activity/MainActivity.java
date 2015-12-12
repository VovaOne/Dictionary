package com.im.dictionary.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.im.dictionary.App;
import com.im.dictionary.R;
import com.im.dictionary.service.NotificationService;
import com.im.dictionary.view.adapter.TabAdapter;
import com.im.dictionary.view.dialog.WordDialog;
import com.im.dictionary.model.Card;

import java.util.concurrent.TimeUnit;

import static com.im.dictionary.service.NotificationService.SERVICE_POWER_MODE_SHARED_PREFERENCE_PROPERTY;
import static com.im.dictionary.view.dialog.WordDialog.ADDED_CARD_INTENT;
import static com.im.dictionary.view.dialog.WordDialog.ADDED_CARD_ID_EXTRA;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    public static final String SEARCH_CARD_INTENT = "com.im.dictionary.card.search.intent";
    public static final String SEARCH_CARDS_EXTRA_PARAM = "com.im.dictionary.card.search.param.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initEvents();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        checkAndMayStartNotificationService();
    }

    private void checkAndMayStartNotificationService() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        // first boot
        if (!sharedPref.contains(SERVICE_POWER_MODE_SHARED_PREFERENCE_PROPERTY)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SERVICE_POWER_MODE_SHARED_PREFERENCE_PROPERTY, true);
            editor.apply();
            startService(new Intent(App.getAppContext(), NotificationService.class));
            return;
        }

        boolean needToStartService = sharedPref.getBoolean(SERVICE_POWER_MODE_SHARED_PREFERENCE_PROPERTY, true);
        if (needToStartService)
            startService(new Intent(App.getAppContext(), NotificationService.class));

    }

    private void initEvents() {
        initToolbar();
        initNavigationPanel();
        initActionButton();
        initSwitchService();
    }

    private void initSwitchService() {

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean notificationSwitchPosition = sharedPref.getBoolean(SERVICE_POWER_MODE_SHARED_PREFERENCE_PROPERTY, true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        Switch serviceSwitch = (Switch) header.findViewById(R.id.start_stop_service_switch);
        serviceSwitch.setChecked(notificationSwitchPosition);

        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(SERVICE_POWER_MODE_SHARED_PREFERENCE_PROPERTY, isChecked);
                editor.apply();

                if (isChecked) {
                    startService(new Intent(App.getAppContext(), NotificationService.class));
                } else {
                    stopService(new Intent(App.getAppContext(), NotificationService.class));
                }
            }
        });
    }

    private void initActionButton() {
        final Context context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final WordDialog wordDialog = new WordDialog(context);
                wordDialog.setItemAddedCallback(new WordDialog.ItemAdded() {
                    @Override
                    public void apply(final Card card) {
                        Intent intent = new Intent(ADDED_CARD_INTENT);
                        intent.putExtra(ADDED_CARD_ID_EXTRA, card);
                        LocalBroadcastManager.getInstance(App.getAppContext()).sendBroadcast(intent);

                        Snackbar
                                .make(findViewById(R.id.coordinator), R.string.card_added, Snackbar.LENGTH_LONG)
                                .setAction(R.string.edit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        wordDialog.show(card);
                                    }
                                }).show();
                    }
                });
                wordDialog.show();

            }
        });
    }

    private void initNavigationPanel() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String searchParam) {
                if (waitInput != null) waitInput.cancel(true);
                waitInput = new WaitInput(new WaitInput.OnTaskCompleted() {
                    @Override
                    public void apply() {
                        Intent intent = new Intent(SEARCH_CARD_INTENT);
                        intent.putExtra(SEARCH_CARDS_EXTRA_PARAM, searchParam);
                        LocalBroadcastManager.getInstance(App.getAppContext()).sendBroadcast(intent);
                    }
                });
                waitInput.execute();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private WaitInput waitInput;

    private static class WaitInput extends AsyncTask<String, Void, Integer> {

        interface OnTaskCompleted {
            void apply();
        }

        private OnTaskCompleted callback;

        public WaitInput(OnTaskCompleted callback) {
            this.callback = callback;
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {
                return 0;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (!isCancelled() && integer == 1) callback.apply();

        }
    }

}
