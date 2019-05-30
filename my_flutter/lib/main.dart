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
  Widget build(BuildContext context) {
    return new Scaffold(
      body: _getWidgetForRoute(
          window.defaultRouteName == null ? 'route1' : '电量'),
    );
  }


  //和原生进行交互的时候 双方定义好的约定好的变量
  static const MethodChannel _methodChannel = MethodChannel('MyFlutterAPP.flutter.io/getBattery');
  String _batteryLebvel = '电量是 ：';


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
    }
  }

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
                    })
              ],
            ),
          ));
  }


  //得到原生的电量
  _pressGetBattery() async {
    String battery;
    //相当于调用原生中的方法 获取电量
    try {
      final String result = await _methodChannel.invokeMethod('getBatteryLevel');
      print('点了'+result.toString());
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

}





