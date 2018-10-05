package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.source.code.adapters.FoodViewPagerAdapter;
import es.source.code.fragment.OrderedFragment;
import es.source.code.fragment.UnorderedFragment;
import es.source.code.model.OrderItem;
import es.source.code.utils.Const;

/**
 * @author taoye
 * @date 2018/10/4.
 * @classname FoodOrderView
 * @description 订单界面。包含“未下单菜”和“已下单菜”
 */
public class FoodOrderView extends AppCompatActivity{

    private Context mContext;// 上下文
    private Intent intent;
    private TabLayout tl_foodorderView;
    private ViewPager vp_foodorderView;
    private int defaultPage; // 标记默认页是“未下单菜”还是“已下单菜”，从上一层activity传过来的intent中获得。

    private List<OrderItem> unordered_orderList;// 未下单菜列表
    private List<OrderItem> ordered_orderList;// 已下单菜列表

    private String[] tabTitles = {"未下单菜","已下单菜"};// 两个tab的标题

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.food_order_view);
        mContext = this;

        initViews();
        initEvents();
    }

    private void initViews(){
        unordered_orderList = new ArrayList<OrderItem>();
        ordered_orderList = new ArrayList<OrderItem>();
        intent = getIntent();
        defaultPage = intent.getIntExtra(Const.IntentMsg.DEFAULTPAGE, Const.IntentMsg.PAGE_UNORDERED); // 获取默认页的下标。0 或 1
        handleOrderList((List<OrderItem>)intent.getSerializableExtra("OrderList")); // 将得到的订单分成“未下单菜”列表和“已下单菜”列表

        tl_foodorderView = (TabLayout) findViewById(R.id.tl_foodorderView);
        vp_foodorderView = (ViewPager) findViewById(R.id.vp_foodorderView);

        FoodViewPagerAdapter foodViewPagerAdapter = new FoodViewPagerAdapter(getSupportFragmentManager(),tabTitles);

        foodViewPagerAdapter.add(UnorderedFragment.newInstance(unordered_orderList));
        foodViewPagerAdapter.add(OrderedFragment.newInstance(ordered_orderList));

        vp_foodorderView.setAdapter(foodViewPagerAdapter);

        vp_foodorderView.setOffscreenPageLimit(2);
        // 设置默认页
        vp_foodorderView.setCurrentItem(defaultPage);

        tl_foodorderView.setupWithViewPager(vp_foodorderView);
    }

    private void initEvents(){

    }

    /**
     * @author taoye
     * @description 将得到的订单分成“未下单菜”列表和“已下单菜”列表
     * @param orderList
     */
    private void handleOrderList(List<OrderItem> orderList){
        if(orderList.size()>= 0){
            for(int i = 0;i<orderList.size();i++){
                if(orderList.get(i).isOrdered()){
                    ordered_orderList.add(orderList.get(i));
                }else{
                    unordered_orderList.add(orderList.get(i));
                }
            }
        }
    }
}
