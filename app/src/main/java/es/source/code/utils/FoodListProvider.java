package es.source.code.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import es.source.code.model.Food;

/**
 * @author taoye
 * @date 2018/10/29.
 * @classname FoodListProvider.java
 * @description 菜单的提供者的基类，从Json文件中读取菜单
 */
public class FoodListProvider {
    private static final String filename = "foods.json"; // Json文件名

    // 从json文件中读取数据，并返回一个包含Food对象的List
    protected static List<Food> getFoodsFromJSON(String listName, Context mContext){
        List<Food> foodList = new ArrayList<>();
        Food food;
        try{
            InputStreamReader inputStreamReader = new InputStreamReader(mContext.getAssets().open(filename));
            BufferedReader bReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder sBuilder = new StringBuilder();
            while((line = bReader.readLine()) != null){
                sBuilder.append(line);
            }
            bReader.close();
            inputStreamReader.close();

            JSONObject foodsJson = new JSONObject(sBuilder.toString());
            JSONArray foodsArray = foodsJson.getJSONArray(listName);
            for(int i = 0; i<foodsArray.length(); i++){
                JSONObject foodObject = foodsArray.getJSONObject(i);
                food = GsonUtil.getFoodFromJsonWithGSON(foodObject.toString());
                foodList.add(food);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return foodList;
    }

    /**
     *通过读不同的Json文件模拟服务器
     * 后期会删除
     */
    protected static List<Food> getFoodsFromJSON_simu(String listName, Context mContext){
        List<Food> foodList = new ArrayList<>();
        Food food;
        try{
            InputStreamReader inputStreamReader = new InputStreamReader(mContext.getAssets().open("simulation.json"));
            BufferedReader bReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder sBuilder = new StringBuilder();
            while((line = bReader.readLine()) != null){
                sBuilder.append(line);
            }
            bReader.close();
            inputStreamReader.close();

            JSONObject foodsJson = new JSONObject(sBuilder.toString());
            JSONArray foodsArray = foodsJson.getJSONArray(listName);
            for(int i = 0; i<foodsArray.length(); i++){
                JSONObject foodObject = foodsArray.getJSONObject(i);
                food = GsonUtil.getFoodFromJsonWithGSON(foodObject.toString());
                foodList.add(food);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return foodList;
    }
}
