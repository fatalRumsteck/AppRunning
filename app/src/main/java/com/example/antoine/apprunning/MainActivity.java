package com.example.antoine.apprunning;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


import static java.lang.System.out;



public class MainActivity extends AppCompatActivity{

    TextView text;
    TextView compteur;
    Button btStart;
    Button btStop;
    Button btSend;
    ListView lv;


    MeasureRecorder rec = new MeasureRecorder();

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location loc) {
            text.setText("Lat "+loc.getLatitude()+" ; Lon "+loc.getLongitude());
            compteur.setText(String.valueOf(loc.getTime()));
            if (rec.isRecording){
                rec.add(new Mesure(loc.getLongitude(),loc.getLatitude(),loc.getAltitude(),String.valueOf(loc.getTime())));
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
        @Override
        public void onProviderEnabled(String s) {

        }
        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationManager mLocationManager;

        text = (TextView) findViewById(R.id.texts);
        compteur = (TextView) findViewById(R.id.cpt);
        lv = (ListView) findViewById(R.id.lvTrajets);

        btSend = (Button) findViewById(R.id.btSend);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rec.isRecording){
                    Toast.makeText(getApplicationContext(),"En cours d'enregistrement !",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Tentative d'envoi !",Toast.LENGTH_SHORT).show();
                    new MyAsync().execute();
                }
            }
        });
        btStart=(Button) findViewById(R.id.btStart);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rec.isRecording){
                    Toast.makeText(getApplicationContext(),"Déjà en train de suivre le trajet !",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Début de l'enregistrement !",Toast.LENGTH_SHORT).show();
                    rec.start("admin");
                }
            }
        });

        btStop=(Button) findViewById(R.id.btStop);
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rec.isRecording){
                    Toast.makeText(getApplicationContext(),"Arrêt de l'enregistrement !",Toast.LENGTH_SHORT).show();
                    ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list,rec.stop(getApplicationContext()));
                    lv.setAdapter(adapter);
                }else{
                    Toast.makeText(getApplicationContext(),"Aucun trajet en cours d'enregistrement !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if(checkLocationPermission() ){
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1,mLocationListener);
        }else{
            out.println("Unable");
        }
    }

    public class MyAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Socket fromServer = new Socket("192.168.0.104",2009);
                ObjectOutputStream oos = new ObjectOutputStream(fromServer.getOutputStream());
                oos.writeObject(rec);
                rec = new MeasureRecorder();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
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
}
