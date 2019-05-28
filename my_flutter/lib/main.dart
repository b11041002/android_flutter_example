import 'package:flutter/material.dart';
import 'dart:ui';


void main() => runApp(MyApp());

class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: _getWidgetForRoute(window.defaultRouteName),
    );
  }

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
          child: Text('route22'),
        );
    }
  }
}


