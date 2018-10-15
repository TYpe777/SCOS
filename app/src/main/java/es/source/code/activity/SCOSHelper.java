package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import es.source.code.adapters.NavigationAdapter;
import es.source.code.utils.Const;

/**
 * Created by taoye on 2018/10/15.
 */
public class SCOSHelper extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Context mContext;
    private Intent intent;
    private GridView gv_helper;

    private int[] images = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
    private String[] titles = {"用户使用协议","关于系统","电话人工帮助","短信帮助","邮件帮助"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoshelper);
        mContext = this;

        initViews();
    }

    private void initViews(){
        gv_helper = (GridView) findViewById(R.id.gridView_helper);

        NavigationAdapter helperAdapter = new NavigationAdapter(mContext,images,titles);
        gv_helper.setAdapter(helperAdapter);
        gv_helper.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView,View view,int i,long l){
        if(adapterView.getId() == R.id.gridView_helper){
            switch(images[i]){
                case R.mipmap.ic_launcher:
                    Toast.makeText(mContext,"点击了第"+ i +"个按钮",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed(){
        intent = new Intent();
        setResult(Const.ResultCode.FROM_SCOSHELPER,intent);
        finish();
    }
}
