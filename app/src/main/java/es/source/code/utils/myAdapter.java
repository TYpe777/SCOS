package es.source.code.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.source.code.activity.R;

/**
 * @author taoye
 * @date 2018/10/2
 * @description 自定义的GridView适配器
 */
public class myAdapter extends BaseAdapter {
    private int[] images;
    private String[] texts;
    private LayoutInflater layoutInflater;

    public myAdapter(){
    }

    public myAdapter(Context context,int[] images,String[] texts){
        this.images = images;
        this.texts = texts;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return images.length;
    }

    @Override
    public Object getItem(int position){
        return images[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        View v = layoutInflater.inflate(R.layout.gridview_item,null);
        ImageView iv = (ImageView) v.findViewById(R.id.iv_gridView);
        TextView tv = (TextView) v.findViewById(R.id.tv_gridView);
        iv.setImageResource(images[position]);
        tv.setText(texts[position]);
        return v;
    }
}
