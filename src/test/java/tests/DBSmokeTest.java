package tests;

import db.DBQuery;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DBSmokeTest {

    @Test
    public void verifySeedDataCounts() throws Exception {
        int students = DBQuery.getCount("students");
        int courses = DBQuery.getCount("courses");
        int enrollments = DBQuery.getCount("enrollments");

        System.out.println("students=" + students);
        System.out.println("courses=" + courses);
        System.out.println("enrollments=" + enrollments);

        Assert.assertTrue(students > 0, "Students table should not be empty");
        Assert.assertTrue(courses > 0, "Courses table should not be empty");
        Assert.assertTrue(enrollments > 0, "Enrollments table should not be empty");
    }

    @Test
    public void verifyStudentExistsUsingRealId() throws Exception {
        int id = DBQuery.getAnyStudentId();
        boolean exists = DBQuery.studentExists(id);

        System.out.println("Checking student_id=" + id + " exists: " + exists);

        Assert.assertTrue(exists, "Expected student_id=" + id + " to exist");
    }
}
