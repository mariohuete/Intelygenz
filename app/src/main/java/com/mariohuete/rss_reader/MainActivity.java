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
import com.mariohuete.rss_reader.utils.Connect;
import com.mariohuete.rss_reader.utils.StoreData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
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
    private List<Model> modelList = new ArrayList<>();
    private ModelAdapter adapter;
    private ProgressDialog pd;
    private Intent intent;
    @InjectView(R.id.listView) protected ListView lv;
    @InjectView(R.id.editTxt) protected EditText editTxt;


    //METHODS:
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        if(Connect.isOnline(this))
            requestData();
        else {
            pd.dismiss();
            Connect.showToast(this);
            restoreData();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void requestData() {
        // Use retrofit to get json data
        RestAdapter restAdapter = new RestAdapter.Builder().
                setEndpoint(getString(R.string.end_point)).build();
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
        Collections.sort(listOnline, Model.ByNameComparator);
        // Set adapter for listView
        adapter = new ModelAdapter(listOnline, this);
        lv.setAdapter(adapter);
        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                // Save data to retrieve it when there's no internet connection
                saveData(listOnline.get(position));
                // Start new activity with details
                intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("mName", listOnline.get(position).getName());
                intent.putExtra("mDesc", listOnline.get(position).getInstructions());
                intent.putExtra("mPhot", listOnline.get(position).getPhoto());
                //startActivity(intent);
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

    public void saveData(Model mdl) {
        // If folder doesn't exist -> create
        if(!StoreData.folderExists()) {
            StoreData.createFolder();
        }
        // Save de item in sd card
        saveModel(mdl);
    }

    public void restoreData() {
        // Show all data stored in sd card
        pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.reloading));
        pd.setCancelable(false);
        pd.show();
        modelList = restoreModel(new File(StoreData.FOLDER_DIR));
        Collections.sort(modelList, Model.ByNameComparator);
        // Set adapter for listView
        adapter = new ModelAdapter(modelList, this);
        lv.setAdapter(adapter);
        updateList(modelList);
        pd.dismiss();
    }

    public void saveModel(Model mdl) {
        // Save item in sd card
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(StoreData.FOLDER_DIR+"/"+mdl.getName()+".bin")));
            oos.writeObject(mdl); // write the class as an 'object'
            oos.flush(); // flush the stream to insure all of the information was written to 'save_object.bin'
            oos.close();// close the stream
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Model> restoreModel(File f) {
        // Retrieve all items stored in sd card
        File list[] = f.listFiles();
        ArrayList<Model> restoredList = new ArrayList<>();
        ObjectInputStream ois;
        Model o;
        try {
            for (File aList : list) {
                ois = new ObjectInputStream(new FileInputStream(aList));
                o = (Model) ois.readObject();
                restoredList.add(o);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return restoredList;
    }
}
