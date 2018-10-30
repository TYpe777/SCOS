package es.source.code.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.source.code.Interface.Interface_HandleOrderItem;
import es.source.code.Interface.Interface_ShowFoodDetailed;
import es.source.code.activity.R;
import es.source.code.adapters.FoodListViewAdapter;
import es.source.code.model.Food;
import es.source.code.utils.Const;

/**
 * @author taoye
 * @date 2018/10/4.
 * @classname HotDishesFragment.java
 * @description 热菜展示页面。可以点菜，也可以退订
 */
public class HotDishesFragment extends Fragment {

    private Context mContext;
    private View mView;
    private Intent intent;
    private ListView lv_hotDishes;
    private List<Food> foods;

    //声明接口
    private Interface_HandleOrderItem interface_handleOrderItem;
    private Interface_ShowFoodDetailed interface_showFoodDetailed;
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        interface_handleOrderItem = (Interface_HandleOrderItem) activity;
        interface_showFoodDetailed = (Interface_ShowFoodDetailed) activity;
    }

    /**
     * 用来实现Activity向fragment中传递数据
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param foodList
     * @return
     */
    public static HotDishesFragment newInstance(List<Food> foodList){
        HotDishesFragment hotDishesFragment = new HotDishesFragment();
        Bundle bundle_foodlist = new Bundle();
        bundle_foodlist.putSerializable(Const.IntentMsg.FOODLIST,(Serializable) foodList);
        hotDishesFragment.setArguments(bundle_foodlist);
        return hotDishesFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_food,container,false);
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mView = getView();

        initViews();
    }

    private void initViews(){
        lv_hotDishes = (ListView) mView.findViewById(R.id.lv_food);

        foods = new ArrayList<Food>();

        if(getArguments() != null){
            foods = (List<Food>)getArguments().getSerializable(Const.IntentMsg.FOODLIST);
        }

        // 创建自定义适配器的实例
        FoodListViewAdapter foodListViewAdapter = new FoodListViewAdapter(mContext,foods,onClickListener);
        // 为ListView控件设置适配器
        lv_hotDishes.setAdapter(foodListViewAdapter);

        lv_hotDishes.setOnItemClickListener(new OnItemClickHandler());
    }

    // ListView的Item点击监听事件
    private class OnItemClickHandler implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
            // 实现点击菜单项，打开对应菜品的详细信息界面
            interface_showFoodDetailed.showFoodDetailed("HotDishes",position); // 调用接口
        }
    }
    // ListView每一个Item中的Button点击监听器的实例
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            int pos = (Integer) btn.getTag();
            if("点菜".equals(btn.getText().toString())){
                // 通过调用接口，将food实例传递给activity。再由activity添加进订单列表中。
                interface_handleOrderItem.addOrderItem(foods.get(pos));//调用接口
                Toast.makeText(mContext,"点菜成功",Toast.LENGTH_SHORT).show();
                btn.setText("退点");
            }else{
                // 通过调用接口，将food实例传递给activity。再由activity添加进订单列表中。
                interface_handleOrderItem.removeOrderItem(foods.get(pos));//调用接口
                Toast.makeText(mContext,"退订成功",Toast.LENGTH_SHORT).show();
                btn.setText("点菜");
            }
        }
    };


    // 在EventBus上注册
    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    // 从EventBus上解除注册
    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    // 接收消息的处理方法
    @Subscribe
    public void onReceiverEvent(List<Food> foodList){
        // 创建自定义适配器的实例
        FoodListViewAdapter foodListViewAdapter = new FoodListViewAdapter(mContext, foodList, onClickListener);
        // 为ListView控件设置适配器
        lv_hotDishes.setAdapter(foodListViewAdapter);
    }
}
