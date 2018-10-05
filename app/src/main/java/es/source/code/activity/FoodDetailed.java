package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.model.Food;
import es.source.code.model.OrderItem;
import es.source.code.model.User;
import es.source.code.utils.Const;

/**
 * @author taoye
 * @date 2018/10/4.
 * @classname FoodDetailed.java
 * @description 菜品的详细信息界面
 */
public class FoodDetailed extends AppCompatActivity implements OnClickListener,OnTouchListener{

    private Context mContext;
    private Intent intent;

    private RelativeLayout rlayout_fooddetailed; // 布局界面的主布局，用于手势监听
    private Button btn_order;
    private TextView tv_name,tv_price;
    private EditText et_description;
    private ImageView iv_image;

    private GestureDetector gestureDetector; // 手势监听实例
    private static final int FLING_MIN_DISTANCE = 50;   //最小距离
    private static final int FLING_MIN_VELOCITY = 20;  //最小速度

    private User loginUser; // 当前用户
    private int indexInFoodList; // 记录当前显示的是食物在列表中的下标
    private List<Food> foodList;// 当前显示的食物列表
    private List<OrderItem> orderList; // 订单列表，用于决定按钮是显示“点菜”还是“退点”

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detailed);
        mContext = this;

        initViews();
        initEvents();
    }

    private void initViews(){
        rlayout_fooddetailed = (RelativeLayout) findViewById(R.id.rlayout_fooddetailed);
        iv_image = (ImageView) findViewById(R.id.iv_fooddetailed_image);
        tv_name = (TextView) findViewById(R.id.tv_fooddetailed_name);
        tv_price = (TextView) findViewById(R.id.tv_fooddetailed_price);
        et_description = (EditText) findViewById(R.id.et_fooddetailed_description);
        btn_order = (Button) findViewById(R.id.btn_fooddetailed_order);

        intent = getIntent();
        indexInFoodList = intent.getIntExtra("Index", 0);
        foodList = new ArrayList<Food>();
        foodList = (List<Food>)intent.getSerializableExtra("FoodList");
        loginUser = (User) intent.getSerializableExtra(Const.IntentMsg.USER);
        orderList = loginUser.getOrderList();
//        orderList = new ArrayList<OrderItem>();

        showFood(indexInFoodList);

        btn_order.setOnClickListener(this);
    }

    private void initEvents(){
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
            public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY){
                float x1 = e1.getX() -  e2.getX();
                float x2 = e2.getX() - e1.getX();
                // 向左滑
                if(x1 > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY){
                    if((indexInFoodList+1)==foodList.size()){
                        Toast.makeText(mContext,"已经是最后一个菜了",Toast.LENGTH_SHORT).show();
                    }else{
                        indexInFoodList ++;
                        showFood(indexInFoodList);
                    }
                }
                // 向右滑
                else if(x2 > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY){
                    if(indexInFoodList == 0){
                        Toast.makeText(mContext,"已经是第一个菜了",Toast.LENGTH_SHORT).show();
                    }else{
                        indexInFoodList--;
                        showFood(indexInFoodList);
                    }
                }
                return false;
            }
        };

        gestureDetector = new GestureDetector(mContext,gestureListener);
        rlayout_fooddetailed.setOnTouchListener(this);
        rlayout_fooddetailed.setLongClickable(true);
    }

    /**
     * @author taoye
     * @description 根据下标显示菜品的具体信息
     * @param i
     */
    private void showFood(int i){
        if(foodList != null){
            if(foodList.get(i).getImage() != null) {
                iv_image.setImageDrawable(foodList.get(i).getImage());
            } else {
                iv_image.setImageDrawable(getResources().getDrawable(R.drawable.ribbed_row_of_pear_juice));
            }
            tv_name.setText(foodList.get(i).getName());
            tv_price.setText(Float.toString(foodList.get(i).getPrice()));
            et_description.setText(foodList.get(i).getDescription());
            if(!foodList.get(i).inOrderList(orderList)){ // 菜品不在订单中，应该显示“点菜”
                btn_order.setText("点菜");
            }else{ // 菜品在订单中，应该显示“退点”
                btn_order.setText("退点");
            }
        }
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_fooddetailed_order:
                break;
        }
    }

    @Override
    public boolean onTouch(View view,MotionEvent event){
        return gestureDetector.onTouchEvent(event);
    }
}
