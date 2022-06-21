package developer.abdusamid.codial_app.models

class Students {
    var id: Int? = null
    var name: String? = null
    var surname: String? = null
    var number: String? = null
    var day: String? = null
    var groups: Groups? = null

    constructor(
        id: Int?,
        name: String?,
        surname: String?,
        number: String?,
        day: String?,
        groups: Groups?,
    ) {
        this.id = id
        this.name = name
        this.surname = surname
        this.number = number
        this.day = day
        this.groups = groups
    }

    constructor(name: String?, surname: String?, number: String?, day: String?, groups: Groups?) {
        this.name = name
        this.surname = surname
        this.number = number
        this.day = day
        this.groups = groups
    }

    constructor()

}