package com.faith.ctv.radioview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RadioView.OnRadionViewListener,View.OnClickListener{

    private RadioView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = (RadioView) findViewById(R.id.id_radioV);
        mView.setOnRadionViewListener(this);
        findViewById(R.id.id_left).setOnClickListener(this);
        findViewById(R.id.id_center).setOnClickListener(this);
        findViewById(R.id.id_right).setOnClickListener(this);
    }

    @Override
    public void clickRadion(View v,boolean isSel) {
        Toast.makeText(this,isSel ? "已选中":"已取消",Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_left:
                mView.setGravity(RadioView.Gravity.GRAVITY_LEFT);
                mView.setText("对齐方式：左对齐");
                break;
            case R.id.id_center:
                mView.setGravity(RadioView.Gravity.GRAVITY_CENTER);
                mView.setText("对齐方式：居中对齐");
                break;
            default:
                mView.setGravity(RadioView.Gravity.GRAVITY_RIGHT);
                mView.setText("对齐方式：右对齐");
                break;
        }
    }
}
