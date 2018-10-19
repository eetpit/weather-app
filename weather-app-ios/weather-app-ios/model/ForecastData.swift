//
//  ForecastData.swift
//  weather-app-ios
//
//  Created by Eetu Pitkänen on 19/10/2018.
//  Copyright © 2018 Eetu Pitkänen. All rights reserved.
//

import Foundation

class ForecastData: Codable {
    let list: [WeatherData]
    
    init(list: [WeatherData]) {
        self.list = list
    }
}
