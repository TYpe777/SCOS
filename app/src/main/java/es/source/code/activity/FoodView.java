package es.source.code.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

import es.source.code.Interface.Interface_HandleOrderItem;
import es.source.code.Interface.Interface_ShowFoodDetailed;
import es.source.code.adapters.FoodViewPagerAdapter;
import es.source.code.fragment.ColdDishesFragment;
import es.source.code.fragment.DrinksFragment;
import es.source.code.fragment.HotDishesFragment;
import es.source.code.fragment.SeafoodFragment;
import es.source.code.model.Food;
import es.source.code.model.OrderItem;
import es.source.code.model.User;
import es.source.code.service.ServerObserverService;
import es.source.code.utils.ColddishesProvider;
import es.source.code.utils.Const;
import es.source.code.utils.DrinksProvider;
import es.source.code.utils.HotdishesProvider;
import es.source.code.utils.SeafoodProvider;

/**
 * @author taoye
 * @date 2018/10/3
 * @classname FoodView.java
 * @descripition 食物展示。包含“热菜”、“冷菜”、“海鲜”和“酒水”
 */
public class FoodView extends AppCompatActivity implements Interface_HandleOrderItem,Interface_ShowFoodDetailed {
    private static final String TAG = "FoodView";
    private Context mContext;// 上下文
    private Intent intent;
    private TabLayout tl_foodView;
    private ViewPager vp_foodView;

    private User loginUser; // 当前用户
    private List<Food> Hotdishes, Colddishes, Seafood, Drinks; // 用于记录当前的四张菜单
    private List<OrderItem> orderList;//订单列表

