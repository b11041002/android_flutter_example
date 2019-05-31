package jeno.com.myflutterapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.flutter.facade.Flutter;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterView;

public class MainActivity extends AppCompatActivity {

    EventChannel.EventSink eventSink;
    public static final String BATTERY_CHANNEL = "MyFlutterAPP.flutter.io/getBattery";
    private static final String CHARGING_CHANNEL = "MyFlutterApp.flutter/begainGetBattery";

    private static final String ANDRID_SENDTO_FLUTTER_FOR_CHANNER = "android_to_flutter/data";


    private View flutterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //android 加载flutter
//        androidInitFlutter();

        //flutter得到android的信息
//        flutterGetAndroidData();

        //android 主动发消息给
//        androidSendMegToFlutter();


        // android 通过 methodChannel 向flutter发送数据
        androidSendMsgToFlutterOfMethodChannel();


    }


    /**
     * 单纯的 android加载flutter界面
     */
    private void androidInitFlutter() {
        flutterView = Flutter.createView(MainActivity.this, getLifecycle(), "route2");
        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(flutterView, layout);
//        initRegisterFlutter();
    }


    /**
     * flutter 得到android的信息
     */
    private void flutterGetAndroidData() {
        flutterView = Flutter.createView(MainActivity.this, getLifecycle(), "route2");
        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(flutterView, layout);
        //接受Flutter的消息并将消息传递给Flutter
        getBattery();

    }


    //得到电量信息
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


    /**
     * android 向 flutter 发送消息
     */
    private void androidSendMegToFlutter() {

        flutterView = Flutter.createView(MainActivity.this, getLifecycle(), "route2");
        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(flutterView, layout);

        //注册广播
        initResisgerNetWork();
        //注册 触发给flutter发消息的时候调用
        initRegisterFlutter();
    }


    /**
     * 注册向Flutter发送信息
     */
    private void initRegisterFlutter() {
        new EventChannel((FlutterView) flutterView, CHARGING_CHANNEL).setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object o, EventChannel.EventSink eventSink) {
                // 调用flutter成功的方法
                MainActivity.this.eventSink = eventSink;
            }

            @Override
            public void onCancel(Object o) {

            }
        });
    }

    //android 7.0 后需要动态注册
    private void initResisgerNetWork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            NetBroadcastReceiver netBroadcastReceiver = new NetBroadcastReceiver();
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter);
        }
    }


    //网络监听回调
    public class NetBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                //当前网络可以用
                if (eventSink != null)
                    MainActivity.this.eventSink.success("当前网络可用");
            } else {
                //当前网络不可用
                if (eventSink != null)
                    MainActivity.this.eventSink.error("当前网络不可用", "网络断开", null);
            }

        }
    }


    /**
     * android 向flutter发送消息 通过methodChannel
     * <p>
     * 因为fluuter接收消息的时候  是在initData中需要初始化，有的时候接收不到消息
     * 解决方案是：先使用Flutter给android发送消息，在让android给flutter发送消息
     *
     * 这样姐解决了  双方通信问题
     */
    private void androidSendMsgToFlutterOfMethodChannel() {

        flutterView = Flutter.createView(MainActivity.this, getLifecycle(), "向android发送消息");
        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(flutterView, layout);


        //android 端向 flutter发送消息
        // showAndroidInfo-> android 向 flutter 发送的消息
        // otherParams->    用于 Android 需要给 Flutter 传递的额外数据。

        new MethodChannel((FlutterView) flutterView, ANDRID_SENDTO_FLUTTER_FOR_CHANNER).invokeMethod("showAndroidInfo", "otherParams", new MethodChannel.Result() {
            @Override
            public void success(@Nullable Object o) {
                //如果发送成功的回调
                Log.i("回调", "success");
            }

            @Override
            public void error(String s, @Nullable String s1, @Nullable Object o) {
                //如果发送失败的回调
                Log.i("回调", "error");
            }

            @Override
            public void notImplemented() {
                // Flutter 未实现对应方法
                Log.i("回调", "notImplemented");
            }
        });


    }


}
