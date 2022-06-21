package developer.abdusamid.codial_app.models

class Groups {
    var id: Int? = null
    var name: String? = null
    var mentors: Mentors? = null
    var times: String? = null
    var days: String? = null
    var courses: Courses? = null
    var open: Boolean? = null

    constructor()
    constructor(
        id: Int?,
        name: String?,
        mentors: Mentors?,
        times: String?,
        days: String?,
        courses: Courses?,
        open: Boolean?
    ) {
        this.id = id
        this.name = name
        this.mentors = mentors
        this.times = times
        this.days = days
        this.courses = courses
        this.open = open
    }

    constructor(
        name: String?,
        mentors: Mentors?,
        times: String?,
        days: String?,
        courses: Courses?,
        open: Boolean?
    ) {
        this.name = name
        this.mentors = mentors
        this.times = times
        this.days = days
        this.courses = courses
        this.open = open
    }


}