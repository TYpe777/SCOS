package es.source.code.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import es.source.code.activity.R;
import es.source.code.model.Food;

/**
 * Created by taoye on 2018/10/4.
 */
public class FoodListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Food> foods;
    private View.OnClickListener mOnClickListener;

    public FoodListViewAdapter(Context context,List<Food> foods,View.OnClickListener onClickListener){
        inflater = LayoutInflater.from(context);
        this.foods = foods;
        mOnClickListener = onClickListener;
    }

    @Override
    public int getCount(){
        return foods != null?foods.size():0;
    }

    @Override
    public Object getItem(int position){
        return foods != null?foods.get(position):null;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.food_view_listview_item,null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_foodview_name);
            viewHolder.price = (TextView) convertView.findViewById(R.id.tv_foodview_price);
            viewHolder.btn = (Button) convertView.findViewById(R.id.btn_foodview_order);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Food food = foods.get(position);

        if(food != null){
            viewHolder.name.setText(food.getName());
            viewHolder.price.setText(Float.toString(food.getPrice()));
            viewHolder.btn.setTag(position);
            viewHolder.btn.setOnClickListener(this.mOnClickListener);
        }

        return convertView;
    }

    static class ViewHolder{
        TextView name;
        TextView price;
        Button btn;
    }
}
