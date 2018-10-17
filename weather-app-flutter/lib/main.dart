import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:weather-app-flutter/api/WeatherApi.dart';
import 'package:weather-app-flutter/model/ForecastData.dart';
import 'package:weather-app-flutter/model/WeatherData.dart';
import 'package:weather-app-flutter/util/LocationUtil.dart';
import 'package:weather-app-flutter/widgets/Weather.dart';
import 'package:weather-app-flutter/widgets/WeatherItem.dart';

void main() => runApp(new WeatherApp());

class WeatherApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new WeatherAppState();
  }
}

class WeatherAppState extends State<WeatherApp> {
  bool isLoading = false;
  WeatherData weatherData;
  ForecastData forecastData;

  @override
  void initState() {
    super.initState();

    loadWeather();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'weather-app-flutter',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: Scaffold(
            backgroundColor: Colors.white,
            appBar: AppBar(
              title: Text('weather-app-flutter'),
            ),
            body: Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: <Widget>[
                    Expanded(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: <Widget>[
                            Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: weatherData != null ? Weather(weather: weatherData) : Container(),
                            ),
                            Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: isLoading ? CircularProgressIndicator(
                                  strokeWidth: 2.0,
                                  valueColor: new AlwaysStoppedAnimation(Colors.white),
                                ) : IconButton(
                                  icon: new Icon(Icons.refresh),
                                  tooltip: 'Refresh',
                                  onPressed: loadWeather,
                                  color: Colors.blue,
                                )
                            )
                          ],
                        )
                    ),
                    SafeArea(
                        child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Column(
                            children: <Widget>[
                              weatherData != null ? Text(weatherData.name, style: new TextStyle(color: Colors.black, fontSize: 24.0)) : Container(),
                              Container(
                                height: 400.0,
                                child: forecastData != null ? ListView.builder(
                                    itemCount: forecastData.list.length,
                                    scrollDirection: Axis.vertical,
                                    itemBuilder: (context, index) => WeatherItem(weather: forecastData.list.elementAt(index))
                                ) : Container(),
                              )
                            ],
                          ),
                        )
                    )
                  ],
                )
            )
        )
    );
  }

  loadWeather() async {
    setState(() {
      isLoading = true;
    });

    final location = await LocationUtil.getLocation();

    if (location != null) {
      final lat = location['latitude'];
      final lon = location['longitude'];

      final weatherResponse = await WeatherApi.getWeatherData(lat.toString(), lon.toString());
      final forecastResponse = await WeatherApi.getForecastData(lat.toString(), lon.toString());

      if (weatherResponse.statusCode == 200 && forecastResponse.statusCode == 200) {
        setState(() {
          weatherData = new WeatherData.fromJson(jsonDecode(weatherResponse.body));
          forecastData = new ForecastData.fromJson(jsonDecode(forecastResponse.body));
          isLoading = false;
        });

        return;
      }
    }

    setState(() {
      isLoading = false;
    });
  }
}