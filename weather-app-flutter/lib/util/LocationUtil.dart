import 'package:flutter/services.dart';
import 'package:location/location.dart';

class LocationUtil {

  static final Location _location = new Location();

  static getLocation() {
    try {
      return _location.getLocation();
    } on PlatformException catch (e) {
      if (e.code == 'PERMISSION_DENIED') {
        print('Permission denied');
      } else if (e.code == 'PERMISSION_DENIED_NEVER_ASK') {
        print('Permission denied - please ask the user to enable it from the app settings');
      }

      return null;
    }
  }
}