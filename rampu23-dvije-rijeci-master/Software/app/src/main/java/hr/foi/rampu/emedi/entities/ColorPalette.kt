package hr.foi.rampu.emedi.entities

import android.os.Parcel
import android.os.Parcelable

data class ColorPalette(
    val color1: Int,
    val color2: Int,
    val color3: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(color1)
        parcel.writeInt(color2)
        parcel.writeInt(color3)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ColorPalette> {
        override fun createFromParcel(parcel: Parcel): ColorPalette {
            return ColorPalette(parcel)
        }

        override fun newArray(size: Int): Array<ColorPalette?> {
            return arrayOfNulls(size)
        }
    }
}
