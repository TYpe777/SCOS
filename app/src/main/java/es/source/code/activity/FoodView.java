package es.source.code.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.Interface.Interface_AddOrderItem;
import es.source.code.adapters.FoodViewAdapter;
import es.source.code.fragment.ColdDishesFragment;
import es.source.code.fragment.DrinksFragment;
import es.source.code.fragment.HotDishesFragment;
import es.source.code.fragment.SeafoodFragment;
import es.source.code.model.Food;
import es.source.code.model.OrderItem;

/**
 * @author taoye
 * @date 2018/10/3
 * @classname FoodView.java
 * @descripition 食物展示
 */
public class FoodView extends AppCompatActivity implements Interface_AddOrderItem{

    private Context mContext;
    private TabLayout tl_foodView;
    private ViewPager vp_foodView;

    private List<OrderItem> orderList;//菜单列表

    private String[] mTitles = {"热菜","冷菜","海鲜","酒水"};

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_ordered:
                Toast.makeText(mContext, "已点菜品", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_vieworders:
                Toast.makeText(mContext,"查看订单",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_help:
                Toast.makeText(mContext,"呼叫帮助",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @author taoye
     * @description 实现接口类。通过addOrderItem方法接受Fragment中传递来的Food对象，创建菜单项添加到菜单列表中
     * @param food
     */
    @Override
    public void addOrderItem(Food food){
        orderList.add(new OrderItem(food));
    }

    private void initViews(){
        orderList = new ArrayList<OrderItem>();

        tl_foodView = (TabLayout) findViewById(R.id.tl_foodView);
        vp_foodView = (ViewPager) findViewById(R.id.vp_foodView);

        FoodViewAdapter foodViewAdapter = new FoodViewAdapter(getSupportFragmentManager(),mTitles);

        foodViewAdapter.add(new HotDishesFragment());
        foodViewAdapter.add(new ColdDishesFragment());
        foodViewAdapter.add(new SeafoodFragment());
        foodViewAdapter.add(new DrinksFragment());

        vp_foodView.setAdapter(foodViewAdapter);

        vp_foodView.setOffscreenPageLimit(2);

        tl_foodView.setupWithViewPager(vp_foodView);

        tl_foodView.getTabAt(0).setIcon(getResources().getDrawable(R.mipmap.ic_hotdishes_selected));
        tl_foodView.getTabAt(1).setIcon(getResources().getDrawable(R.mipmap.ic_colddishes_unselected));
        tl_foodView.getTabAt(2).setIcon(getResources().getDrawable(R.mipmap.ic_seafood_unselected));
        tl_foodView.getTabAt(3).setIcon(getResources().getDrawable(R.mipmap.ic_drinks_unselected));
    }

    /**
     * @description 初始化事件
     * @author taoye
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

