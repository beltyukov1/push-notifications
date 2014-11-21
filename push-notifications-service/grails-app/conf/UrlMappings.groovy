class UrlMappings {

	static mappings = {
        "/registrations"(resources: 'registration')
        "/notifications/send"(controller: 'notification') {
                action = [POST: 'sendNotifications']
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
