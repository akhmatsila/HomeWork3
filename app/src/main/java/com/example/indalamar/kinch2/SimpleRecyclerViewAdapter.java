package com.example.indalamar.kinch2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.indalamar.kinch2.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

public  class SimpleRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder> {
    private final ItemListActivity mParentActivity;
    private final ArrayList<DummyContent.DummyItem> mValues = new ArrayList<>() ;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();

            Context context = view.getContext();
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("url",item.image);

            context.startActivity(intent);
        }
    };


    SimpleRecyclerViewAdapter(ItemListActivity parent) {
        mParentActivity = parent;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }
    public void setElement(ArrayList<Pair<String,String>> comeValues)
    {
        for (int i = 0 ; i < comeValues.size();++i)
        {
            mValues.add(DummyContent.createDummyItem(i,comeValues.get(i).second,comeValues.get(i).first));
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}
