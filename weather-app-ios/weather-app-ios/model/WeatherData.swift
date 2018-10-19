//
//  WeatherData.swift
//  weather-app-ios
//
//  Created by Eetu Pitkänen on 19/10/2018.
//  Copyright © 2018 Eetu Pitkänen. All rights reserved.
//

import Foundation

class WeatherData: Codable {
    let dt: Double
    let main: Main
    let name: String
    let weather: [Weather]
    
    init(dt: Double, main: Main, name: String, weather: [Weather]) {
        self.dt = dt
        self.main = main
        self.name = name
        self.weather = weather
    }
    
    class Main: Codable {
        let temp: Double
        
        init(temp: Double) {
            self.temp = temp
        }
    }
    
    class Weather: Codable {
        let main: String
        let icon: String
        
        init(main: String, icon: String) {
            self.main = main
            self.icon = icon
        }
    }
}
