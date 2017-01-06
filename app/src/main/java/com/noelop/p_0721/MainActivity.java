package com.noelop.p_0721;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    WifiManager wifi;
    ListView lv;
    TextView textStatus;
    Button buttonScan,btnchart,XYlocation;
    int size = 0;
    List<ScanResult> results;
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String,String>>();
    SimpleAdapter adapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textStatus = (TextView) findViewById(R.id.textView2);
        buttonScan = (Button) findViewById(R.id.button1);
        btnchart = (Button) findViewById(R.id.btnchart);
        XYlocation = (Button)findViewById(R.id.btnxylocation);
        lv = (ListView) findViewById(R.id.listView1);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if(wifi.isWifiEnabled()==false)
        {
            wifi.setWifiEnabled(true);
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
        }


        adapter = new SimpleAdapter(MainActivity.this, arraylist, R.layout.list
                , new String[] {"power","bssid","ssid"}
                , new int[] { R.id.power,R.id.bssid,R.id.ssid});
        lv.setAdapter(adapter);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                results = wifi.getScanResults();
                size = results.size();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


        buttonScan.setOnClickListener(btnscanlsner);
        btnchart.setOnClickListener(btnIntenchange);
        XYlocation.setOnClickListener(btnIntenchange);
        lv.setOnItemLongClickListener(lvlongcliner);
    }



    private Button.OnClickListener btnscanlsner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            arraylist.clear();
            wifi.startScan();
            // 然後發出掃描需求
            Toast.makeText(MainActivity.this, "Scanning..."+size, Toast.LENGTH_LONG).show();
            try {
                size = size -1;
                while(size >= 0)
                {
                    HashMap<String, String> item = new HashMap<String, String>();

                    item.put("power", new String(results.get(size).level + " dBm"));
                    //The detected signal level in dBm, also known as the RSSI.
                    item.put("bssid", results.get(size).BSSID);
                    //The address of the access point.
                    item.put("ssid", results.get(size).SSID);
                    //The network name.
                    arraylist.add(item);
                    adapter.notifyDataSetChanged();
                    size--;
                }
                Collections.sort(arraylist, new Comparator<HashMap<String, String>>() {

                    @Override
                    public int compare(HashMap<String, String> lhs,
                                       HashMap<String, String> rhs) {
                        // TODO Auto-generated method stub
                        return ((String) lhs.get("power")).compareTo((String) rhs.get("power"));
                    }
                });
                textStatus.setText(arraylist.get(0).get("ssid"));
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    };

    private Button.OnClickListener btnIntenchange= new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnchart:
                    Intent intent = new Intent(MainActivity.this,AchartengineActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnxylocation:
                    Intent intent2 = new Intent(MainActivity.this,XYloactionActivity.class);
                    startActivity(intent2);
                    break;

            }


        }
    };


    private ListView.OnItemLongClickListener lvlongcliner = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            if (((ListView)parent).getTag() != null){
                ((View)((ListView)parent).getTag()).setBackgroundDrawable(null);
            }
            ((ListView)parent).setTag(view);
            view.setBackgroundColor(Color.RED);
            //Toast.makeText(MainActivity.this, parent.toString() +"\n"+ view +"\n"+ position +"\n"+ id, Toast.LENGTH_LONG).show();




            return false;
        }
    };

}