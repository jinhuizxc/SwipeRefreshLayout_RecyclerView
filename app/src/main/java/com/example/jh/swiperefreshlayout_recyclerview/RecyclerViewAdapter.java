package com.example.jh.swiperefreshlayout_recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者：jinhui on 2017/3/16
 * 邮箱：1004260403@qq.com
 *
 * RecyclerViewAdapterRecyclerView.Adapter必须执行3个方法
 *
 *
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private List<Map<String, Object>> datas;
    private Context context;
    // 构造方法
    public RecyclerViewAdapter(Context context) {
        this.context = context;
        datas = new ArrayList();
    }
    // 添加数据
    public void addData(List data){
        Log.e("==MainActivity==", "getItemCount" + datas.size());
        datas.addAll(data);
        this.notifyItemChanged(0, datas.size());
    }

    public interface OnItemClickListener{
        void onItemClicked(View view, int position);
        void onItemLongClicked(View view, int position);
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载每个列表项
        Log.d("==MainActivity==", "onCreateViewHolder");
        if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.item_base, parent, false);
            return new ItemViewHolder(view);
        }
        if(viewType == TYPE_FOOTER){
            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent, false);
            return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //加载每个列表项的数据
        Log.d("==MainActivity==", "onBindViewHolder");
        if(holder instanceof ItemViewHolder){
            Map<String, Object> listItem = datas.get(position);
            ((ItemViewHolder) holder).name.setText((String)listItem.get("name") + "  ------------" + String.valueOf(position));
            ((ItemViewHolder) holder).description.setText((String)listItem.get("description"));
            ((ItemViewHolder) holder).header.setImageResource((int)listItem.get("header"));
            if(onItemClickListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClicked(holder.itemView, position);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClicked(holder.itemView, position);
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        int aa = datas.size() == 0 ? 0:datas.size() + 1;
        Log.e("==MainActivity==", "getItemCount" + aa);
        return aa;
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("==MainActivity==", "getItemViewType" + position);
        if(position + 1 == getItemCount()){
            return TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }

    }

    //ViewHolder
    public class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView description;
        ImageView header;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            header = (ImageView) itemView.findViewById(R.id.header);
        }
    }

    // footView
    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
