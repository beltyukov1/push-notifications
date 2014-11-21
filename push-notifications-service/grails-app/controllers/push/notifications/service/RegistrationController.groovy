package push.notifications.service

import org.springframework.http.HttpStatus

class RegistrationController {

    def azureService

    /***
     * Register user with Azure Notification Hub
     * Persist user in in-memory database
     * @return
     */
    def save() {
        def user = null
        try {
            user = new User(request?.JSON)
            user.save(flush: true)
            log.debug "Persisted user ${user.dump()}"
        } catch (e) {
            log.error "Failed to persist user: ${e.message}"
            render status: HttpStatus.INTERNAL_SERVER_ERROR
        }

        def registration = azureService.registerUser(user)

        def status = registration ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR
        render status: status
    }

    /***
     * Delete registration based on registration ID path parameter
     * @return
     */
    def delete() {
        azureService.deleteRegistration(params.id)

        render status: HttpStatus.NO_CONTENT
    }

    /***
     * Delete all registrations in hub; used for testing only
     * @return
     */
    def deleteAll() {
        azureService.deleteAllRegistrations()

        render status: HttpStatus.NO_CONTENT
    }
}
