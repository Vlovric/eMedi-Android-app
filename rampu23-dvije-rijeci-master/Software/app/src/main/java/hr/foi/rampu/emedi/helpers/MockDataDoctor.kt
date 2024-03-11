import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.entities.Doctor

object MockDataDoctor {
    val doctorList = arrayOf<Doctor>(
        Doctor(
            1,
            "Ivo",
            "Ivić",
            "Ginekologija i opstetricija",
            10,
            "Ginekolog je liječnik koji liječi probleme i bolesti ženskog reproduktivnog sustava. Liječi poremećaje mokraćnog sustava i zdjelice, rak grlića maternice, te probleme sa dojkama i hormonalima.",
            "Gincej",
            "Dugo Selo",
            "iivic@",
            "012340087",
        ),
        Doctor(
            2,
            "Marija",
            "Marić",
            "Kardiologija",
            15,
            "Kardiolog je bitan član zdravstvenog tima u bolnici ili klinici. Pomažu osobama sa zdravstvenim problemima krvnih žila i srca.",
            "Poliklinika Maratana",
            "Krapina",
            "mdok.maric@hotmail.com",
            "039849439",
        ),
        Doctor(
        3,
        "Mate",
        "Perković",
        "Kardiologija",
        20,
        "Kardiolog je bitan član zdravstvenog tima u bolnici ili klinici. Pomažu osobama sa zdravstvenim problemima krvnih žila i srca.",
        "Perkovićeva",
        "Zagreb",
        "perkovic23@gmail.com",
        "097837643",
        ),
        Doctor(
        4,
        "Petra",
        "Krančar",
        "Abdominalna kirurgija",
        7,
        "Kirurgija jetre, žučnih puteva, gušterače, tumora želuca i debelog crijeva, upalnih bolesti crijeva, hernija trbušne stjenke i proktološka patologija. ",
        "Klinika Maj",
        "Zagreb",
        "pkrančar@gmail.com",
        "097837643",
        ),
        Doctor(
        5,
        "Mario",
        "Picek",
        "Fizikalna medicina i rehabilitacija",
        7,
        "U našoj bolnici fizikalnu terapiju vodi multidisciplinarni tim stručnjaka, od dijagnostike tegoba s kojima dolazite ili utvrđivanja rizika za nastanak oštećenja, pa sve do smanjenja tog istog rizika i saniranja već nastalog oštećenja.",
        "Poliklinika Femur",
        "Zabok",
        "mpicek.23@gmail.com",
        "013457887",
        ),
        Doctor(
        6,
        "Lea",
        "Mišnić",
        "Fizikalna medicina i rehabilitacija",
        5,
        "U našoj bolnici fizikalnu terapiju vodi multidisciplinarni tim stručnjaka, od dijagnostike tegoba s kojima dolazite ili utvrđivanja rizika za nastanak oštećenja, pa sve do smanjenja tog istog rizika i saniranja već nastalog oštećenja.",
        "Poliklinika Femur",
        "Zaprešić",
        "lmisnic.23@gmail.com",
        "013457887",
        )
    )

    fun loadDoctors() {
        val doctorsDAO = AppDatabase.getInstance().getDoctorsDao()

        if (doctorsDAO.getAllDoctors().isEmpty()) {
            doctorsDAO.insertDoctor(*doctorList)
        }
    }

    fun getDemoData(): List<Doctor> {
        val doctors = AppDatabase.getInstance().getDoctorsDao().getAllDoctors()
        return doctors.toList()
    }
}
