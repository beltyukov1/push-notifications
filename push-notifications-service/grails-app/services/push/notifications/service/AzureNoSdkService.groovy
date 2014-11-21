package push.notifications.service

import grails.transaction.Transactional
import wslite.rest.RESTClient
import push.notifications.auth.AuthGenerator;

@Transactional
class AzureNoSdkService {

    def azureClient = new RESTClient("https://notification-hub-spike-ns.servicebus.windows.net/notification-hub-spike")

    //=== REGISTER USER ===
    void registerUser(User user) {
        if (user.deviceType == 'android') {
            registerUserAndroid(user)
        } else if (user.deviceType == 'ios') {
            registerUseriOS(user)
        }
    }

    private void registerUserAndroid(User user) {
        def response = azureClient.post(
                path: '/registrations/?api-version=2013-08',
                headers: [
                        'Authorization': AuthGenerator.generateAuthToken(),
                        'Content-Type' : 'application/atom+xml;type=entry;charset=utf-8',
                        'x-ms-version' : '2013-08'
                ]
        ) {
            xml {
                entry('xmlns': 'http://www.w3.org/2005/Atom') {
                    content('type': 'application/xml') {
                        GcmRegistrationDescription('xmlns:i': 'http://www.w3.org/2001/XMLSchema-instance',
                                'xmlns': 'http://schemas.microsoft.com/netservices/2010/10/servicebus/connect') {
                            Tags(user.oneAccountId)
                            GcmRegistrationId(user.token)
                        }
                    }
                }
            }
        }

        println "${new Date()}: Android registration status: ${response.statusCode}"
    }

    private void registerUseriOS(User user) {
        def response = azureClient.post(
                path: '/registrations/?api-version=2013-08',
                headers: [
                        'Authorization': AuthGenerator.generateAuthToken(),
                        'Content-Type' : 'application/atom+xml;type=entry;charset=utf-8',
                        'x-ms-version' : '2013-08'
                ]
        ) {
            xml {
                entry('xmlns': 'http://www.w3.org/2005/Atom') {
                    content('type': 'application/xml') {
                        AppleRegistrationDescription('xmlns:i': 'http://www.w3.org/2001/XMLSchema-instance',
                                'xmlns': 'http://schemas.microsoft.com/netservices/2010/10/servicebus/connect') {
                            Tags(user.oneAccountId)
                            DeviceToken(user.token)
                        }
                    }
                }
            }
        }

        println "${new Date()}: iOS registration status: ${response.statusCode}"
    }


    //=== SEND NOTIFICATION ===
    void sendNotification(String deviceType, String oas) {
        if (deviceType == 'android') {
            sendAndroidNotification(oas)
        } else if (deviceType == 'ios') {
            sendiOSNotification(oas)
        }
    }

    private void sendAndroidNotification(String oas) {
        def response = azureClient.post(
                path: '/messages/?api-version=2013-08',
                headers: [
                        'Authorization': AuthGenerator.generateAuthToken(),
                        'Content-Type' : 'application/json;charset=utf-8',
                        'ServiceBusNotification-Tags': oas,
                        'ServiceBusNotification-Format': 'gcm'
                ]
        ) {

            json data: [
                msg: "Sent at ${new Date()}"
            ]
        }

        println "${new Date()}: Android notification status: ${response.statusCode}"
    }

    private void sendiOSNotification(String oas) {
        def response = azureClient.post(
                path: '/messages/?api-version=2013-08',
                headers: [
                        'Authorization': AuthGenerator.generateAuthToken(),
                        'Content-Type' : 'application/json;charset=utf-8',
                        'ServiceBusNotification-Tags': oas,
                        'ServiceBusNotification-Format': 'apple',
                        'ServiceBusNotification-ApnsExpiry': '2015-11-16T19:20+01:00'
                ]
        ) {

            json aps: [
                    alert: "Sent at ${new Date()}",
                    badge: 1
            ]
        }

        println "${new Date()}: Apple notification status: ${response.statusCode}"
    }
}
