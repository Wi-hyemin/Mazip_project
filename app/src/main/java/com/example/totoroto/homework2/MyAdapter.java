package com.example.totoroto.homework2;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private ArrayList<ItemData> mDatas;
    Context context;
    ItemData itemData;

    public void setItemDatas(ArrayList<ItemData> itemDatas){
        mDatas = itemDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        itemData = mDatas.get(position);
        holder.storeImage.setImageBitmap(itemData.getBitmap());
        holder.storeName.setText(itemData.storeName);
        holder.storeDetail.setText(itemData.storeDetail);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView storeImage;
        TextView storeName;
        TextView storeDetail;
        Button btn_check;

        public MyViewHolder(final View itemView) {
            super(itemView);

            storeImage = (ImageView)itemView.findViewById(R.id.storeImage);
            storeName = (TextView)itemView.findViewById(R.id.storeName);
            storeDetail = (TextView)itemView.findViewById(R.id.storeDetail);
            btn_check = (Button)itemView.findViewById(R.id.btn_check);

            btn_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, MapActivity.class);
                    /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                    intent.putExtra("storeName", itemData.getStoreName());
                    intent.putExtra("lat", itemData.getLat());
                    intent.putExtra("lon", itemData.getLon());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setContext(Context context){
        this.context = context;
    }
}

