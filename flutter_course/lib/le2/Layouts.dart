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
        leading: const Icon(Icons.android_sharp),
        title: const Text('App Title'),
        backgroundColor: Colors.teal,
        elevation: 4,
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

            containerQuiz()
        /*******************--[focus here 🧐]--*******************/
        );
  }

  Container containerQuiz() {
    return Container(
        color: Colors.yellowAccent,
        height: 400,
        child: Center(
          child: Icon(
            Icons.done,
            size: 100,
          ),
        ),
      );
  }

  Container containerQize() {
    return Container(
        // color: Colors.blue,
        width: 300,
        height: 100,
        decoration: BoxDecoration(
          color: Colors.blue,
          border: Border.all(color: Colors.black, width: 5),
          // shape: BoxShape.circle,
        ),
        child: Center(
          child: Text(
            "Qize Container",
            style: TextStyle(fontSize: 30, fontFamily: 'casual'),
          ),
        ));
  }

  Container containerExample() {
    return Container(
      // color: Colors.blue,
      width: 300,
      height: 200,
      decoration: BoxDecoration(
        color: Colors.amber,
        border: Border.all(color: Colors.black54, width: 3),
        // shape: BoxShape.circle,
      ),
      // space from the parent and it's child
      padding: EdgeInsets.only(top: 100, left: 50),
      // space from parents to anythings surrounding it
      margin: EdgeInsets.only(top: 100, left: 50),
      child: Text(
        "Hello Container",
        style: TextStyle(fontSize: 30),
      ),
    );
  }
}
