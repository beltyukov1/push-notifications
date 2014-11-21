package push.notifications.service

import com.windowsazure.messaging.AppleRegistration
import com.windowsazure.messaging.GcmRegistration
import com.windowsazure.messaging.Notification
import com.windowsazure.messaging.NotificationHub
import com.windowsazure.messaging.Registration
import grails.transaction.Transactional
import groovy.json.JsonBuilder

@Transactional
class AzureService {

    def notificationHub
    private static final String ANDROID = 'android'
    private static final String IOS = 'ios'

    /***
     * Registers user with Azure Notification Hubs service
     * @param user
     * @return
     */
    Registration registerUser(User user) {
        // Get new registration ID from Notification Hub
        def registrationId
        try {
            registrationId = notificationHub.createRegistrationId()
            log.debug "Retrieved new registration ID $registrationId from Notification Hub"
        } catch (e) {
            log.error "Failed to create registration ID: ${e.message}"
            return null
        }

        // Create registration object based on device type
        def registration = null
        try {
            if (user.deviceType == ANDROID) {
                registration = new GcmRegistration(registrationId, user.token)
            } else if (user.deviceType == IOS) {
                registration = new AppleRegistration(registrationId, user.token)
            }

            if (!registration) {
                return null
            }

            log.debug "Created registration object"
        } catch (e) {
            log.error "Failed to create registration object: ${e.message}"
            return null
        }

        // Add tag to registration object (dummy one account ID in our case)
        try {
            registration.tags.add(user.oneAccountId as String)
            log.debug "Added ${user.oneAccountId} tag to registration"
        } catch (e) {
            log.error "Failed to add tag: ${e.message}"
            return null
        }

        // Save registration object to Notification Hub
        def upsertedRegistration
        try {
            upsertedRegistration = notificationHub.upsertRegistration(registration)
            log.debug "Registered ${user.deviceType} device with Notification Hub"
        } catch (e) {
            log.error "Failed to register ${user.deviceType} device: ${e.message}"
            return null
        }

        upsertedRegistration
    }

    /***
     * Sends notification to Azure, which then sends it to GCM/APNS
     * @param deviceType
     * @param oas
     */
    void sendNotification(String deviceType, String oas) {
        // Create notification object based on device type
        def notification = null
        try {
            if (deviceType == ANDROID) {
                def messageMap = [
                        data: [
                                msg: "Android ${new Date()}"
                        ]
                ]
                def message = new JsonBuilder(messageMap).toPrettyString()
                notification = Notification.createGcmNotifiation(message)
            } else if (deviceType == IOS) {
                def messageMap = [
                        aps: [
                                alert: "iOS ${new Date()}"
                        ]
                ]
                def message = new JsonBuilder(messageMap).toPrettyString()
                notification = Notification.createAppleNotifiation(message)
            }

            if (!notification) {
                return
            }

            log.debug "Created notification for $deviceType device"
        } catch (e) {
            log.error "Failed to create notification for $deviceType device: ${e.message}"
            return
        }

        // Send notification to Notification Hub
        try {
            notificationHub.sendNotification(notification, oas)
            log.debug "Sent notification with body ${notification.body} and OAS $oas to $deviceType device"
        } catch (e) {
            log.error "Failed send notification with body ${notification.body} and OAS $oas to $deviceType device: ${e.message}"
        }
    }

    /***
     * Delete registration based on registration ID
     * @param registrationId
     */
    void deleteRegistration(String registrationId) {
        try {
            notificationHub.deleteRegistration(registrationId)
            log.debug "Deleted registration with ID $registrationId"
        } catch (e) {
            log.error "Failed to delete registration with ID $registrationId: ${e.message}"
        }
    }

    /***
     * Delete all registration records; used only for testing purposes
     */
    void deleteAllRegistrations() {
        try {
            def registrationsCollection = notificationHub.getRegistrations()
            registrationsCollection.registrations.each { registration ->
                notificationHub.deleteRegistration(registration)
            }
            log.debug "Delete all registrations"
        } catch (e) {
            log.error "Failed to delete all registrations: ${e.message}"
        }
    }
}
