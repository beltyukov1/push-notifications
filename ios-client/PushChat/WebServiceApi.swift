//
//  WebServiceApi.swift
//  PushChat
//
//  Created by Alex Beltyukov on 11/20/14.
//  Copyright (c) 2014 Alex Beltyukov. All rights reserved.
//

import Foundation
import Alamofire

class WebServiceApi {
    
    class func registerUserWith(registrationId: NSString) {
        let basePath = "https://vfrmrhqbho.localtunnel.me/push-notifications-service"
        let postBody = [
            "token": registrationId,
            "oneAccountId": 2,
            "deviceType": "ios"
        ]
        
        println("Getting ready to register user")
        
        Alamofire
            .request(
                .POST,
                basePath + "/registration",
                parameters: postBody,
                encoding: .JSON)
            .response { (request, response, data, error) in
                println(request)
                println(response)
                println(error)
        }
        
    }
}
