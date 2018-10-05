package es.source.code.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.source.code.activity.R;
import es.source.code.adapters.UnorderedListViewAdapter;
import es.source.code.model.OrderItem;

/**
 * @author taoye
 * @date 2018/10/4.
 * @classname UnorderedFragment.java
 * @description 未下单菜列表。对应FoodView界面的“已点菜品”按钮
 */
public class UnorderedFragment extends Fragment {
    private Context mContext;
    private View mView;
    private ListView lv_unordered;
    private TextView tv_unordered_foodsum,tv_unordered_pricesum;
    private Button btn_submitorder;
    private List<OrderItem> orderList;

    /**
     * 用来实现Activity向fragment中传递数据
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param orderList
     * @return
     */
    public static UnorderedFragment newInstance(List<OrderItem> orderList){
       UnorderedFragment unorderedFragment = new UnorderedFragment();
        Bundle bundle_ordered_orderList = new Bundle();
        bundle_ordered_orderList.putSerializable("OrderList",(Serializable) orderList);
        unorderedFragment.setArguments(bundle_ordered_orderList);
        return unorderedFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_unordered,container,false);
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mView = getView();

        initViews();
    }

    private void initViews(){
        lv_unordered = (ListView) mView.findViewById(R.id.lv_unordered);
        tv_unordered_foodsum = (TextView) mView.findViewById(R.id.tv_foodorderview_unordered_foodsum);
        tv_unordered_pricesum = (TextView) mView.findViewById(R.id.tv_foodorderview_unordered_pricesum);
        btn_submitorder = (Button) mView.findViewById(R.id.btn_foodorderview_ordered_submitorder);

        orderList = new ArrayList<OrderItem>();

        if(getArguments() != null){
            orderList = (List<OrderItem>) getArguments().getSerializable("OrderList");
        }

        UnorderedListViewAdapter unorderedListViewAdapter = new UnorderedListViewAdapter(mContext,orderList,onClickListener);
        lv_unordered.setAdapter(unorderedListViewAdapter);
        lv_unordered.setOnItemClickListener(new OnItemClickHandler());

        // 菜品总数
        int foodsum = 0;
        if(orderList.size() >= 0){
            for(int i=0;i<orderList.size();i++){
                foodsum += orderList.get(i).getCount();
            }
        }
        tv_unordered_foodsum.setText("菜品总数:" + foodsum);
        // 订单总价
        float pricesum = 0;
        if(orderList.size() >= 0){
            for(int i=0;i<orderList.size();i++){
                pricesum += ((orderList.get(i).getFood().getPrice())*(orderList.get(i).getCount()));
            }
        }
        tv_unordered_pricesum.setText("订单总价:" + pricesum);

    }

    private class OnItemClickHandler implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
            Toast.makeText(mContext, "点击了" + orderList.get(position).getFood().getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            int pos = (Integer) btn.getTag();
                Toast.makeText(mContext,"退订成功",Toast.LENGTH_SHORT).show();
        }
    };
}
