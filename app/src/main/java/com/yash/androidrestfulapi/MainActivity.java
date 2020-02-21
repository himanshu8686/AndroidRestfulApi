package com.yash.androidrestfulapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yash.androidrestfulapi.model.CityItem;
import com.yash.androidrestfulapi.network.MyIntentService;
import com.yash.androidrestfulapi.utils.NetworkHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_code;
    private Button btn_run_code,btn_show_all;
    private boolean isNetworkOk;
    private ProgressBar progressBar_horizontal;
    public static final String JSON_URL="https://restwork.000webhostapp.com/restworkAndroidApi/json/itemsfeed.php";
    private BroadcastReceiver mReceiver=new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            CityItem[] cityItems= (CityItem[]) intent.getParcelableArrayExtra(MyIntentService.SERVICE_PAYLOAD);
           // Log.e("city array", String.valueOf(cityItems[0].getCityname()));
            if (cityItems.length!=0)
            {
                progressBar_horizontal.setVisibility(View.GONE);
            }
            for(CityItem cityItem : cityItems)
            {
                logOutput(cityItem.getCityname());
            }
        }
    };

    private void logOutput(String data)
    {
        Log.e("LogOutput",data);
        tv_code.append(data+"\n\n");
    }

    private void initializeViews()
    {
        //tv_network=findViewById(R.id.tv_network);
        tv_code=findViewById(R.id.tv_code);
        btn_run_code=findViewById(R.id.btn_run_code);
        btn_run_code.setOnClickListener(this);
        btn_show_all=findViewById(R.id.btn_show_all);
        btn_show_all.setOnClickListener(this);
        progressBar_horizontal= findViewById(R.id.progressBar_horizontal);
    }

    private void runCode()
    {
        if (isNetworkOk)
        {
            Intent intent=new Intent(MainActivity.this,MyIntentService.class);
            intent.setData(Uri.parse(JSON_URL));
            startService(intent);
            progressBar_horizontal.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(this, "Network Not Available!!!", Toast.LENGTH_SHORT).show();
        }
    }

    /*----------------------------------- Override Methods-------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        isNetworkOk= NetworkHelper.isNetworkAvailable(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkOk==true)
        {
            Toast.makeText(this, "Network available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter(MyIntentService.SERVICE_MESSAGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v)
    {
        if (v==btn_run_code)
        {
                runCode();
        }
        else if(v==btn_show_all)
        {
            listShow();
        }
    }

    private void listShow()
    {
        Intent intent=new Intent(MainActivity.this,ListActivity.class);
        startActivity(intent);
    }
}
