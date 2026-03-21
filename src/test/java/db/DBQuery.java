package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBQuery {

    private static int getCountForQuery(String sql) throws Exception {
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            return rs.getInt(1);
        }
    }

    public static int getUserCount() throws Exception {
        return getCountForQuery("SELECT COUNT(*) FROM app_user");
    }

    public static int getCourseCount() throws Exception {
        return getCountForQuery("SELECT COUNT(*) FROM course");
    }

    public static int getEnrollmentCount() throws Exception {
        return getCountForQuery("SELECT COUNT(*) FROM enrollment");
    }

    public static boolean appUserExists(long userId) throws Exception {
        String sql = "SELECT 1 FROM app_user WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static long getAnyStudentUserId() throws Exception {
        String sql = "SELECT id FROM app_user WHERE role = 'STUDENT' ORDER BY id LIMIT 1";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) {
                throw new IllegalStateException("No STUDENT user found in app_user");
            }
            return rs.getLong(1);
        }
    }
}
