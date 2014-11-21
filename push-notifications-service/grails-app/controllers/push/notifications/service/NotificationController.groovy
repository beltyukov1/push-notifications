package push.notifications.service

import org.springframework.http.HttpStatus

class NotificationController {

    def azureService

    /***
     * Send notifications to Azure Notification Hub to send out to GCM/APNS
     * @return
     */
    def sendNotifications() {
        azureService.sendNotification(params.deviceType, params.oas)

        render status: HttpStatus.OK
    }
}
