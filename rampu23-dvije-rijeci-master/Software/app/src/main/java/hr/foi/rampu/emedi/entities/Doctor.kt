package hr.foi.rampu.emedi.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="doctors")
data class Doctor(
    @PrimaryKey(autoGenerate=true) val id: Int,
    val name : String,
    val surname : String,
    val specialization : String,
    val yearsEmployed : Int,
    val jobDescription : String,
    val clinicName : String,
    val address : String,
    val email : String,
    val telephone : String,
    //val review : MutableList<Review> = mutableListOf()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        /*mutableListOf<Review>().apply {
            parcel.readList(this, Review::class.java.classLoader)
        }*/
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(specialization)
        parcel.writeInt(yearsEmployed)
        parcel.writeString(jobDescription)
        parcel.writeString(clinicName)
        parcel.writeString(address)
        parcel.writeString(email)
        parcel.writeString(telephone)
        //parcel.writeList(review)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Doctor> {
        override fun createFromParcel(parcel: Parcel): Doctor {
            return Doctor(parcel)
        }

        override fun newArray(size: Int): Array<Doctor?> {
            return arrayOfNulls(size)
        }
    }
}
