import 'package:flutter/material.dart';
import 'package:weather-app-flutter/model/WeatherData.dart';
import 'package:weather-app-flutter/api/WeatherApi.dart';

class Weather extends StatelessWidget {
  final WeatherData weather;

  Weather({ Key key, @required this.weather }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Image.network(WeatherApi.getWeatherIconUrl(weather.icon)),
        Text(weather.main, style: new TextStyle(color: Colors.black, fontSize: 24.0)),
        Text('${weather.temp.toString()}${WeatherApi.CELSIUS}',  style: new TextStyle(color: Colors.black)),
      ],
    );
  }

}