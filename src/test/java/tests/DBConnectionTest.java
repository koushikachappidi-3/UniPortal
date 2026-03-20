package tests;

import db.DBUtils;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnectionTest {

    @Test
    public void testDBConnection() throws Exception {
        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {

            rs.next();
            int result = rs.getInt(1);
            System.out.println("DB Connected! SELECT 1 returned: " + result);
        }
    }
}
