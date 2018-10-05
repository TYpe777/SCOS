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
import es.source.code.model.OrderItem;

/**
 * Created by taoye on 2018/10/4.
 */
public class UnorderedListViewAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<OrderItem> orderList;
    private View.OnClickListener mOnClickListener;

    public UnorderedListViewAdapter(Context context,List<OrderItem> orderList,View.OnClickListener onClickListener){
        inflater = LayoutInflater.from(context);
        this.orderList = orderList;
        mOnClickListener = onClickListener;
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
            convertView = inflater.inflate(R.layout.food_order_view_unordered_listview_item,null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_foodorderview_unordered_name);
            viewHolder.price = (TextView) convertView.findViewById(R.id.tv_foodorderview_unordered_price);
            viewHolder.count = (TextView) convertView.findViewById(R.id.tv_foodorderview_unordered_count);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tv_foodorderview_unordered_description);
            viewHolder.btn = (Button) convertView.findViewById(R.id.btn_foodorderview_unordered_unorder);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderItem orderItem = orderList.get(position);

        if(orderItem != null){
            viewHolder.name.setText(orderItem.getFood().getName());
            viewHolder.price.setText(Float.toString(orderItem.getFood().getPrice()));
            viewHolder.count.setText(Integer.toString(orderItem.getCount()));
            viewHolder.description.setText(orderItem.getFood().getDescription());
            viewHolder.btn.setTag(position);
            viewHolder.btn.setOnClickListener(this.mOnClickListener);
        }

        return convertView;
    }

    static class ViewHolder{
        TextView name;
        TextView price;
        TextView count;
        TextView description;
        Button btn;
    }
}
