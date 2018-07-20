package com.example.shuaige.common.adapter;

import android.support.annotation.DrawableRes;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ShuaiGe on 2018-07-20.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.VH> {

    private List<T> mData = new ArrayList<>();
    private ArrayList<Integer> mLayouts;

    public BaseRecyclerViewAdapter(List<T> mData, OnItemClick<T> mOnItemClick, OnItemLongClickListener onLongClickListener, Integer... layoutIds) {
        this.mData = mData;
        this.mLayouts = new ArrayList<>(Arrays.asList(layoutIds));
        this.mOnItemClick = mOnItemClick;
        this.onLongClickListener = onLongClickListener;
    }


    public BaseRecyclerViewAdapter(List<T> mData, OnItemClick<T> mOnItemClick, Integer... layoutIds) {
        this.mData = mData;
        this.mLayouts = new ArrayList<>(Arrays.asList(layoutIds));
        this.mOnItemClick = mOnItemClick;
    }

    public BaseRecyclerViewAdapter(List<T> list, Integer... layoutIds) {
        this.mData = list;
        this.mLayouts = new ArrayList<>(Arrays.asList(layoutIds));
    }

    private OnItemClick<T> mOnItemClick;
    private OnItemLongClickListener onLongClickListener;

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH holder = null;
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(mLayouts.get(viewType),parent,false);
        holder = new VH(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        ArrayList<Integer> clickIds = bindDataToView(holder, position, mData.get(position));
        Click click = new Click(position, mData.get(position));
        holder.itemView.setOnClickListener(click);
    }

    @Override
    public int getItemCount() {
        if (mData == null ||mData.isEmpty()){
            return 0;
        }
        return mData.size();
    }

    public abstract ArrayList<Integer> bindDataToView(VH viewHolder,int postion,T model);

    public static class VH extends RecyclerView.ViewHolder {
        SparseArray<View> viewHoler;

        public VH(View itemView) {
            super(itemView);
        }

        public <V extends View> V get(int id) {
            if (viewHoler == null) {
                viewHoler = new SparseArray<>();
            }
            View childView = viewHoler.get(id);
            if (childView == null) {
                childView = itemView.findViewById(id);
                viewHoler.put(id, childView);
            }
            return (V) childView;
        }

        public void setText(int tvId, CharSequence charSequence) {
            TextView tv = get(tvId);
            if (tv != null) {
                tv.setText(charSequence);
            }
        }

        public void setTexDrawable(int tvId, @DrawableRes int l, @DrawableRes int t, @DrawableRes int r,
                                   @DrawableRes int b) {
            TextView tv = get(tvId);
            if (tv != null) {
                tv.setCompoundDrawablesWithIntrinsicBounds(l, t, r, b);
            }
        }

        public void setTextColorStr(int tvId, Pair<String, String>... colorString) {
            TextView tv = get(tvId);
            String source = "<font color=\"%1$s\">%2$s</font>";
            StringBuilder sb = new StringBuilder();
            for (Pair<String, String> p : colorString) {
                sb.append(String.format(source, p.first, p.second));
            }
            tv.setText(Html.fromHtml(sb.toString()));
        }

    }

    /**
     * @param <T>
     */
    public interface OnItemClick<T> {
        void onItemClick(View v, int position, T item);
    }

    /**
     * OnItemLongClickListener
     *
     * @param <T>
     */
    public interface OnItemLongClickListener<T> {
        void onLongClick(View v, int position, T item);
    }

    public void setOnItemClickListener(OnItemClick<T> l) {
        this.mOnItemClick = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        this.onLongClickListener = l;
    }

    class Click implements View.OnClickListener{
        int mPosition;
        T mCurModel;

        public Click(int mPosition, T mCurModel) {
            this.mPosition = mPosition;
            this.mCurModel = mCurModel;
        }

        @Override
        public void onClick(View v) {
            mOnItemClick.onItemClick(v,mPosition,mCurModel);
        }
    }
}
