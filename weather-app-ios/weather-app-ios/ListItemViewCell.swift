//
//  ListItemViewCell.swift
//  weather-app-ios
//
//  Created by Eetu Pitkänen on 19/10/2018.
//  Copyright © 2018 Eetu Pitkänen. All rights reserved.
//

import UIKit

class ListItemViewCell: UITableViewCell {
    
    @IBOutlet weak var dateText: UILabel!
    @IBOutlet weak var icon: UIImageView!
    @IBOutlet weak var temperature: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
