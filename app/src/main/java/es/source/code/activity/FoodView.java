package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.source.code.Interface.Interface_HandleOrderItem;
import es.source.code.adapters.FoodViewPagerAdapter;
import es.source.code.fragment.ColdDishesFragment;
import es.source.code.fragment.DrinksFragment;
import es.source.code.fragment.HotDishesFragment;
import es.source.code.fragment.SeafoodFragment;
import es.source.code.model.Food;
import es.source.code.model.OrderItem;
import es.source.code.utils.Const;
import es.source.code.utils.FoodList;

/**
 * @author taoye
 * @date 2018/10/3
 * @classname FoodView.java
 * @descripition 食物展示。包含“热菜”、“冷菜”、“海鲜”和“酒水”
 */
public class FoodView extends AppCompatActivity implements Interface_HandleOrderItem {

    private Context mContext;// 上下文
    private Intent intent;
    private TabLayout tl_foodView;
    private ViewPager vp_foodView;

    private FoodList foodList; // 菜单列表
    private List<OrderItem> orderList;//订单列表

    private String[] mTitles = {"热菜","冷菜","海鲜","酒水"}; // 四个tab的标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_view);
        mContext = this;

        initViews();
        initEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_foodview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // ActionBar上的按钮点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_ordered: // 已点菜品
                intent = new Intent(FoodView.this,FoodOrderView.class);
                intent.putExtra(Const.IntentMsg.DEFAULTPAGE,Const.IntentMsg.PAGE_UNORDERED); // 设置FoodOrderView的初始默认页。对应“未下单菜”
                intent.putExtra("OrderList",(Serializable) orderList); // 传递订单给FoodOrderView
                startActivityForResult(intent, Const.RequestCode.FOODVIEW_ORDERED);
                return true;
            case R.id.menu_vieworders: // 查看订单
                intent = new Intent(FoodView.this,FoodOrderView.class);
                intent.putExtra(Const.IntentMsg.DEFAULTPAGE,Const.IntentMsg.PAGE_ORDERED); // 设置FoodOrderView的初始默认页。对应“已下单菜”
                intent.putExtra("OrderList",(Serializable) orderList); // 传递订单给FoodOrderView
                startActivityForResult(intent,Const.RequestCode.FOODVIEW_VIEWORDER);
                return true;
            case R.id.menu_help: // 系统帮助
                // 测试代码
                {
                    String s = "";
                    if(orderList.size()>0){
                        for(int i = 0;i<orderList.size();i++){
                            s += orderList.get(i).getFood().getName();
                            s += ";";
                        }
                    }
                    Toast.makeText(mContext,"已点了"+s,Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @author taoye
     * @description 实现接口类。通过addOrderItem方法接受Fragment中传递来的Food对象，创建订单项添加到订单列表中
     * @param food
     */
    @Override
    public void addOrderItem(Food food){
        orderList.add(new OrderItem(food));
    }
    // 通过addOrderItem方法接受Fragment中传递来的Food对象，删除订单列表中指定的订单项
    @Override
    public void removeOrderItem(Food food) {
        int i;
        if(orderList.size()>0){
            // 找到要删除的food对象的下标
            for(i = 0;i<orderList.size();i++){
                if(food.getName().equals(orderList.get(i).getFood().getName())){
                    break;
                }
            }
            orderList.remove(i);
        }
    }

    /**
     * @author taoye
     * @description 初始化视图
     */
    private void initViews(){
        foodList = new FoodList();
        orderList = new ArrayList<OrderItem>();

        tl_foodView = (TabLayout) findViewById(R.id.tl_foodView);
        vp_foodView = (ViewPager) findViewById(R.id.vp_foodView);

        FoodViewPagerAdapter foodViewPagerAdapter = new FoodViewPagerAdapter(getSupportFragmentManager(),mTitles);

        foodViewPagerAdapter.add(HotDishesFragment.newInstance(foodList.getHotDishes()));
        foodViewPagerAdapter.add(ColdDishesFragment.newInstance(foodList.getColdDishes()));
        foodViewPagerAdapter.add(SeafoodFragment.newInstance(foodList.getSeafood()));
        foodViewPagerAdapter.add(DrinksFragment.newInstance(foodList.getDrinks()));

        vp_foodView.setAdapter(foodViewPagerAdapter);

        vp_foodView.setOffscreenPageLimit(2);
        // Tablayout绑定Viewpager
        tl_foodView.setupWithViewPager(vp_foodView);

        tl_foodView.getTabAt(0).setIcon(getResources().getDrawable(R.mipmap.ic_hotdishes_selected));
        tl_foodView.getTabAt(1).setIcon(getResources().getDrawable(R.mipmap.ic_colddishes_unselected));
        tl_foodView.getTabAt(2).setIcon(getResources().getDrawable(R.mipmap.ic_seafood_unselected));
        tl_foodView.getTabAt(3).setIcon(getResources().getDrawable(R.mipmap.ic_drinks_unselected));
    }

    /**
     * @author taoye
     * @description 初始化事件
     */
    private void initEvents() {
        // 添加这个tab选择事件只是为了切换图片。不添加也可以实现页面的滑动和tab的切换，但图片不换变化
        tl_foodView.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tl_foodView.getTabAt(0)) {
                    tl_foodView.getTabAt(0).setIcon(getResources().getDrawable(R.mipmap.ic_hotdishes_selected));
                    vp_foodView.setCurrentItem(0);
                } else if (tab == tl_foodView.getTabAt(1)) {
                    tl_foodView.getTabAt(1).setIcon(getResources().getDrawable(R.mipmap.ic_colddishes_selected));
                    vp_foodView.setCurrentItem(1);
                } else if (tab == tl_foodView.getTabAt(2)) {
                    tl_foodView.getTabAt(2).setIcon(getResources().getDrawable(R.mipmap.ic_seafood_selected));
                    vp_foodView.setCurrentItem(2);
                } else if (tab == tl_foodView.getTabAt(3)) {
                    tl_foodView.getTabAt(3).setIcon(getResources().getDrawable(R.mipmap.ic_drinks_selected));
                    vp_foodView.setCurrentItem(3);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == tl_foodView.getTabAt(0)) {
                    tl_foodView.getTabAt(0).setIcon(getResources().getDrawable(R.mipmap.ic_hotdishes_unselected));
                } else if (tab == tl_foodView.getTabAt(1)) {
                    tl_foodView.getTabAt(1).setIcon(getResources().getDrawable(R.mipmap.ic_colddishes_unselected));
                } else if (tab == tl_foodView.getTabAt(2)) {
                    tl_foodView.getTabAt(2).setIcon(getResources().getDrawable(R.mipmap.ic_seafood_unselected));
                } else if (tab == tl_foodView.getTabAt(3)) {
                    tl_foodView.getTabAt(3).setIcon(getResources().getDrawable(R.mipmap.ic_drinks_unselected));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}

