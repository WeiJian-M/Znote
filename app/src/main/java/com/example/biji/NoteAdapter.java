package com.example.biji;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter implements Filterable {
    private Context mContext;

    private List<Note> backList; //备份原始的数据
    private List<Note> noteList; //现在的数据，或者说需要操作的数据
    private MyFilyter mFilter;

    public NoteAdapter(Context mContext, List<Note> noteList){
        this.mContext = mContext;
        this.noteList = noteList;
        backList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mContext.setTheme(R.style.DayTheme);

        View v = View.inflate(mContext, R.layout.note_layout, null);
        TextView tv_content = (TextView)v.findViewById(R.id.tv_content);
        TextView tv_time = (TextView)v.findViewById(R.id.tv_time);

        String allText = noteList.get(position).getContent();

        tv_content.setText(allText);
        tv_time.setText(noteList.get(position).getTime());

        v.setTag(noteList.get(position).getId());

        return v;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null){
            mFilter = new MyFilyter();
        }
        return mFilter;
    }

    class MyFilyter extends Filter {

        // 在下面这个方法中定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            List<Note> list;
            if(TextUtils.isEmpty(constraint)){//当过滤的关键字为空时，我们显示所有的数据
                list = backList;
            }else{ //否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                for (Note note: backList){
                    if(note.getContent().contains(constraint)){
                        list.add(note);
                    }
                }
            }
            result.values = list; //将得到的几何保存到FilterResults的values变量中
            result.count = list.size(); // 将集合的大小保存到FilterResults的count变量中

            return result;
        }

        // 下面这个方法告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            noteList = (List<Note>)results.values;
            if(results.count > 0){
                notifyDataSetChanged(); // 通知数据发生了改变
            }else{
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}
