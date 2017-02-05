package com.chinafeisite.tianbu.competition.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinafeisite.tianbu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 *  @项目名：  RK3188-BP 
 *  @包名：    com.chinafeisite.tianbu.competition.adapter
 *  @文件名:   Badapter
 *  @创建者:   Administrator
 *  @创建时间:  2016/12/29 11:42
 *  @描述：    TODO
 */

//场景的adapter
public class Badapter extends BaseAdapter
{ private static final String TAG = "Badapter";
    JSONArray mSenceList;
    Context mContext;
    public Badapter(Context context, JSONArray List) {
        mSenceList = List;
        mContext = context;
    }

    @Override
    public int getCount() {
        if (mSenceList!=null){
            return  mSenceList.length();}
        return 0;}
    @Override
    public Object getItem(int position) {
        if (mSenceList!=null){
            try {
                mSenceList.get(position);
            } catch (JSONException e) {
                e.printStackTrace();}}
        return null;
    }
    public  void setDate(JSONArray senceList) {
        mSenceList = senceList;
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "======getView");
        ViewHolder viewHolder =null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.childview, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_scene_name= (TextView) convertView.findViewById(R.id.tv_scene_name);
            viewHolder.iv_select= (ImageView) convertView.findViewById(R.id.iv_select);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        try {
            JSONObject object = (JSONObject) mSenceList.get(position);
            viewHolder.tv_scene_name.setText(object.optString("sceneName"));
            /*if (currentPosition == position ){
                viewHolder.iv_select.setVisibility(View.VISIBLE);
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }


    public class ViewHolder{
        TextView  tv_scene_name;
        ImageView iv_select;
    }
}