package hr.foi.rampu.emedi.helpers

import android.app.Application
import hr.foi.rampu.emedi.helpers.TextSizeUtility

class ApplicationHelper : Application() {

    override fun onCreate() {
        super.onCreate()

        TextSizeUtility.initialize(this)
    }
}