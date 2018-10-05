package es.source.code.fragment;

import android.app.Activity;
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
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.source.code.Interface.Interface_HandleOrderItem;
import es.source.code.activity.R;
import es.source.code.adapters.FoodListViewAdapter;
import es.source.code.model.Food;

/**
 * @author taoye
 * @date 2018/10/4.
 * @classname ColdDishesFragment.java
 * @description 冷菜展示页面。可以点菜，也可以退订
 */
public class ColdDishesFragment extends Fragment {
    private Context mContext;
    private View mView;
    private ListView lv_coldDishes;
    private List<Food> foods;

    /**
     * 用来实现Activity向fragment中传递数据
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param foodList
     * @return
     */
    public static ColdDishesFragment newInstance(List<Food> foodList){
        ColdDishesFragment coldDishesFragment = new ColdDishesFragment();
        Bundle bundle_foodlist = new Bundle();
        bundle_foodlist.putSerializable("FoodList",(Serializable) foodList);
        coldDishesFragment.setArguments(bundle_foodlist);
        return coldDishesFragment;
    }

    private Interface_HandleOrderItem interface_handleOrderItem;//声明接口
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        interface_handleOrderItem = (Interface_HandleOrderItem) activity;
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
        lv_coldDishes = (ListView) mView.findViewById(R.id.lv_food);

        foods = new ArrayList<Food>();

        if(getArguments() != null){
            foods = (List<Food>)getArguments().getSerializable("FoodList");
        } else{
            foods.add(new Food("梨汁肋排",45));
            foods.add(new Food("麻婆豆腐",20));
            foods.add(new Food("牛肉炖土豆",50));
            foods.add(new Food("糯米团子",25));
            foods.add(new Food("清蒸大闸蟹",80));
            foods.add(new Food("肉螺炒牛蛙",45));
            foods.add(new Food("土豆炖牛腩",50));
            foods.add(new Food("香菇油菜",18));
            foods.add(new Food("响油泥鳅",45));
            foods.add(new Food("猪肉炖粉条",40));
        }

        FoodListViewAdapter foodListViewAdapter = new FoodListViewAdapter(mContext,foods,onClickListener);
        lv_coldDishes.setAdapter(foodListViewAdapter);

        lv_coldDishes.setOnItemClickListener(new OnItemClickHandler());
    }

    private class OnItemClickHandler implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
            Toast.makeText(mContext, "点击了" + foods.get(position).getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            int pos = (Integer) btn.getTag();
            if("点菜".equals(btn.getText().toString())){
                // 通过调用接口，将food实例传递给activity。再由activity添加进菜单列表中。
                interface_handleOrderItem.addOrderItem(foods.get(pos));//调用接口
                Toast.makeText(mContext,"点菜成功",Toast.LENGTH_SHORT).show();
                btn.setText("退点");
            }else{
                // 通过调用接口，将food实例传递给activity。再由activity添加进菜单列表中。
                interface_handleOrderItem.removeOrderItem(foods.get(pos));//调用接口
                Toast.makeText(mContext,"退订成功",Toast.LENGTH_SHORT).show();
                btn.setText("点菜");
            }
        }
    };
}
