//
//  ViewController.swift
//  weather-app-ios
//
//  Created by Eetu Pitkänen on 19/10/2018.
//  Copyright © 2018 Eetu Pitkänen. All rights reserved.
//

import UIKit
import SDWebImage
import CoreLocation
import Alamofire
import os.log

class ViewController: UIViewController, CLLocationManagerDelegate {
    
    @IBOutlet weak var weatherIcon: UIImageView!
    @IBOutlet weak var weatherDescription: UILabel!
    @IBOutlet weak var temperatureText: UILabel!
    @IBOutlet weak var locationName: UILabel!
    
    var locationManager : CLLocationManager!
    
    @IBAction func refreshClick(_ sender: Any) {
        getLocation()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        weatherIcon.sd_setImage(with: URL(string: "https://openweathermap.org/img/w/01d.png"))
        setupLocationManager()
    }
    
    private func setupLocationManager() {
        locationManager = CLLocationManager()
        locationManager.requestWhenInUseAuthorization()
        locationManager.delegate = self
        
        getLocation()
    }
    
    private func getLocation() {
        if CLLocationManager.authorizationStatus() != .authorizedWhenInUse {
            return
        }
        
        if !CLLocationManager.locationServicesEnabled() {
            return
        }
        
        locationManager.requestLocation()
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let lastLocation = locations.last!
        let latitude = lastLocation.coordinate.latitude
        let longitude = lastLocation.coordinate.longitude
        
        fetchWeatherData(latitude, longitude)
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Swift.Error) {
        os_log("Fetching location failed.", log: OSLog.default, type: .error)
    }
    
    private func fetchWeatherData(_ latitude: Double, _ longitude: Double) {
        Alamofire.request("https://api.openweathermap.org/data/2.5/weather?APPID=0613fdf35a1e7d5548e0d2b3b11296cb&lat=\(latitude)&lon=\(longitude)&units=metric")
            .responseJSON { response in
                if let data = response.data {
                    let weatherData = try! JSONDecoder().decode(WeatherData.self, from: data)
                    self.setWeatherData(weatherData)
                }
            }
    }
    
    private func setWeatherData(_ data: WeatherData) {
        weatherIcon.sd_setImage(with: URL(string: "https://openweathermap.org/img/w/\(data.weather[0].icon).png"))
        weatherDescription.text = data.weather[0].main
        temperatureText.text = "\(data.main.temp)°C"
        locationName.text = data.name
    }

}

