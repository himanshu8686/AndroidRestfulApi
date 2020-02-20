package com.yash.androidrestfulapi.network;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.yash.androidrestfulapi.model.CityItem;
import com.yash.androidrestfulapi.utils.HttpHelper;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyIntentService extends IntentService {


    public static final String SERVICE_PAYLOAD = "SERVICE_PAYLOAD";
    public static final String SERVICE_MESSAGE = "SERVICE_MESSAGE";
    private String data;
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Uri uri=intent.getData();

        try {
            data=HttpHelper.downloadUrl(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
            //data=e.getMessage();
            return;
        }

        Gson gson=new Gson();
        CityItem[] cityItems=gson.fromJson(data,CityItem[].class);

        sendMessageToUi(cityItems);

    }

    private void sendMessageToUi(CityItem[] data)
    {
        Intent intent=new Intent(SERVICE_MESSAGE);
        intent.putExtra(SERVICE_PAYLOAD,data);
        LocalBroadcastManager.getInstance(this)
        .sendBroadcast(intent);
    }
}
