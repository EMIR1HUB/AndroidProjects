import 'package:flutter/material.dart';

void main() => runApp(MyApp());

/// this is your APP Main screen configuration
// ignore: prefer_const_constructors
class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

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
  const MyHomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xfff4f4f4),
      /*******************--[focus here 🧐]--*******************/
      appBar: AppBar(
        leading: const Icon(Icons.stars),
        title: const Text('Lab 1'),
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
            Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            textWelcome(),
            Center(
              child: Column(
                children: [
                  SizedBox(height: 20),
                  iconShare(),
                  SizedBox(height: 20),
                  buttonClickHere(),
                  SizedBox(height: 20),
                  iconFavorite(),
                  SizedBox(height: 20),
                  buttonToPress(),
                  SizedBox(height: 20),
                  containerBox(),
                ],
              ),
            ),
          ],
        )
        /*******************--[focus here 🧐]--*******************/
        );
  }

  Icon iconShare() {
    return Icon(
      Icons.share,
      color: Colors.blue,
      size: 150,
    );
  }

  ElevatedButton buttonClickHere() {
    return ElevatedButton(
      onPressed: () {
        print("press me");
      },
      child: Text(
        "Click here",
        style: TextStyle(
          color: Colors.yellowAccent,
          fontSize: 30,
          fontWeight: FontWeight.w500,
        ),
      ),
      style: ElevatedButton.styleFrom(
        padding: EdgeInsets.symmetric(horizontal: 80, vertical: 10),
        shape: StadiumBorder(),
      ),
    );
  }

  Container iconFavorite() {
    return Container(
      padding: EdgeInsets.all(20),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(100),
        color: Colors.black,
      ),
      child: Icon(Icons.favorite, color: Colors.greenAccent, size: 30),
    );
  }

  ElevatedButton buttonToPress() {
    return ElevatedButton(
      onPressed: () {
        print("text button to press");
      },
      child: Text(
        "Button to press",
        style: TextStyle(
          color: Colors.redAccent,
          fontSize: 30,
          fontWeight: FontWeight.w500,
        ),
      ),
      style: ElevatedButton.styleFrom(
        backgroundColor: Colors.white,
        padding: EdgeInsets.symmetric(horizontal: 80, vertical: 20),
        side: const BorderSide(
          width: 4,
          color: Colors.amberAccent,
        ),
      ),
    );
  }

  Container containerBox() {
    return Container(
      width: 250,
      height: 250,
      decoration: BoxDecoration(
        color: Colors.yellow,
        border: Border(
          right: BorderSide(color: Colors.purple, width: 10),
          // Правая граница (фиолетовая)
          left: BorderSide(color: Colors.blue, width: 10),
          // Левая граница (синяя)
          top: BorderSide(color: Colors.cyan, width: 10),
          // Верхняя граница (голубая)
          bottom: BorderSide(
              color: Colors.red, width: 10), // Нижняя граница (красная)
        ),
      ),
      child: Center(
        child: Icon(
          Icons.warning,
          size: 50,
        ),
      ),
      /*******************--[focus here 🧐]--*******************/
    );
  }

  Image imageLogo() {
    return Image.asset(
      "assets/flutter_logo.png",
      height: 100,
    );
  }

  Column firstExercise() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      // Выравнивание по левому краю
      children: [
        iconShare(),
        SizedBox(height: 20),
        buttonClickHere(),
        SizedBox(height: 20),
        textWelcome(),
        SizedBox(height: 20),
        // Расстояние 10 пикселей между текстом и картинкой
        imageLogo(),
        SizedBox(height: 20),
        // Расстояние 10 пикселей между текстом и картинкой
        imageLogo(),
        SizedBox(height: 20),
        // Расстояние 10 пикселей между текстом и картинкой
        imageLogo(),
        SizedBox(height: 20),
        // Расстояние 10 пикселей между текстом и картинкой
        imageLogo(),
      ],
    );
  }

  Text textWelcome() {
    return Text(
      "Welcome to lab1",
      style: TextStyle(
        fontSize: 36,
        fontStyle: FontStyle.italic,
        color: Colors.greenAccent,
        backgroundColor: Colors.grey,
        fontWeight: FontWeight.w700,
        fontFamily: 'monospace',
      ),
    );
  }



}
