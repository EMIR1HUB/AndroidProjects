import 'package:flutter/material.dart';

void main() => runApp(MyApp());

/// this is your APP Main screen configuration
class MyApp extends StatelessWidget {
  MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: MyHomePage(),
    );
  }
}

/// this is a template to start building a UI
/// to start adding any UI you want change what comes after the [ body: ] tag below
class MyHomePage extends StatelessWidget {
  MyHomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xfff4f4f4),
      /*******************--[focus here 🧐]--*******************/
      appBar: AppBar(
        leading: const Icon(Icons.favorite_border),
        title: const Text('Flutter App'),
        backgroundColor: Colors.teal,
        elevation: 5,
      ),
      body: myWidget(),
      /*******************--[focus here 🧐]--*******************/
    );
  }

  Widget myWidget() {
    return Container(
        padding: EdgeInsets.all(20),
        child:
            /*******************--[focus here 🧐]--*******************/

            iconButtonSplashColor()

        /*******************--[focus here 🧐]--*******************/
        );
  }


  // Lecture First -> all Quiz and Example

  IconButton iconButtonSplashColor() {
    return IconButton(
        onPressed: () {
          print("present");
        },
        splashColor: Colors.greenAccent,
        icon: Icon(Icons.add),
        iconSize: 100,
        padding: EdgeInsets.all(16),
      );
  }

  Image imageExapmle() {
    return Image.asset(
      "assets/gift.png",
      height: 300,
      fit: BoxFit.cover,
    );
  }

  ElevatedButton elevatedButtonExample() {
    return ElevatedButton(
      child: Text(
        "Press now",
        style: TextStyle(
            color: Colors.yellowAccent,
            fontSize: 30,
            fontFamily: 'casual',
            fontWeight: FontWeight.w700),
      ),
      style: ElevatedButton.styleFrom(
        padding: EdgeInsets.symmetric(horizontal: 100, vertical: 10),
        shape: StadiumBorder(),
      ),
      onPressed: () {
        print("press me");
      },
    );
  }

  Icon iconExample() {
    return Icon(
      Icons.whatshot_outlined,
      color: Colors.deepOrange,
      size: 200,
      textDirection: TextDirection.rtl,
    );
  }

  Text textQuiz() {
    return Text('Your first Quiz',
        style: TextStyle(
            color: Colors.teal,
            backgroundColor: Colors.yellowAccent,
            fontSize: 40,
            fontWeight: FontWeight.w700,
            fontFamily: 'casual',
            fontStyle: FontStyle.italic));
  }

  Text textExample() {
    return Text(
      'Hello, welcome to my app',
      style: TextStyle(
          fontSize: 40,
          color: Color(0xFF0002FF),
          fontFamily: 'monospace',
          fontWeight: FontWeight.w900,
          letterSpacing: 4,
          backgroundColor: Colors.greenAccent),
    );
  }
}
