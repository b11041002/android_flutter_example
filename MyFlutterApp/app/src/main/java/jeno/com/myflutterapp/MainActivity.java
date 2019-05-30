package jeno.com.myflutterapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.flutter.facade.Flutter;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterView;

public class MainActivity extends AppCompatActivity {


    public static final String BATTERY_CHANNEL = "MyFlutterAPP.flutter.io/getBattery";
    private View flutterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flutterView = Flutter.createView(MainActivity.this, getLifecycle(), "route2");
                FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                addContentView(flutterView, layout);
                getBattery();
            }
        });

    }


    public int getBattery() {
        new MethodChannel((FlutterView) flutterView, BATTERY_CHANNEL).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                //  methodCall 用来区分不同方法的调用
                if (methodCall.method.equals("getBatteryLevel")) {
                    //如果flutter调用的方法是：getBatteryLevel 那么执行
                    //返回成功的回调方法
                    if (true)
                        result.success("100%电量");
                    else {
                        result.error("错误", "", null);
                    }
                } else {
                    //其他方法的调用 表明没有对应实现。
                    result.notImplemented();
                }
            }
        });
        return 0;
    }


}
