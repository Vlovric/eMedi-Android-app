# eMedi

## Projektni tim

Ime i prezime | E-mail adresa (FOI) | JMBAG | Github korisničko ime | Seminarska grupa
------------  | ------------------- | ----- | --------------------- | ----------------
David Matijanić | dmatijani21@student.foi.hr | 0016153844 | dmatijani | G02
Viktor Lovrić | vlovric21@student.foi.hr | 0016154953 | vlovric21 | G02
Domagoj Hegedušić | dhegedusi21@student.foi.hr | 0016153732 | dhegedusi21 | G02
Magdalena Markovinović  | mmarkovin21@student.foi.hr | 0016155896 | mmarkoovin21 | G02

## Opis domene

Problemska domena ove aplikacije primarno se fokusira na prijavljivanje pacijenta kod doktora ili specijalista. Aplikaciju koriste i pacijenti i doktori, no u ovom projektu obradit će se samo strana pacijenta. Dokument će obuhvaćati specifikacije potrebne pacijentima kako bi se nesmetano registrirali, prijavili u aplikaciju, pronašli određenog doktora ili specijalista po imenu ili karakteristikama, pratili svoje posjete doktoru i pratili propisane lijekove. Ova aplikacija pokrivat će perspektivu pacijenata te će sve funkcionalnosti biti usmjerene prema tome. 

## Specifikacija projekta

Ovim dokumentom opisuju se jasne i detaljne specifikacije aplikativnog rješenja po kojima se implementira sama mobilna aplikacija. Aplikativno rješenje trebalo bi zamijeniti portal "najdoktor" koji se pokazao nepraktičnim za korištenje i proširiti na njega.

### Funkcionalnosti sustava:

Oznaka | Naziv | Kratki opis | Odgovorni član tima
------ | ----- | ----------- | -------------------
F01 | Prijava i registracija | Kako bi mogli iskoristiti sve mogućnosti aplikacije, potrebno je da se korisnik registrira s jedinstvenim osobnim podacima koji ga identificiraju. To su podatci poput imena, prezimena, korisničkog imena, email-a, lozinke i ostalih osobnih podataka. Za pristup svim funkcionalnostima aplikacije potrebna je prethodna prijava registriranog korisnika s potrebnim podacima koji su email i lozinka. | Magdalena Markovinović|  
F02 | Pretraživanje i filtriranje doktora po određenim kriterijima | Korisnici aplikacije imaju mogućnost pretraživanja doktora prema njihovom nazivu. Doktori se mogu filtrirati po mnogo kategorija, što uključuje specijalizaciju, lokaciju, recenzijama | Magdalena Markovinović
F03 | Pregled podataka o liječnicima | Korisnici mogu vidjeti sve bitne podatke o liječnicima te informacije koje su im potrebne. To uključuje radno vrijeme, adresu, telefonski broj, adresu elektroničke pošte, specijalizaciju, usluge koje pruža i slično. | Viktor Lovrić
F04 | Dijeljenje informacija liječnika | Kako bi se olakšao prijenos informacija, korisnici na profilu svakog liječnika imaju gumb za dijeljenje informacija koji kopira najvažnije informacije o doktoru. | Magdalena Markovinović
F05 | Prijava razloga pregleda | Nakon pregledavanja informacija o određenom doktoru, korisnici se mogu prijaviti kod istog. Ispunjavanjem polja za informacije o razlogu pregleda i pritiskom na gumb prijave, poslat će se zahtjev doktoru kojeg doktor treba prihvatiti ili odbiti. Prihvaćanje zahtjeva ovisi isključivo o doktoru i analizi razloga pregleda. U svrhu ovog projekta implementacija prijave razloga pregleda napravit će se na način da se svaki korisnikov zahtjev odobrava nakon fiksnog vremena te se o tome obavještava korisnika. | Viktor Lovrić
F06 | Rezervacija termina pregleda | Kada je pacijent prijavljen kod doktora, ima mogućnost prijave termina pregleda. Pacijent za termin pregled prvo odabire dan te vremenski period unutar tog dana. Dani i vremenski periodi mogu biti ili dostupni ili nedostupni ovisno o zauzetosti doktora. | David Matijanić
F07 | Pregled povijesti posjeta | Korisnik može pregledavati svoju povijest posjeta liječniku i vidjeti kakve lijekove je u tom posjetu dobio na recept. | David Matijanić
F08 | Uređivanje podataka o korisniku | Korisnik može mijenjati svoje osobne podatke u aplikaciji. | David Matijanić
F09 | Pisanje i pregled recenzija | Korisnici mogu pregledavati i ostavljati recenzije doktorima kod kojih su bili na pregledu. Recenzije sadrže ocjenu od 1 do 5 sa opcionalnim komentarom.| Domagoj Hegedušić
F10 | Obavještavanje korisnika | Korisnik će od aplikacije zaprimiti obavijest sta vremena prije rezerviranog termina kod doktora. Korisnik će također dobivati obavijesti kada mu je prijava kod doktora odobrena. | Domagoj Hegedušić
F11 | Pristupačni način za slabovidne osobe | Aplikacija omogućava niz funkcija koje će osobama sa slabijim ili oštećenim vidom pomoći pri upotrebi. Funkcije se odnose na povećanje slova, promjenu fonta za osobe sa poteškoćom u čitanju i promjenu boje aplikacije. | Domagoj Hegedušić
F12 | Odjava i brisanje računa | Korisnik ima mogućnost odjave iz aplikacije i mogućnost brisanje svog korisničkog računa. | Viktor Lovrić

## Tehnologije i oprema

Za razvijanje aplikacije eMedi koristit ćemo sljedeće tehnologije: Android uređaj, git i GitHub za verzioniranje programskog koda, GitHub Wiki za pisanje jednostavne dokumentacije i alat GitHub projects.

Za razvijanje aplikacije eMedi koristit će se primarno Android Studio i programski jezik Kotlin. Za verzioniranje programskog koda koristit će se git i GitHub te GitHub Wiki za pisanje dokumentacije. Projektni zadaci bit će pisani i praćeni u GitHub Projects. Za bazu podataka bit će korišten SQL.

## Baza podataka i web server

Trebamo bazu podataka i pristup serveru za PHP skripte.
