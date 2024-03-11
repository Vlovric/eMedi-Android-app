package hr.foi.rampu.emedi.helpers

import hr.foi.rampu.emedi.entities.User

object UserSession {
    var loggedIn: Boolean = false
    var loggedUser: User = MockDataUser.getDummyUser()
}