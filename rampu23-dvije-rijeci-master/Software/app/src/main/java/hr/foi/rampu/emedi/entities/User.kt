package hr.foi.rampu.emedi.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hr.foi.rampu.emedi.converters.DateConverter
import java.util.Date

/*
DODATI:
List<Posjeta> - KASNIJE
List<Recenzija> RECENZIJE - KASNIJE
Slika - PROFILNA (razmisliti kako upload)
 */

@Entity(tableName="users")
@TypeConverters(DateConverter::class)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name: String,
    var surname: String,
    @ColumnInfo(name="birth_date") var birthDate: Date,
    var email: String,
    var telephoneNumber: String,
    var address: String,
    var username: String,
    var password: String
)