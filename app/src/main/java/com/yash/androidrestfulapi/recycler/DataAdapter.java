package com.yash.androidrestfulapi.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yash.androidrestfulapi.ListActivity;
import com.yash.androidrestfulapi.R;
import com.yash.androidrestfulapi.model.CityItem;

import java.util.List;
import java.util.Map;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>
{
    private Context context;
    private List<CityItem> cityItemsList;
    private Map<String,Bitmap> mBitmaps;

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
}
