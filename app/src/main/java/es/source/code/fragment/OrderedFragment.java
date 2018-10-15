package es.source.code.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.source.code.Interface.Interface_Pay;
import es.source.code.activity.R;
import es.source.code.adapters.OrderedListViewAdapter;
import es.source.code.model.Food;
import es.source.code.model.OrderItem;

/**
 * @author taoye
 * @date 2018/10/4.
 * @classname OrderedFragment.java
 * @description 已下单菜列表。对应FoodView界面的“查看订单”按钮
 */
public class OrderedFragment extends Fragment implements OnClickListener{
    private Context mContext;
    private View mView;
    private ListView lv_ordered;
    private TextView tv_ordered_foodsum,tv_ordered_pricesum;
    private Button btn_pay;
    private List<OrderItem> orderList;

    // 声明接口
    private Interface_Pay interface_pay;
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        interface_pay = (Interface_Pay) activity;
    }

    /**
     * 用来实现Activity向fragment中传递数据
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param orderList
     * @return
     */
    public static OrderedFragment newInstance(List<OrderItem> orderList){
        OrderedFragment orderedFragment = new OrderedFragment();
        Bundle bundle_ordered_orderList = new Bundle();
        bundle_ordered_orderList.putSerializable("OrderList",(Serializable) orderList);
        orderedFragment.setArguments(bundle_ordered_orderList);
        return orderedFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_ordered,container,false);
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mView = getView();

        initViews();
    }

    private void initViews(){
        lv_ordered = (ListView) mView.findViewById(R.id.lv_ordered);
        tv_ordered_foodsum = (TextView) mView.findViewById(R.id.tv_foodorderview_ordered_foodsum);
        tv_ordered_pricesum = (TextView) mView.findViewById(R.id.tv_foodorderview_ordered_pricesum);
        btn_pay = (Button) mView.findViewById(R.id.btn_foodorderview_ordered_pay);
        btn_pay.setOnClickListener(this);

        orderList = new ArrayList<OrderItem>();

//        if(getArguments() != null){
//            orderList = (List<OrderItem>) getArguments().getSerializable("OrderList");
//        }else
        {
            orderList.add(new OrderItem(new Food(R.drawable.ribbed_row_of_pear_juice, "梨汁肋排", 45,"梨:活血化瘀；酱油:活血化瘀" )));
            orderList.add(new OrderItem(new Food(R.drawable.mapo_tofu, "麻婆豆腐", 20,"四川特色")));
            orderList.add(new OrderItem(new Food(R.drawable.braised_beef_with_potatoes, "牛肉炖土豆", 50,"家常菜")));
            orderList.add(new OrderItem(new Food("糯米团子",25)));
            orderList.add(new OrderItem(new Food("清蒸大闸蟹",80)));
            orderList.add(new OrderItem(new Food("肉螺炒牛蛙",45)));
        }

        OrderedListViewAdapter orderedListViewAdapter = new OrderedListViewAdapter(mContext,orderList);
        lv_ordered.setAdapter(orderedListViewAdapter);
        lv_ordered.setOnItemClickListener(new OnItemClickHandler());

        // 菜品总数
        int foodsum = 0;
        if(orderList.size() >= 0){
            for(int i=0;i<orderList.size();i++){
                foodsum += orderList.get(i).getCount();
            }
        }
        tv_ordered_foodsum.setText("菜品总数:" + foodsum);
        // 订单总价
        float pricesum = 0;
        if(orderList.size() >= 0){
            for(int i=0;i<orderList.size();i++){
                pricesum += ((orderList.get(i).getFood().getPrice())*(orderList.get(i).getCount()));
            }
        }
        tv_ordered_pricesum.setText("订单总价:" + pricesum);

    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_foodorderview_ordered_pay:
                // 在FoodOrderView中对用户对象做相应处理
                interface_pay.pay(); // 调用接口
                break;
        }
    }

    private class OnItemClickHandler implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
            Toast.makeText(mContext, "点击了" + orderList.get(position).getFood().getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
