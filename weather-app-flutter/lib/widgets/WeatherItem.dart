import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:weather-app-flutter/model/WeatherData.dart';
import 'package:weather-app-flutter/api/WeatherApi.dart';

class WeatherItem extends StatelessWidget {
  final WeatherData weather;

  WeatherItem({ Key key, @required this.weather }) : super(key: key);

  @override
  Widget build(BuildContext context) {

    return ListTile(
        contentPadding: EdgeInsets.symmetric(horizontal: 20.0, vertical: 10.0),
        leading: Container(
          padding: EdgeInsets.only(right: 12.0),
          child: Text(new DateFormat.yMMMd().format(weather.date), style: new TextStyle(color: Colors.black)),
        ),
        title: Column(
          mainAxisAlignment: MainAxisAlignment.end,
          children: <Widget>[
            Image.network(WeatherApi.getWeatherIconUrl(weather.icon))
          ],
        ),
        trailing:
        Text('${weather.temp.toString()}${WeatherApi.CELSIUS}',  style: new TextStyle(color: Colors.black))
    );
  }

}