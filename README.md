Push Notifications Spike
==================
This repository is a spike of push notifications. It contains the following projects:
- Push notifications service
- Android client
- iOS client

#Push notifications service
Web service (written in Grails) that talks to Microsoft Azure Notification Hub to achieve the following:
- Register devices for push notifications
- Send push notifications
- Delete registrations
This service utilizes a Java wrapper around the REST interface provided by Microsoft. It also contains a class (AzureNoSdkService.groovy) which registers devices and sends push notifications without the use of the Notification Hub SDK.

#Android client
Android application that registers with Google (Google Cloud Messaging (GCM)) and sends registration ID to Azure via push notifications service.
This application is also able to receive and display notifications sent by Azure Notification Hub (via GCM).

#iOS client
iOS application that registers with Apple (Apple Push Notifications Service (APNS)) and sends registration ID to Azure via push notifications service.
This application is also able to receive and display notifications sent by Azure Notification Hub (via APNS).
