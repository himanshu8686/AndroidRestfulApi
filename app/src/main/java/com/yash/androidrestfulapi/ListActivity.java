package com.yash.androidrestfulapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yash.androidrestfulapi.model.CityItem;
import com.yash.androidrestfulapi.network.MyIntentService;
import com.yash.androidrestfulapi.recycler.DataAdapter;
import com.yash.androidrestfulapi.utils.NetworkHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yash.androidrestfulapi.MainActivity.JSON_URL;

public class ListActivity extends AppCompatActivity
{

    private RecyclerView recylcer_view;
    private DataAdapter dataAdapter;
    private List<CityItem> cityItemsList;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar_horizontal;
    private SwipeRefreshLayout swipe_refresh;
    private Map<String, Bitmap> mBitmaps;

    private BroadcastReceiver mReceiver=new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            CityItem[] cityItems= (CityItem[]) intent.getParcelableArrayExtra(MyIntentService.SERVICE_PAYLOAD);
            if (cityItems.length!=0)
            {
                progressBar_horizontal.setVisibility(View.GONE);
            }
            //Log.e("List activity",cityItems[0].getCityname());
            cityItemsList=Arrays.asList(cityItems);
            //  Log.e("lists",""+cityItemsList);
            Collections.shuffle(cityItemsList);
            showRecyclerData();
            dataAdapter.notifyDataSetChanged();
            swipe_refresh.setRefreshing(false);
//            Log.e("lists",cityItemsList.toString());
        }
    };

    private void showRecyclerData() {
        dataAdapter=new DataAdapter(this,cityItemsList);
        recylcer_view.setAdapter(dataAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initializeViews();

        recylcer_view.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recylcer_view.setLayoutManager(layoutManager);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkHelper.isNetworkAvailable(ListActivity.this))
                {
                    startIntentService();
                }
                else{
                    Toast.makeText(ListActivity.this, "No Internet Available", Toast.LENGTH_LONG).show();
                    swipe_refresh.setRefreshing(false);
                }
            }
        });

        if(NetworkHelper.isNetworkAvailable(ListActivity.this))
        {
            startIntentService();
        }
        else{
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.e("onStart","called");
        IntentFilter intentFilter=new IntentFilter(MyIntentService.SERVICE_MESSAGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private void startIntentService() {
        Intent intent=new Intent(ListActivity.this, MyIntentService.class);
        intent.setData(Uri.parse(JSON_URL));
        startService(intent);
        progressBar_horizontal.setVisibility(View.VISIBLE);
    }

    private void initializeViews()
    {
        recylcer_view= findViewById(R.id.recylcer_view);
        progressBar_horizontal=findViewById(R.id.progressBar_horizontal);
        swipe_refresh=findViewById(R.id.swipe_refresh);
    }

//    private static class MyImageTask extends AsyncTaskLoader<Map<String,Bitmap>>
//    {
//
//        private static final String PHOTO_BASE_URL="https://restwork.000webhostapp.com/restworkAndroidApi/images/";
//        private static List<CityItem> mCityList;
//
//        public MyImageTask(@NonNull Context context,List<CityItem> cityItems) {
//            super(context);
//            mCityList=cityItems;
//        }
//
//        @Nullable
//        @Override
//        public Map<String, Bitmap> loadInBackground()
//        {
//            Map<String ,Bitmap> map=new HashMap<>();
//            for (CityItem item:mCityList)
//            {
//                String imageUrlString=PHOTO_BASE_URL+item.getImage();
//                InputStream inputStream=null;
//                try {
//                    URL imageUrl=new URL(imageUrlString);
//
//                    inputStream= (InputStream) imageUrl.getContent();
//                    Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
//                    map.put(item.getCityname(),bitmap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                finally {
//                    if (inputStream!=null)
//                    {
//                        try {
//                            inputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//            return map;
//        }
//    }
}
