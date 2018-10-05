package es.source.code.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import es.source.code.activity.R;
import es.source.code.model.Food;
import es.source.code.model.OrderItem;

/**
 * Created by taoye on 2018/10/4.
 */
public class OrderedListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<OrderItem> orderList;

    public OrderedListViewAdapter(Context context,List<OrderItem> orderList){
        inflater = LayoutInflater.from(context);
        this.orderList = orderList;
    }

    @Override
    public int getCount(){
        return orderList != null?orderList.size():0;
    }

    @Override
    public Object getItem(int position){
        return orderList != null?orderList.get(position):null;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.food_order_view_ordered_listview_item,null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_foodorderview_ordered_name);
            viewHolder.price = (TextView) convertView.findViewById(R.id.tv_foodorderview_ordered_price);
            viewHolder.count = (TextView) convertView.findViewById(R.id.tv_foodorderview_ordered_count);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tv_foodorderview_ordered_description);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderItem orderItem = orderList.get(position);

        if(orderItem != null){
            Food food = orderItem.getFood();
            String name = food.getName();
            viewHolder.name.setText(orderItem.getFood().getName());
            viewHolder.price.setText(Float.toString(orderItem.getFood().getPrice()));
            viewHolder.count.setText(Integer.toString(orderItem.getCount()));
            viewHolder.description.setText(orderItem.getFood().getDescription());
        }

        return convertView;
    }

    static class ViewHolder{
        TextView name;
        TextView price;
        TextView count;
        TextView description;
    }
}
