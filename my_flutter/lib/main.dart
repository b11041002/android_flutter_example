import 'package:flutter/material.dart';
import 'dart:ui';
import 'package:flutter/services.dart';


void main() =>
    runApp(new MaterialApp(
      home: MyApp(),
    ));


class MyApp extends StatefulWidget {
  @override
  MyAppState createState() => new MyAppState();
}

class MyAppState extends State<MyApp> {


  @override
  void initState() {
    super.initState();
    //类似于接受广播类似的 在这里注册
    _eventChannel.receiveBroadcastStream().listen(
        _eventSuccessForAndroid, onError: _eventErrorForAndroid,
        onDone: _eventDoneForAndroid);

    //android 向 flutter 通信
    _evnet_getandroid_methodChannel.setMethodCallHandler((
        MethodCall methodCall) {
      if (methodCall.method == "showAndroidInfo") {
        //如果android 想调用 showAndroidInfo 这个方法的话
        print('出发');
        setState(() {
          _fromAndroidInfo = methodCall.arguments;
        });
      }
    });
  }


  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      body: _getWidgetForRoute(
          window.defaultRouteName),
    );
  }


  //和原生进行交互的时候 双方定义好的约定好的变量
  static const MethodChannel _methodChannel = MethodChannel(
      'MyFlutterAPP.flutter.io/getBattery');

  //一开始就得到android发送过来的信息
  static const EventChannel _eventChannel = EventChannel(
      'MyFlutterApp.flutter/begainGetBattery');


  static const MethodChannel _evnet_getandroid_methodChannel = MethodChannel(
      "android_to_flutter/data");


  String _batteryLebvel = '电量是 ：';

  //从android过来的消息
  String _froAndroidBatteryLebvel = '还没过来呢...';

  String _fromAndroidInfo = '从android端通过MethodChannel传递过来的数据';


  //根据 Route得到相应的布局
  //当android调用的时候根据 route 得到相应的布局
  Widget _getWidgetForRoute(String route) {
    switch (route) {
      case 'route1':
        return new Center(
          child: Text('route11'),
        );
      case 'route2':
        return new Center(
          child: Text('route223'),
        );
      case '电量':
        return _getBatteryLebvelWidget();
      case '主动显示原生发送的消息':
        return _getAndroidBeginInfo();
      case '向android发送消息':
        return _getInfoByAndroidWidget();
    }
  }


  Widget _getInfoByAndroidWidget() {
    return new Center(
      child: Text(_fromAndroidInfo),
    );
  }


  //主动从android得到点了 并显示
  Widget _getBatteryLebvelWidget() {
    return
      Scaffold(
          body: Center(
            child: new Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(_batteryLebvel),
                IconButton(
                    icon: Icon(Icons.refresh, color: Colors.blue,),
                    onPressed: () {
                      //点击进行刷新
                      _pressGetBattery();
                    }),
                Text(_froAndroidBatteryLebvel)
              ],
            ),
          ));
  }


  //得到原生的电量
  _pressGetBattery() async {
    String battery;
    //相当于调用原生中的方法 获取电量
    try {
      final String result = await _methodChannel.invokeMethod(
          'getBatteryLevel');
      print('点了' + result.toString());
      battery = '电量是：$result';
    } on PlatformException {
      battery = '获取电量失败';
    } catch (e) {
      print(e);
    }


    setState(() {
      _batteryLebvel = battery;
    });
  }


  //开始就加载数据   从android 端得到消息  接收android端的消息
  Widget _getAndroidBeginInfo() {
    return new Center(
      child: Text(_froAndroidBatteryLebvel,
        style: TextStyle(fontSize: 20, color: Colors.blue),),
    );
  }


  //接受android的消息的内容
  void _eventSuccessForAndroid(Object object) {
    setState(() {
      _froAndroidBatteryLebvel = object;
    });
  }

  /**
   * 接受android的消息错误的时候
   * 这个也是原生发送过来的错误信息
   */
  void _eventErrorForAndroid(Object error) {
    setState(() {
      PlatformException exception = error;
//      _froAndroidBatteryLebvel =
//          exception?.message ?? 'Battery status: unknown.';
      _froAndroidBatteryLebvel = exception.message;
    });
  }

  /**
   * 当接受的到的消息完成后
   */
  void _eventDoneForAndroid() {
    setState(() {
      _froAndroidBatteryLebvel = '完成！';
    });
  }


}





