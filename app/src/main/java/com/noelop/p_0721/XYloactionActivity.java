package com.noelop.p_0721;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noelop135899 on 2016/10/13.
 */

public class XYloactionActivity extends Activity {
    TextView wificirclecenter_1,wificirclecenter_2,wificirclecenter_3;
    ListView TplLacationAns,TriwifiList;
    Button button_1,button_2;
    List<ScanResult> results;
    List<WifiInfo> infos;
    SimpleAdapter adapter,adapter_2;
    private int size;
    private double z,i,j,l,m,n,dis1,dis2,dis3;
    private double A[] = new double[]{-1.8,3.0};
    private double B[] = new double[]{1.8,-4.0};
    private double C[] = new double[]{-2.0,-6.5};
    private double D[] = new double[]{-1.8,3.0};
    private String s1,s2,s3,d1,d2,d3,n1,n2,n3,m1="c0:34:b4:10:2e:f6",m2="c0:34:b4:10:25:8d",
    m3="c0:34:b4:10:1d:ee",m4="c0:34:b4:10:25:d6";;

    WifiInfo wifiInfo;
    WifiManager wifi;
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String,String>>();
    ArrayList<HashMap<String, String>> loactiolist = new ArrayList<HashMap<String,String>>();
    ArrayList<HashMap<String, String>> triwifiarray = new ArrayList<HashMap<String,String>>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xylocation);

        button_1 = (Button) findViewById(R.id.button3);
        button_2 = (Button)findViewById(R.id.button4);
        TplLacationAns = (ListView) findViewById(R.id.TplLacationAns);
        TriwifiList = (ListView) findViewById(R.id.TriwifiList);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                results = wifi.getScanResults();
                size = results.size();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


        adapter = new SimpleAdapter(XYloactionActivity.this, loactiolist, R.layout.locans
                , new String[] {"intersect","tplans","location"}
                , new int[] { R.id.intersect,R.id.tplans,R.id.location});
        TplLacationAns.setAdapter(adapter);

        adapter_2 = new SimpleAdapter(XYloactionActivity.this, triwifiarray, R.layout.triwifilayout
                , new String[] {"cirwifi_1","cirwifi_2","cirwifi_3","cridbp_1","cridbp_2","cridbp_3",
                "cridis_1","cridis_2","cridis_3"}
                , new int[] {R.id.cirwifi_1,R.id.cirwifi_2,R.id.cirwifi_3,
                R.id.cridbp_1,R.id.cridbp_2,R.id.cridbp_3,
                R.id.cridis_1,R.id.cridis_2,R.id.cridis_3,});
        TriwifiList.setAdapter(adapter_2);

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TriWifiCircle();
                addans();
                addtricir();
            }
        });
    }
    public void addtricir(){
        HashMap<String, String> item＿tricir = new HashMap<String, String>();
        item＿tricir.put("cirwifi_1",n1);
        item＿tricir.put("cirwifi_2",n2);
        item＿tricir.put("cirwifi_3",n3);
        item＿tricir.put("cridbp_1",d1);
        item＿tricir.put("cridbp_2",d2);
        item＿tricir.put("cridbp_3",d3);
        item＿tricir.put("cridis_1",String.valueOf(Math.rint(dis1*10)/10));
        item＿tricir.put("cridis_2",String.valueOf(Math.rint(dis2*10)/10));
        item＿tricir.put("cridis_3",String.valueOf(Math.rint(dis3*10)/10));
        triwifiarray.add(0,item＿tricir);
        adapter_2.notifyDataSetChanged();

    }

    public void addans(){
        //loactiolist.clear();
        double ans[] = new double[2];
        double a,b;
        HashMap<String, String> item_tpl = new HashMap<String, String>();
        i++;
        try {
            dis1=dbmtodistance(d1);
            dis2=dbmtodistance(d2);
            dis3=dbmtodistance(d3);
            TPL tpl = new TPL(switchposition(s1), switchposition(s2), switchposition(s3),
                    dis1, dis2, dis3);
            ans = tpl.showTPL();
            item_tpl.put("tplans", "TPL in :" + i);
            item_tpl.put("location", "(" + Math.rint(ans[0]*100)/100 + "," + Math.rint(ans[1]*100)/100 + ")");
            item_tpl.put("intersect","相交");
        }catch (Exception e) {
            item_tpl.put("tplans", "TPL in :" + i);
            item_tpl.put("location", "無解");
            item_tpl.put("intersect","兩個圓不相交");
            Toast.makeText(XYloactionActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
        loactiolist.add(0,item_tpl);
        adapter.notifyDataSetChanged();
    }

    public double dbmtodistance(String s){
        //y = 53.5692 + 0.8649x  線性回歸方程式
        double d = 1.0;
        double distance;
        d= -(Double.parseDouble(s));
        if(d<=53){
            distance = 0.1;
        }else {
            distance = ((d - 53.5692)/0.8649);
        }
        System.err.println(distance);
        return distance + 5;
    }


    public double[] switchposition(String s){
        double position[] = new double[2];
        //Toast.makeText(XYloactionActivity.this,s, Toast.LENGTH_LONG).show();
        switch (s){
            case "c0:34:b4:10:2e:f6":
                position[0]=A[0];position[1]=A[1];   //AP : Neo_F6
                break;
            case "c0:34:b4:10:25:8d":
                position[0]=B[0];position[1]=B[1];   //AP : Neo_8D
                break;
            case "c0:34:b4:10:1d:ee":
                position[0]=C[0];position[1]=C[1];   //AP : Neo_EE
                break;
            case "c0:34:b4:10:25:d6":
                position[0]=D[0];position[1]=D[1];   //AP : Neo_D6
                break;
        }
        return position;
    }

    public String[] TriWifiCircle(){
        arraylist.clear();
        wifi.startScan();
        try {
            size = size -1;
            while(size >= 0)
            {
                if (results.get(size).BSSID.contains(m1)||results.get(size).BSSID.contains(m2)||
                results.get(size).BSSID.contains(m3)||results.get(size).BSSID.contains(m4)) {

                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("ssid", results.get(size).SSID);
                    item.put("dbm",String.valueOf(results.get(size).level));
                    item.put("MAC",results.get(size).BSSID);
                    arraylist.add(item);
                }
                size--;
            }
            Collections.sort(arraylist, new Comparator<HashMap<String, String>>() {

                @Override
                public int compare(HashMap<String, String> lhs,
                                   HashMap<String, String> rhs) {
                    // TODO Auto-generated method stub
                    return ((String) lhs.get("dbm")).compareTo((String) rhs.get("dbm"));
                }
            });
            double s_1[] = switchposition(arraylist.get(0).get("MAC"));
            double s_2[] = switchposition(arraylist.get(1).get("MAC"));
            double s_3[] = switchposition(arraylist.get(2).get("MAC"));
            n1=(arraylist.get(0).get("ssid")+":"+"("+s_1[0]+","+s_1[1]+")");
            n2=(arraylist.get(1).get("ssid")+":"+"("+s_2[0]+","+s_2[1]+")");
            n3=(arraylist.get(2).get("ssid")+":"+"("+s_3[0]+","+s_3[1]+")");


            d1 = arraylist.get(0).get("dbm");
            d2 = arraylist.get(1).get("dbm");
            d3 = arraylist.get(2).get("dbm");

            s1 = arraylist.get(0).get("MAC");
            s2 = arraylist.get(1).get("MAC");
            s3 = arraylist.get(2).get("MAC");


        } catch (Exception e) {
            // TODO: handle exception
        }

        return new String[]{s1,s2,s3,d1,d2,d3};
    }

}
