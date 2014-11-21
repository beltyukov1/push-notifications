import com.windowsazure.messaging.NotificationHub

// Place your Spring DSL code here
beans = {
    notificationHub(NotificationHub,
            grailsApplication.config.grails.notificationHubConnectionString,
            grailsApplication.config.grails.notificationHubName)
}
