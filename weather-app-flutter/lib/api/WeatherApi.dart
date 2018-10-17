import 'package:http/http.dart' as http;

class WeatherApi {

  static const END_POINT = 'https://api.openweathermap.org/data/2.5';
  static const APP_ID = '0613fdf35a1e7d5548e0d2b3b11296cb';
  static const IMAGE_END_POINT = 'https://openweathermap.org/img/w/';
  static const FILE_TYPE = '.png';
  static const CELSIUS = '°C';

  static getWeatherData(final lat, final lon) {
    return http.get('$END_POINT/weather?APPID=$APP_ID&lat=$lat&lon=$lon&units=metric');
  }

  static getForecastData(final lat, final lon) {
    return http.get('$END_POINT/forecast?APPID=$APP_ID&lat=$lat&lon=$lon&units=metric');
  }

  static getWeatherIconUrl(final icon) {
    return '$IMAGE_END_POINT$icon$FILE_TYPE';
  }
}