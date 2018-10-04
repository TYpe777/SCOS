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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.activity.R;
import es.source.code.adapters.FoodListViewAdapter;
import es.source.code.model.Food;

/**
 * Created by taoye on 2018/10/4.
 */
public class SeafoodFragment extends Fragment {
    private Context mContext;
    private View mView;
    private ListView lv_seafood;
    private List<Food> foods;

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
        lv_seafood = (ListView) mView.findViewById(R.id.lv_food);

        foods = new ArrayList<Food>();

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

        FoodListViewAdapter foodListViewAdapter = new FoodListViewAdapter(mContext,foods,onClickListener);
        lv_seafood.setAdapter(foodListViewAdapter);

        lv_seafood.setOnItemClickListener(new OnItemClickHandler());
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
            if("点菜".equals(btn.getText().toString())){
                int pos = (Integer) btn.getTag();

                String fName = foods.get(pos).getName();
                String fPrice = Float.toString(foods.get(pos).getPrice());

                Toast.makeText(mContext,"点菜成功",Toast.LENGTH_SHORT).show();
                btn.setText("已点");
            }else{
                Toast.makeText(mContext,"已经点菜",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
