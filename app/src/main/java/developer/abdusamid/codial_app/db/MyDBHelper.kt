package developer.abdusamid.codial_app.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import developer.abdusamid.codial_app.models.Courses
import developer.abdusamid.codial_app.models.Groups
import developer.abdusamid.codial_app.models.Mentors
import developer.abdusamid.codial_app.models.Students

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION),
    DBService {
    companion object {
        const val DB_NAME = "base"
        const val DB_VERSION = 1

        const val TABLE_COURSES = "courses"
        const val COURSES_ID = "id"
        const val COURSES_NAME = "name"
        const val COURSES_ABOUT = "about"

        const val TABLE_MENTORS = "mentors"
        const val MENTORS_ID = "id"
        const val MENTORS_SURNAME = "surname"
        const val MENTORS_NAME = "name"
        const val MENTORS_PATRONYMIC = "patronymic"
        const val MENTORS_COURSES_ID = "courses_id"

        const val TABLE_GROUPS = "groups"
        const val GROUPS_ID = "id"
        const val GROUPS_NAME = "name"
        const val GROUPS_MENTORS_ID = "mentors_id"
        const val GROUPS_TIMES = "times"
        const val GROUPS_DAYS = "days"
        const val GROUPS_COURSES_ID = "courses_id"
        const val GROUPS_OPEN = "open"

        const val TABLE_STUDENTS = "students"
        const val STUDENTS_ID = "id"
        const val STUDENTS_NAME = "name"
        const val STUDENTS_SURNAME = "surname"
        const val STUDENTS_NUMBER = "number"
        const val STUDENTS_DAY = "day"
        const val STUDENTS_GROUPS_ID = "groups_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val queryCourses =
            "CREATE TABLE $TABLE_COURSES($COURSES_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE , $COURSES_NAME text NOT NULL unique , $COURSES_ABOUT TEXT NOT NULL)"
        val queryMentors =
            "CREATE TABLE $TABLE_MENTORS($MENTORS_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE , $MENTORS_SURNAME TEXT NOT NULL , $MENTORS_NAME TEXT NOT NULL , $MENTORS_PATRONYMIC TEXT NOT NULL , $MENTORS_COURSES_ID INTEGER NOT NULL , FOREIGN KEY ($MENTORS_COURSES_ID) REFERENCES $TABLE_COURSES($COURSES_ID))"
        val queryGroups =
            "CREATE TABLE $TABLE_GROUPS($GROUPS_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE , $GROUPS_NAME TEXT NOT NULL unique , $GROUPS_MENTORS_ID INTEGER NOT NULL , $GROUPS_TIMES TEXT NOT NULL , $GROUPS_DAYS TEXT NOT NULL , $GROUPS_COURSES_ID INTEGER NOT NULL , $GROUPS_OPEN numeric NOT NULL , FOREIGN KEY ($GROUPS_COURSES_ID) REFERENCES $TABLE_COURSES($COURSES_ID))"
        val queryStudents =
            "CREATE TABLE $TABLE_STUDENTS($STUDENTS_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE , $STUDENTS_NAME TEXT NOT NULL , $STUDENTS_SURNAME TEXT NOT NULL , $STUDENTS_NUMBER TEXT NOT NULL , $STUDENTS_DAY TEXT NOT NULL , $STUDENTS_GROUPS_ID INTEGER NOT NULL , FOREIGN KEY ($STUDENTS_GROUPS_ID) REFERENCES $TABLE_GROUPS($GROUPS_ID))"
        db.execSQL(queryCourses)
        db.execSQL(queryMentors)
        db.execSQL(queryGroups)
        db.execSQL(queryStudents)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //NOT YET IMPLEMENTED
    }

    override fun addCourses(courses: Courses, context: Context): Boolean {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COURSES_NAME, courses.name)
        contentValues.put(COURSES_ABOUT, courses.about)
        val result = database.insert(TABLE_COURSES, null, contentValues)
        val boolean = if (result.toInt() == -1) {
            Toast.makeText(
                context,
                "Failed to Added \na course with this name already exists, please try again by changing the name",
                Toast.LENGTH_SHORT
            ).show()
            false
        } else {
            Toast.makeText(context, "Successfully Added!", Toast.LENGTH_SHORT).show()
            true
        }
        database.close()
        return boolean
    }

    @SuppressLint("Recycle")
    override fun showCourses(): ArrayList<Courses> {
        val arrayListCourses = ArrayList<Courses>()
        val database = this.readableDatabase
        val query = "SELECT * FROM $TABLE_COURSES"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val courses = Courses(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
                )
                arrayListCourses.add(courses)
            } while (cursor.moveToNext())
        }
        return arrayListCourses
    }

    override fun deleteCourses(courses: Courses): Boolean {
        val database = this.writableDatabase
        getMentorByCoursesID(courses)
        val result =
            database.delete(TABLE_COURSES, "$COURSES_ID = ?", arrayOf(courses.id.toString()))
        return result != -1
    }

    override fun getCoursesByID(id: Int): Courses {
        val database = this.readableDatabase
        val cursor =
            database.query(
                TABLE_COURSES,
                arrayOf(COURSES_ID, COURSES_NAME, COURSES_ABOUT),
                "$COURSES_ID = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )

        cursor.moveToFirst()
        return Courses(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2)
        )
    }

    override fun addMentors(mentors: Mentors, context: Context) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(MENTORS_SURNAME, mentors.surname)
        contentValues.put(MENTORS_NAME, mentors.name)
        contentValues.put(MENTORS_PATRONYMIC, mentors.patronymic)
        contentValues.put(MENTORS_COURSES_ID, mentors.courses!!.id)
        val result = database.insert(TABLE_MENTORS, null, contentValues)
        if (result.toInt() == -1) {
            Toast.makeText(context, "Failed to Added!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Successfully Added!", Toast.LENGTH_SHORT).show()
        }
        database.close()
    }

    @SuppressLint("Recycle")
    override fun showMentors(): ArrayList<Mentors> {
        val arrayListMentors = ArrayList<Mentors>()
        val database = this.readableDatabase
        val query = "SELECT * FROM $TABLE_MENTORS"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val mentors = Mentors(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    getCoursesByID(cursor.getInt(4))
                )
                arrayListMentors.add(mentors)
            } while (cursor.moveToNext())
        }
        return arrayListMentors
    }

    override fun updateMentors(mentors: Mentors, context: Context) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(MENTORS_SURNAME, mentors.surname)
        contentValues.put(MENTORS_NAME, mentors.name)
        contentValues.put(MENTORS_PATRONYMIC, mentors.patronymic)
        contentValues.put(MENTORS_COURSES_ID, mentors.courses!!.id)
        val result =
            database.update(TABLE_MENTORS, contentValues, "id=?", arrayOf(mentors.id.toString()))
        if (result == -1) {
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun deleteMentors(mentors: Mentors): Boolean {
        val database = this.writableDatabase
        getGroupsByMentorsID(mentors)
        val result =
            database.delete(TABLE_MENTORS, "$MENTORS_ID = ?", arrayOf(mentors.id.toString()))
        return result != -1
    }

    override fun getMentorsByID(id: Int): Mentors {
        val database = this.readableDatabase
        val query = "SELECT * FROM $TABLE_MENTORS WHERE $MENTORS_ID = $id"
        val cursor = database.rawQuery(query, null)
        cursor.moveToFirst()
        return Mentors(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3),
            getCoursesByID(cursor.getInt(4))
        )
    }

    override fun getMentorByCoursesID(courses: Courses) {
        val database = this.writableDatabase
        val query = "SELECT * FROM $TABLE_MENTORS WHERE $MENTORS_COURSES_ID = '${courses.id}'"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val mentors = Mentors(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    getCoursesByID(cursor.getInt(4))
                )
                deleteMentors(mentors)
            } while (cursor.moveToNext())
        }
    }

    override fun getAllMentorsByID(id: Int): ArrayList<Mentors> {
        val arrayListMentors = ArrayList<Mentors>()
        val database = this.readableDatabase
        val query = "SELECT * FROM $TABLE_MENTORS WHERE $MENTORS_COURSES_ID = $id"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val mentors = Mentors(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    getCoursesByID(cursor.getInt(4))
                )
                arrayListMentors.add(mentors)
            } while (cursor.moveToNext())
        }
        return arrayListMentors
    }

    override fun addGroups(groups: Groups, context: Context): Boolean {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(GROUPS_NAME, groups.name)
        contentValues.put(GROUPS_MENTORS_ID, groups.mentors!!.id)
        contentValues.put(GROUPS_TIMES, groups.times)
        contentValues.put(GROUPS_DAYS, groups.days)
        contentValues.put(GROUPS_COURSES_ID, groups.courses!!.id)
        contentValues.put(GROUPS_OPEN, groups.open)
        val result = database.insert(TABLE_GROUPS, null, contentValues)
        val boolean = if (result.toInt() == -1) {
            Toast.makeText(
                context,
                "Failed to Added \na group with this name already exists, please try again by changing the name",
                Toast.LENGTH_SHORT
            ).show()
            false
        } else {
            Toast.makeText(context, "Successfully Added!", Toast.LENGTH_SHORT).show()
            true
        }
        database.close()
        return boolean

    }

    override fun showGroups(string: String, context: Context): ArrayList<Groups> {
        val arrayList = ArrayList<Groups>()
        val database = this.readableDatabase
        val query = "SELECT * FROM $TABLE_GROUPS WHERE $GROUPS_OPEN = '$string'"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                var groups = Groups()
                when (string) {
                    "0" -> {
                        groups = Groups(
                            cursor.getInt(0),
                            cursor.getString(1),
                            getMentorsByID(cursor.getInt(2)),
                            cursor.getString(3),
                            cursor.getString(4),
                            getCoursesByID(cursor.getInt(5)),
                            false
                        )
                    }
                    "1" -> {
                        groups = Groups(
                            cursor.getInt(0),
                            cursor.getString(1),
                            getMentorsByID(cursor.getInt(2)),
                            cursor.getString(3),
                            cursor.getString(4),
                            getCoursesByID(cursor.getInt(5)),
                            true
                        )
                    }
                }
                arrayList.add(groups)

            } while (cursor.moveToNext())
        }
        return arrayList
    }

    override fun updateGroups(groups: Groups, context: Context) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(GROUPS_NAME, groups.name)
        contentValues.put(GROUPS_MENTORS_ID, groups.mentors!!.id)
        contentValues.put(GROUPS_TIMES, groups.times)
        contentValues.put(GROUPS_DAYS, groups.days)
        contentValues.put(GROUPS_COURSES_ID, groups.courses!!.id)
        contentValues.put(GROUPS_OPEN, groups.open)
        val result =
            database.update(TABLE_GROUPS, contentValues, "id=?", arrayOf(groups.id.toString()))
        if (result == -1) {
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun deleteGroups(groups: Groups): Boolean {
        val database = this.writableDatabase
        getStudentByGroupsID(groups)
        val result =
            database.delete(TABLE_GROUPS, "$GROUPS_ID = ?", arrayOf(groups.id.toString()))
        return result != -1
    }

    override fun getGroupsByMentorsID(mentors: Mentors) {
        val database = this.writableDatabase
        val query = "SELECT * FROM $TABLE_GROUPS WHERE $GROUPS_MENTORS_ID = '${mentors.id}'"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val groups = Groups(
                    cursor.getInt(0),
                    cursor.getString(1),
                    getMentorsByID(cursor.getInt(2)),
                    cursor.getString(3),
                    cursor.getString(4),
                    getCoursesByID(cursor.getInt(5)),
                    cursor.getString(6).equals("0")
                )
                deleteGroups(groups)
            } while (cursor.moveToNext())
        }
    }

    override fun getGroupsByID(id: Int, boolean: Boolean): Groups {
        val database = this.readableDatabase
        val cursor =
            database.query(
                TABLE_GROUPS,
                arrayOf(
                    GROUPS_ID,
                    GROUPS_NAME,
                    GROUPS_MENTORS_ID,
                    GROUPS_TIMES,
                    GROUPS_DAYS,
                    GROUPS_COURSES_ID,
                    GROUPS_OPEN
                ),
                "$GROUPS_ID = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )

        cursor.moveToFirst()
        return Groups(
            cursor.getInt(0),
            cursor.getString(1),
            getMentorsByID(cursor.getInt(2)),
            cursor.getString(3),
            cursor.getString(4),
            getCoursesByID(cursor.getInt(5)),
            boolean
        )
    }

    override fun startLessonGroup(groups: Groups, context: Context): Boolean {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(GROUPS_OPEN, groups.open)
        val result =
            database.update(TABLE_GROUPS, contentValues, "id=?", arrayOf(groups.id.toString()))
        val boolean = if (result == -1) {
            Toast.makeText(context, "Failed To Started Lesson", Toast.LENGTH_SHORT).show()
            false
        } else {
            Toast.makeText(context, "Lesson started!", Toast.LENGTH_SHORT).show()
            true
        }
        return boolean
    }

    override fun addStudents(students: Students, context: Context) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(STUDENTS_NAME, students.name)
        contentValues.put(STUDENTS_SURNAME, students.surname)
        contentValues.put(STUDENTS_NUMBER, students.number)
        contentValues.put(STUDENTS_DAY, students.day)
        contentValues.put(STUDENTS_GROUPS_ID, students.groups!!.id)
        val result = database.insert(TABLE_STUDENTS, null, contentValues)
        if (result.toInt() == -1) {
            Toast.makeText(context, "Failed to Added!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Successfully Added!", Toast.LENGTH_SHORT).show()
        }
        database.close()
    }

    override fun showStudents(id: Int, boolean: Boolean): ArrayList<Students> {
        val arrayListStudents = ArrayList<Students>()
        val database = this.readableDatabase
        val query = " SELECT * FROM $TABLE_STUDENTS WHERE $STUDENTS_GROUPS_ID = '$id'"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val students = Students(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    getGroupsByID(cursor.getInt(5), boolean)
                )
                arrayListStudents.add(students)
            } while (cursor.moveToNext())
        }
        return arrayListStudents
    }

    override fun updateStudents(students: Students, context: Context) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(STUDENTS_NAME, students.name)
        contentValues.put(STUDENTS_SURNAME, students.surname)
        contentValues.put(STUDENTS_NUMBER, students.number)
        val result =
            database.update(TABLE_STUDENTS, contentValues, "id=?", arrayOf(students.id.toString()))
        if (result == -1) {
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun deleteStudents(students: Students): Boolean {
        val database = this.writableDatabase
        val result =
            database.delete(TABLE_STUDENTS, "$STUDENTS_ID = ?", arrayOf(students.id.toString()))
        return result != -1
    }

    override fun getStudentByGroupsID(groups: Groups) {
        val database = this.writableDatabase
        val query = "SELECT * FROM $TABLE_STUDENTS WHERE $STUDENTS_GROUPS_ID = '${groups.id}'"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val students = Students(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    getGroupsByID(groups.id!!, groups.open!!)
                )
                deleteStudents(students)
            } while (cursor.moveToNext())
        }
    }
}