package es.source.code.adapters;

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
 * @classname NavigationAdapter
 * @description MainScreen界面的GridView适配器
 */
public class NavigationAdapter extends BaseAdapter {
    private int[] images;
    private String[] texts;
    private LayoutInflater layoutInflater;

    public NavigationAdapter(){
    }

    public NavigationAdapter(Context context, int[] images, String[] texts){
        this.images = images;
        this.texts = texts;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return images != null?images.length:0;
    }

    @Override
    public Object getItem(int position){
        return images != null?images[position]:null;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.gridview_item,null);

            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_gridView);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_gridView);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.image.setImageResource(images[position]);
        viewHolder.title.setText(texts[position]);
        return convertView;
    }

    static class ViewHolder{
        ImageView image;
        TextView title;
    }
}
