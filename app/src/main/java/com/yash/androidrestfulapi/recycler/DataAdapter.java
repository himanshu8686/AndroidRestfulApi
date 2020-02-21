package com.yash.androidrestfulapi.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yash.androidrestfulapi.R;
import com.yash.androidrestfulapi.model.CityItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>
{
    private Context context;
    private List<CityItem> cityItemsList;
    private Map<String,Bitmap> mBitmaps=new HashMap<>();

    public DataAdapter(Context context, List<CityItem> cityItemList) {
        this.context = context;
        this.cityItemsList = cityItemList;
    }


    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view=LayoutInflater.from(context).inflate(R.layout.card_view_layout,parent,false);
        DataViewHolder holder =new DataViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        CityItem cityItem=cityItemsList.get(position);
        holder.text_view_note.setText(cityItem.getCityname());

        if (mBitmaps.containsKey(cityItem.getCityname()))
        {
            holder.img_view.setImageBitmap(mBitmaps.get(cityItem.getCityname()));
        }
        else {
            MyImageTask task=new MyImageTask();
            task.setViewHolder(holder);
            task.execute(cityItem);
        }
    }

    @Override
    public int getItemCount() {
        return cityItemsList.size();
    }

    /**
     * view holder class
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_view_note;
        ImageView img_view;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            this.text_view_note=itemView.findViewById(R.id.text_view_note);
            this.img_view=itemView.findViewById(R.id.img_view);
        }
    }


    /**
     *
     */
    class MyImageTask extends AsyncTask<CityItem,Void,Bitmap>
    {
        private static final String PHOTO_BASE_URL = "https://restwork.000webhostapp.com/restworkAndroidApi/images/";
        private CityItem cityItem;
        private DataViewHolder dataViewHolder;

        public void setViewHolder(DataViewHolder dataViewHolder)
        {
            this.dataViewHolder=dataViewHolder;
        }

        @Override
        protected Bitmap doInBackground(CityItem... cityItems)
        {
        Bitmap bitmap1=null;
        cityItem=cityItems[0];

            String imageurl=PHOTO_BASE_URL+cityItem.getImage();
            System.out.println(imageurl);
            InputStream inputStream=null;
            try {
                URL imageUrl=new URL(imageurl);
                inputStream= (InputStream) imageUrl.getContent();
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                bitmap1=Bitmap.createScaledBitmap(bitmap,300,300,false);
                // map.put(item.getCityname(),bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (inputStream!=null)
                {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("TAG","doInBackground: Image downloaded: "+imageurl);
            return bitmap1;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dataViewHolder.img_view.setImageBitmap(bitmap);
            mBitmaps.put(cityItem.getCityname(),bitmap);
        }
    }
}
