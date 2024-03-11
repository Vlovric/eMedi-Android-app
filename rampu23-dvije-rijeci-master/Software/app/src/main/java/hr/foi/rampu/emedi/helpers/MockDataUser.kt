package hr.foi.rampu.emedi.helpers

import android.util.Log
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.entities.User
import java.util.Date

object MockDataUser {
    val userList = arrayOf<User>(
        User(1,
            "Pero",
            "Perić",
            Date(1990, 5, 15),
            "email1@example.com",
            "1234567890",
            "Adresa 1",
            "ja",
            "ti"),
        User(2,
            "Vedran",
            "Galić",
            Date(1985, 8, 20),
            "email2@example.com",
            "0987654321",
            "Adresa 2",
            "korisnik2",
            "lozinka2")
    )

    fun loadUsers() {
        val usersDAO = AppDatabase.getInstance().getUsersDao()

        if (usersDAO.getAllUsers().isEmpty()) {
            usersDAO.insertUser(*userList)
        }
    }

    fun getUsers(): List<User> {
        val users = AppDatabase.getInstance().getUsersDao().getAllUsers()
        return users.toList()
    }

    fun insertUser(user: User) {
        val usersDAO = AppDatabase.getInstance().getUsersDao()
        Log.i("TEST", "Do ovdje sam dosao")
        usersDAO.insertUser(user)
    }

    fun getUserCount(): Int {
        return AppDatabase.getInstance().getUsersDao().getAllUsers().count()
    }

    fun findUserByCredentials(username: String, password: String): User {
        return AppDatabase.getInstance().getUsersDao().getUserByCredentials(username, password)
    }

    fun getUserById(id: Int): User {
        return AppDatabase.getInstance().getUsersDao().getUser(id)
    }

    fun getNewUserId(): Int {
        val usersDAO = AppDatabase.getInstance().getUsersDao()
        val allUsers = usersDAO.getAllUsers()
        var newId = allUsers.count()

        for (user in allUsers) {
            if (user.id > newId) {
                newId = user.id
            }
        }

        return ++newId
    }

    fun getDummyUser(): User {
        return User(3,
            "Test",
            "Testić",
            Date(),
            "testtestic@posta.hr",
            "1231231231",
            "",
            "",
            "")
    }
}