package hr.foi.rampu.emedi.entities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.foi.rampu.emedi.helpers.NotificationHelper

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        NotificationHelper.showNotification(context!!, "You have a scheduled appointment in 1 hour!")
    }
}
