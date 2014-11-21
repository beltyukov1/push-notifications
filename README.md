Push Notifications Spike
==================
This repository is a spike of push notifications. It contains the following projects:
- Push notifications service
- Android client
- iOS client

#Push notifications service
Web service (written in Grails) that talks to Microsoft Azure Notification Hub to achieve the following:
- Register devices for push notifications (Android and iOS)
- Send push notifications (Android and iOS)
- Delete registrations

#Android client
Sample Android application that registers with Google (Google Cloud Messaging) and sends registration ID to Azure via push notifications service.

#iOS client
Sample iOS application that registers with Apple (Apple Push Notifications Service) and sends registration ID to Azure via push notifications service.
