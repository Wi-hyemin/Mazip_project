package com.example.totoroto.homework2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private ArrayList<ItemData> mDatas;
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

        holder.storeImage.setImageResource(itemData.storeImage);
        holder.storeName.setText(itemData.storeName);
        holder.storeDetail.setText(itemData.storeDetail);

        if(itemData.getCheck())
            holder.btn_check.setBackgroundResource(R.drawable.ic_checked);
        else
            holder.btn_check.setBackgroundResource(R.drawable.ic_unchecked);
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
                    ItemData itemData = mDatas.get(getAdapterPosition()); //아이템 위치가 자꾸바뀌니까

                    if(itemData.getCheck()) {
                        itemData.isCheck = false;
                        btn_check.setBackgroundResource(R.drawable.ic_unchecked);
                    }
                    else {
                        itemData.isCheck = true;
                        btn_check.setBackgroundResource(R.drawable.ic_checked);
                    }
                }
            });
        }

    }
}
