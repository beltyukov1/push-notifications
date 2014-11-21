package push.notifications.service

import wslite.rest.ContentType
import wslite.rest.RESTClient

class ParseService {

    private static final String APPLICATION_ID = 'HLBGPBRR2cPfMmdJIjWAsA4DFy10irE6qv9AH6VW'
    private static final String REST_API_KEY = 'bXkJrl5SRQUHWXAQiQO5Qu2LxykDtDHJm2Farg8Z'

    static transactional = false

    def parseClient = new RESTClient('https://api.parse.com/1/')

    def registerUserWithParse(String token) {
        println "TOKEN: $token"

        def response = parseClient.post(
                path: 'installations',
                headers: ['X-Parse-Application-Id': APPLICATION_ID, 'X-Parse-REST-API-Key': REST_API_KEY]) {
            type ContentType.JSON
            json deviceType: 'android', pushType: 'gcm', channels: ['']
        }

        println "${new Date()}: status: ${response.statusCode}"

        response.statusCode
    }

    def sendTestNotification() {
        def response = parseClient.post(
                path: 'push',
                headers: ['X-Parse-Application-Id': APPLICATION_ID, 'X-Parse-REST-API-Key': REST_API_KEY]) {
            type ContentType.JSON
            json channels: ['broadcast', ''], data: [alert: "testing"]
        }

        println "${new Date()}: status: ${response.statusCode}"

        response.statusCode
    }
}
