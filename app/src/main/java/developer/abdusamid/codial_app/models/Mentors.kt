package developer.abdusamid.codial_app.models

class Mentors {
    var id: Int? = null
    var surname: String? = null
    var name: String? = null
    var patronymic: String? = null
    var courses: Courses? = null

    constructor(id: Int?, surname: String?, name: String?, patronymic: String?, courses: Courses?) {
        this.id = id
        this.surname = surname
        this.name = name
        this.patronymic = patronymic
        this.courses = courses
    }

    constructor(surname: String?, name: String?, patronymic: String?, courses: Courses?) {
        this.surname = surname
        this.name = name
        this.patronymic = patronymic
        this.courses = courses
    }

    constructor()

}