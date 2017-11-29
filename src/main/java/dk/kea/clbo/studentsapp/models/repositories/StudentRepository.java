package dk.kea.clbo.studentsapp.models.repositories;

import dk.kea.clbo.studentsapp.models.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by clbo on 20/11/2017.
 */
@Repository
public class StudentRepository implements IStudentRepository {

    @Autowired
    private JdbcTemplate jdbc;
    private ArrayList<Student> students;
    private SqlRowSet rs;

    public StudentRepository() {
        students = new ArrayList<Student>();
    }


    @Override
    public void create(Student student) {

        // Convert Enrollmentdate into the right format for mysql (yyyy-MM-dd)
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String enrollmentdate = formatter.format(student.getEnrollmentDate());

        jdbc.update("insert into students(first_name,last_name, enrollment_date, cpr)values('"+ student.getFirstName() +"','"+ student.getLastName()+"', '"+ enrollmentdate +"', '"+ student.getCpr()+"')");

    }

    @Override
    public Student read(int id) {

        rs = jdbc.queryForRowSet("SELECT * FROM students where students_id ='" + id + "'");
        while (rs.next()) {

            return  new Student(rs.getInt("students_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("enrollment_date"),
                    rs.getString("cpr"));

        }

        return new Student();
    }

    @Override
    public List<Student> readAll() {
        students.clear();
        rs = jdbc.queryForRowSet("select * from students");
        while (rs.next()) {

            students.add(new Student(rs.getInt("students_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("enrollment_date"),
                    rs.getString("cpr")));

        }
        return students;
    }

    @Override
    public boolean update(Student student) {

        int result = jdbc.update("UPDATE students SET " +
                "first_name ='"+ student.getFirstName() +"' , " +
                "last_name='"+ student.getLastName() +"' ," +
                "enrollment_date='"+ student.getEnrollmentDate() +"' ," +
                "cpr='"+ student.getCpr() +"' WHERE students_id = '"+ student.getStudentId() +"'");
        return true;

    }

    @Override
    public boolean delete(Student student) {
        // TODO: return type bool
        int result = jdbc.update("DELETE FROM students WHERE students_id='" + student.getStudentId() + "'");
        return true;
    }
}
