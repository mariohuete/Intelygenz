package com.mariohuete.rss_reader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.mariohuete.rss_reader.adapters.ModelAdapter;
import com.mariohuete.rss_reader.models.Model;
import com.mariohuete.rss_reader.utils.Api;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by mariobama on 09/02/15.
 */
public class MainActivity extends ActionBarActivity {
    //ATTRIBUTES:
    private List<Model> modelList = new ArrayList<Model>();
    private ModelAdapter adapter;
    public static final String END_POINT = "http://services.hanselandpetal.com";
    private ProgressDialog pd;
    private Intent intent;
    @InjectView(R.id.listView) protected ListView lv;
    @InjectView(R.id.editTxt) protected EditText editTxt;


    //METHODS:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.loading));
        pd.setCancelable(false);
        pd.show();
        ButterKnife.inject(this);
        initList();
    }

    private void initList() {
        // If there's internet connection, get info from json
        // if not, try to get data stored in sd card
        //if(Connec.isOnline(this))
            requestData();
        /*else {
            pd.dismiss();
            restoreData();
        }*/
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void requestData() {
        // Use retrofit to get json data
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(END_POINT).build();
        Api api = restAdapter.create(Api.class);
        api.getList(new Callback<List<Model>>() {
            @Override
            public void success(List<Model> models, Response response) {
                modelList = models;
                updateList(modelList);
                pd.dismiss();
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    public void updateList(final List<Model> listOnline) {
        // Sort the items alphabetically cause I have no date
        //Collections.sort(listOnline, Model.ByNameComparator);
        // Set adapter for listView
        adapter = new ModelAdapter(listOnline, this);
        lv.setAdapter(adapter);
        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                // Save data to retrieve it when there's no internet connection
                //saveData(listOnline.get(position));
                // Start new activity with details
                /*intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("mName", listOnline.get(position).getName());
                intent.putExtra("mDesc", listOnline.get(position).getInstructions());
                intent.putExtra("mPhot", listOnline.get(position).getPhoto());
                startActivity(intent);*/
            }
        });
        // TextFilter for search by title
        lv.setTextFilterEnabled(true);

        // Listener for text searching
        editTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    adapter.resetData();
                }
                adapter.getFilter().filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }
}
