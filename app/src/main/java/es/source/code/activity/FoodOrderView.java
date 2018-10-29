package es.source.code.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.Interface.Interface_Pay;
import es.source.code.adapters.FoodViewPagerAdapter;
import es.source.code.fragment.OrderedFragment;
import es.source.code.fragment.UnorderedFragment;
import es.source.code.model.OrderItem;
import es.source.code.model.User;
import es.source.code.utils.Const;

/**
 * @author taoye
 * @date 2018/10/4.
 * @classname FoodOrderView
 * @description 订单界面。包含“未下单菜”和“已下单菜”
 */
public class FoodOrderView extends AppCompatActivity implements Interface_Pay{

    private Context mContext;// 上下文
    private Intent intent;
    private TabLayout tl_foodorderView;
    private ViewPager vp_foodorderView;
    private int defaultPage; // 标记默认页是“未下单菜”还是“已下单菜”，从上一层activity传过来的intent中获得。

    private User loginUser;// 当前的用户
    private List<OrderItem> userOrderList;// 用户的订单。要实时与用户对象内部的订单属性同步
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

    /**
     * @author taoye
     * @description 实现接口类Interface_Pay。点击“结账”时，判断用户是否是老用户
     */
    @Override
    public void pay(){
        new PayAsyncTask().execute();
    }

    private void initViews(){
        userOrderList = new ArrayList<OrderItem>();
        unordered_orderList = new ArrayList<OrderItem>();
        ordered_orderList = new ArrayList<OrderItem>();
        intent = getIntent();
        defaultPage = intent.getIntExtra(Const.IntentMsg.DEFAULTPAGE, Const.IntentMsg.PAGE_UNORDERED); // 获取默认页的下标。0 或 1
        loginUser = (User) intent.getSerializableExtra(Const.IntentMsg.USER);
        userOrderList = loginUser.getOrderList();
        handleOrderList(userOrderList); // 将得到的订单分成“未下单菜”列表和“已下单菜”列表

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

    // 点击手机上的退出键相应事件
    @Override
    public void onBackPressed(){
        intent = new Intent();
        intent.putExtra(Const.IntentMsg.USER,loginUser);
        setResult(Const.ResultCode.FROM_FOODORDERVIEW, intent);
        finish();
    }

    class PayAsyncTask extends AsyncTask<String,Integer,Void>{
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(FoodOrderView.this);
//            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER); // 圆形旋转进度条
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL); // 水平进度条
            progressDialog.setMax(6);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings){
//            SystemClock.sleep(6000); // 搭配圆形旋转进度条
            for(int i= -1; i<6;i++){
                publishProgress(i+1);
                SystemClock.sleep(1000);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            if(loginUser != null && loginUser.isOldUser()){
                Toast.makeText(mContext, "您好，老顾客，本次你可享受 7 折优惠", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }
}
