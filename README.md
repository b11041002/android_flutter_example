##### 创建android项目、创建依赖flutter项目
- android项目创建
> 在路径为 `E:\android demo\MyFlutterApp` 创建 `MyFlutterApp` android项目
- 在相同路径下创建Flutter依赖库

在android项目目录下执行：`flutter create -t module my_flutter`生成flutter依赖。得到如下图：

![flutter原生交互目录.jpg](https://upload-images.jianshu.io/upload_images/2953304-be09aa8092cd1ce0.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


##### 在android项目根目录添加设置依赖
> 在 settings.gradle 中添加依赖 


```Java
include ':app'
//如下是添加的依赖
setBinding(new Binding([gradle: this]))
evaluate(new File(
        settingsDir.parentFile,
        'my_flutter/.android/include_flutter.groovy'
))
```
**`my_flutter/.android/include_flutter.groovy` 依赖的路径**
 
##### 在app/build.gradle 中添加项目的引用
 ```Java
 dependencies {
  ...
  //引入flutter 模块
  implementation project(':flutter')
}
 ```
##### 

##### builder项目，这时候可能会出现是否版本的一致性的问题，进行解决。
**我这里遇到了一个这种问题：`Error:com.android.tools.r8.utils.AbortException: Error: Invoke-customs are only supported starting with Android O (--min-api 26)`**

解决：在项目的`app/build.gradle`添加如下配置：
```Java
android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```
##### 代码中引入布局
```Java
//得到一个View类 
// routel 路由类  用来区分不同 Flutter 页面
View flutterView = Flutter.createView(MainActivity.this, getLifecycle(),"routel");
FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
addContentView(flutterView, layout);
```
 
 ![abc.gif](https://upload-images.jianshu.io/upload_images/2953304-7b544061e8a64e98.gif?imageMogr2/auto-orient/strip)

 现在就实现了在android项目中加载flutter代码。
 。
 
 
#####  android 加载不同的界面
- routel
 
当调用flutter代码的时候，根据不同的值加载不同的界面
 
```View flutterView = Flutter.createView(MainActivity.this, getLifecycle(),"routel");```
 
下面是flutter的代码部分

根据不同的路由加载不同的界面

```Java
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
          child: Text('route1'),
        );
      case 'route2':
        return new Center(
          child: Text('route2'),
        );
    }
  }
}
```

##### flutter的热加载 
> 当我们的android项目加载了flutter代码后怎么热更新呢？
- 先将路径定位到flutter代码路径下：`E:\android demo\MyFlutterApp\my_flutter>`
- 执行操作`flutter attach`

出现：`Waiting for a connection from Flutter on Android SDK built for x86...`
然后我们直接运行或者以 debug 模式运行项目。  
接着点击按钮，触发 Flutter 代码，会看到控制台输出
```Java
🔥  To hot reload changes while running, press "r". To hot restart (and rebuild state), press "R".
An Observatory debugger and profiler on Android SDK built for x86 is available at: http://127.0.0.1:60985/
For a more detailed help message, press "h". To detach, press "d"; to quit, press "q".
```
表明已经可以进行热更新操作了。

**除了直接运行旧有项目来启动 Flutter 之外，其实更多时候我们编写 Flutter 是独立的，可以直接运行 Flutter 来调试和修改 dart 代码。
我一般倾向于直接执行 flutter run，而不是按照官网那样通过 flutter attach，然后以 debug 模型启动旧有项目。
等到 Flutter Module 都调试 OK 之后，再和旧有项目一起运行查看效果。**







文章参考：https://mp.weixin.qq.com/s/OGbH3G3wHVTUt-0EJit8RA

代码地址：https://github.com/songjiabin/android_flutter_example
