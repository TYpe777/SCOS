package es.source.code.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.source.code.model.Food;

/**
 * @author taoye
 * @date 2018/11/3
 * @classname HttpURLConnectionUtil.java
 * @description 通过HttpURLConnection实现与Servlet的请求与响应
 */
public class HttpURLConnectionUtil {
//    public static String BASE_URL = "http://192.168.191.1:8080/SCOSServer"; // 网址

    /**
     * 登录
     * 发送Http请求，并获取WebServer的响应
     * @param urlStr 网址
     * @param parms 提交的数据，参数
     * @return 返回的登录结果
     */
    public static String getLoginStatusByHttp(String urlStr, Map<String, String> parms){
        StringBuilder sBuilder = new StringBuilder();
        try{
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            bWriter.write(getStringFromOutput(parms));

            bWriter.flush();
            bWriter.close();
            outputStream.close();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bReader.readLine()) != null){
                    sBuilder.append(line);
                }
                bReader.close();
                inputStream.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sBuilder.toString();
    }

    /**
     * 注册
     * @param urlStr
     * @param parms
     * @return
     */
    public static String registerByHttp(String urlStr, Map<String, String> parms){
        StringBuilder sBuilder = new StringBuilder();
        try{
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            bWriter.write(getStringFromOutput(parms));

            bWriter.flush();
            bWriter.close();
            outputStream.close();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bReader.readLine()) != null){
                    sBuilder.append(line);
                }
                bReader.close();
                inputStream.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sBuilder.toString();
    }

    /**
     * 通过HttpURLConnection获取Food更新信息
     * @param urlStr
     * @return
     */
    public static List<Food> getUpdateByHttp(String urlStr){
        List<Food> foodList = new ArrayList<>();
        StringBuilder sBuilder = new StringBuilder();
//        String urlStr = "http://192.168.191.1:8080/SCOSServer/FoodUpdateService";
        try{
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(20000); // 设置读数据的超时阈值，默认为5000ms，但这里会出现超市错误，所以我修改成了20000ms
            connection.setConnectTimeout(20000); // 设置连接服务器的超时时间
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bWriter.flush();
            bWriter.close();
            outputStream.close();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String temp;
                while((temp = bReader.readLine()) != null){
                    sBuilder.append(temp);
                }
                bReader.close();
            }
            connection.disconnect();

            JSONArray foodsArray = new JSONArray(sBuilder.toString());
            for(int i = 0; i < foodsArray.length(); i++){
                JSONObject foodObject = foodsArray.getJSONObject(i);
                foodList.add(GsonUtil.getFoodFromJsonWithGSON(foodObject.toString()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return foodList;
    }

    /**
     * 将map转化成key1=value1&key2=value2的形式
     * @param map
     * @return
     */
    private static String getStringFromOutput(Map<String, String> map){
        StringBuilder sBuilder = new StringBuilder();
        try{
            boolean isFirst = true;
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(isFirst){
                    isFirst = false;
                }else{
                    sBuilder.append("&");
                }

                sBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                sBuilder.append("=");
                sBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.i("Http请求", sBuilder.toString());
        return sBuilder.toString();
    }
}
