package tests;

import db.DBQuery;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DBSmokeTest {

    @Test
    public void verifySeedDataCounts() throws Exception {
        int users = DBQuery.getUserCount();
        int courses = DBQuery.getCourseCount();
        int enrollments = DBQuery.getEnrollmentCount();

        System.out.println("users=" + users);
        System.out.println("courses=" + courses);
        System.out.println("enrollments=" + enrollments);

        Assert.assertTrue(users >= 2, "app_user should include seeded admin1 and student1");
        Assert.assertTrue(courses >= 2, "course should include seeded CS612 and CS632P");
        Assert.assertTrue(enrollments >= 0, "enrollment count should be non-negative");
    }

    @Test
    public void verifyStudentExistsUsingRealId() throws Exception {
        long id = DBQuery.getAnyStudentUserId();
        boolean exists = DBQuery.appUserExists(id);

        System.out.println("Checking app_user.id=" + id + " exists: " + exists);

        Assert.assertTrue(exists, "Expected app_user.id=" + id + " to exist");
    }
}