    private boolean isBound = false; // 是否与远程服务绑定
    private MyHandler myHandler = new MyHandler(FoodView.this);
    private Messenger mMessenger; // 客户端自己的信使
    private Messenger rMessenger; // 远程服务的信使
    // 服务连接器,用于记录客户端与远程服务的绑定状态
    private ServiceConnection serviceConn = new ServiceConnection() {
        // 绑定成功后回调
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "绑定服务");
            rMessenger = new Messenger(service);
            mMessenger = new Messenger(myHandler);
            isBound = true;
        }
        // 解绑时回调
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "解绑服务");
            rMessenger = null;
            isBound = false;
        }
    };

    private String[] mTitles = {"热菜","冷菜","海鲜","酒水"}; // 四个tab的标题
    private boolean isStarted = false; // 判断是否开启实时更新


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_view);
        mContext = this;

        initViews();
        initEvents();
    }


    /**
     * 在onStart()方法中绑定服务
     */
    @Override
    protected void onStart(){
        super.onStart();
        intent = new Intent(FoodView.this, ServerObserverService.class);
        bindService(intent, serviceConn, Context.BIND_AUTO_CREATE);
    }

    /**
     * 在onStop()方法中解绑服务，变换menu
     */
    @Override
    protected void onStop(){
        super.onStop();
        if(isBound){ // 解绑服务
            isBound = false;
            unbindService(serviceConn);
        }
        if(isStarted){ // 如果启动了实时更新
            isStarted = false; // 将标记变为未开启
            invalidateOptionsMenu(); // 变换menu
        }
    }

    /**
     * 声明一个静态的Handle内部类，并持有外部类的弱引用
     */
    private static class MyHandler extends Handler {
        private final WeakReference<FoodView> mActivity;

        private MyHandler(FoodView mActivity) {
            this.mActivity = new WeakReference<FoodView>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 10){
                Log.i(TAG, "实时传递成功");
                List<Food> foodList = (List<Food>)msg.getData().getSerializable("Hotdishes");
                int i = foodList.size();
                EventBus.getDefault().post(foodList);
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 初始化Menu
     * onCreateOptionsMenu在Activity的整个周期中只被调用一次
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_foodview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 在需要更新ActionBar上menu的地方执行inVaildateOptionsMenu()方法,会回调此方法
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(!isStarted){
            menu.findItem(R.id.foodview_menu_update).setVisible(true);
            menu.findItem(R.id.foodview_menu_ordered).setVisible(true);
            menu.findItem(R.id.foodview_menu_vieworders).setVisible(true);
            menu.findItem(R.id.foodview_menu_help).setVisible(true);
            menu.findItem(R.id.foodview_menu_stop).setVisible(false);
        }
        else {
            menu.findItem(R.id.foodview_menu_stop).setVisible(true);
            menu.findItem(R.id.foodview_menu_ordered).setVisible(true);
            menu.findItem(R.id.foodview_menu_vieworders).setVisible(true);
            menu.findItem(R.id.foodview_menu_help).setVisible(true);
            menu.findItem(R.id.foodview_menu_update).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 处理ActionBar上的菜单项点击事件
      * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.foodview_menu_update: // 开启实时更新
                try {
                    if (!isStarted) {
                        Message message = Message.obtain();
                        message.what = 1;
                        message.replyTo = mMessenger; // 通过message将客户端的信使发送给服务端
                        rMessenger.send(message); // 利用服务端的信使发送消息,在服务中的Handler中获得这个消息

                        invalidateOptionsMenu(); // 更新menu,将"开启实时更新"换成"关闭实施更新"
                        isStarted = true;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.foodview_menu_stop: // 关闭实时更新
                try{
                    if(isStarted){
                        Message message = Message.obtain();
                        message.what = 0;
                        message.replyTo = mMessenger;
                        rMessenger.send(message);

                        invalidateOptionsMenu();
                        isStarted = false;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.foodview_menu_ordered: // 已点菜品
                intent = new Intent(FoodView.this,FoodOrderView.class);
                // 设置FoodOrderView的初始默认页。对应“未下单菜”
                intent.putExtra(Const.IntentMsg.DEFAULTPAGE,Const.IntentMsg.PAGE_UNORDERED);
                intent.putExtra(Const.IntentMsg.USER,loginUser);// 传递用户对象给FoodOrderView
                startActivityForResult(intent, Const.RequestCode.FOODVIEW_ORDERED);
                break;
            case R.id.foodview_menu_vieworders: // 查看订单
                intent = new Intent(FoodView.this,FoodOrderView.class);
                // 设置FoodOrderView的初始默认页。对应“已下单菜”
                intent.putExtra(Const.IntentMsg.DEFAULTPAGE,Const.IntentMsg.PAGE_ORDERED);
                intent.putExtra(Const.IntentMsg.USER, loginUser);// 传递用户对象给FoodOrderView
                startActivityForResult(intent,Const.RequestCode.FOODVIEW_VIEWORDER);
                break;
            case R.id.foodview_menu_help: // 系统帮助
                // 测试代码
                {
                    String s = "";
                    if(orderList.size()>0){
                        for(int i = 0;i<orderList.size();i++){
                            s += orderList.get(i).getFood().getName();
                            s += ";";
                        }
                    }
                    Toast.makeText(mContext, "已点了" + s, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @author taoye
     * @description 实现接口类Interface_HandleOrderItem。
     *                通过addOrderItem方法接受Fragment中传递来的Food对象，创建订单项添加到订单列表中
     * @param food
     */
    @Override
    public void addOrderItem(Food food) {
        orderList.add(new OrderItem(food));
        loginUser.setOrderList(orderList);// 将订单列表同步到当前的用户对象中
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
            loginUser.setOrderList(orderList);// 将订单列表同步到当前的用户对象中
        }
    }

    /**
     * @author taoye
     * @description 实现接口类Interface_ShowFoodDetailed。实现点击菜品项，进入菜品具体信息页面
     * @param foodType
     * @param index
     */
    @Override
    public void showFoodDetailed(String foodType,int index){
        intent = new Intent(mContext,FoodDetailed.class);
        // 传递对应的菜单列表
        if("HotDishes".equals(foodType)){
            intent.putExtra(Const.IntentMsg.FOODLIST, (Serializable)Hotdishes);
        } else if("ColdDishes".equals(foodType)){
            intent.putExtra(Const.IntentMsg.FOODLIST, (Serializable)Colddishes);
        }else if("Drinks".equals(foodType)){
            intent.putExtra(Const.IntentMsg.FOODLIST, (Serializable)Drinks);
        }else {
            intent.putExtra(Const.IntentMsg.FOODLIST, (Serializable)Seafood);
        }
        // 传递当前的用户loginUser
        intent.putExtra(Const.IntentMsg.USER,loginUser);
        // 传递点击菜品在菜单列表中的下标
        intent.putExtra("Index",index);
        startActivityForResult(intent,Const.RequestCode.FOODVIEW_FOODDETAILED);
    }

    /**
     * @author taoye
     * @description 初始化视图
     */
    private void initViews(){
        intent = getIntent();
        loginUser = (User) intent.getSerializableExtra(Const.IntentMsg.USER);
        orderList = loginUser.getOrderList();
//        orderList = new ArrayList<OrderItem>();

        tl_foodView = (TabLayout) findViewById(R.id.tl_foodView);
        vp_foodView = (ViewPager) findViewById(R.id.vp_foodView);

        FoodViewPagerAdapter foodViewPagerAdapter = new FoodViewPagerAdapter(getSupportFragmentManager(),mTitles);

        // 初始化四张菜单
        Hotdishes = HotdishesProvider.getList(FoodView.this);
        Colddishes = ColddishesProvider.getList(FoodView.this);
        Seafood = SeafoodProvider.getList(FoodView.this);
        Drinks = DrinksProvider.getList(FoodView.this);
        // 添加Fragment实例，并传递菜单列表
        foodViewPagerAdapter.add(HotDishesFragment.newInstance(Hotdishes));
        foodViewPagerAdapter.add(ColdDishesFragment.newInstance(Colddishes));
        foodViewPagerAdapter.add(SeafoodFragment.newInstance(Seafood));
        foodViewPagerAdapter.add(DrinksFragment.newInstance(Drinks));

        // 为ViewPager设置适配器
        vp_foodView.setAdapter(foodViewPagerAdapter);
        // 设置预加载的页数，越少越好
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
        // 添加这个tab选择监听事件只是为了切换图片。不添加也可以实现页面的滑动和tab的切换，但图片不会变化
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

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        switch(resultCode){
            case Const.ResultCode.FROM_FOODORDERVIEW:
                intent = getIntent();
                if(intent.getSerializableExtra(Const.IntentMsg.USER) != null){
                    User user = (User) intent.getSerializableExtra(Const.IntentMsg.USER);
                    loginUser.setOrderList(user.getOrderList());
                    // 测试，更新ViewPager
                    {

                    }
                }
                break;
            case Const.ResultCode.FROM_FOODDETAILED:
                intent = getIntent();
                if(intent.getSerializableExtra(Const.IntentMsg.USER) != null){
                    User user = (User) intent.getSerializableExtra(Const.IntentMsg.USER);
                    loginUser.setOrderList(user.getOrderList());
                }
                break;
        }
    }

    // 点击手机上的退出键相应事件
    @Override
    public void onBackPressed(){
        intent = new Intent();
        intent.putExtra(Const.IntentMsg.USER,loginUser);
        setResult(Const.ResultCode.FROM_FOODVIEW, intent);
        finish();
    }
}

