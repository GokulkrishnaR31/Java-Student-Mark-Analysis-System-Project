import java.sql.*;
import java.util.*;
import org.json.JSONObject;

public class StudentMarkAnalysis {

    // Method to insert student marks and return success message as JSONObject
    public JSONObject insertStudentMarks(String name, List<Integer> marks) {
        String url = "jdbc:sqlite:D:\\Java_Project\\backend\\Student.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            String insertSQL = "INSERT INTO students (name, marks) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                for (int mark : marks) {
                    pstmt.setString(1, name);
                    pstmt.setInt(2, mark);
                    pstmt.executeUpdate();
                }
            }
            JSONObject response = new JSONObject();
            response.put("status", "success");
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return response;
        }
    }

    // Method to calculate statistics and return as JSONObject
    public JSONObject calculateStatistics() {
        String url = "jdbc:sqlite:D:\\Java_Project\\backend\\Student.db";
        JSONObject result = new JSONObject();
        try (Connection conn = DriverManager.getConnection(url)) {
            String selectSQL = "SELECT marks FROM students";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectSQL)) {

                List<Integer> marks = new ArrayList<>();
                int total = 0;
                while (rs.next()) {
                    int mark = rs.getInt("marks");
                    marks.add(mark);
                    total += mark;
                }

                double mean = total / (double) marks.size();
                double variance = 0;
                for (int mark : marks) {
                    variance += Math.pow(mark - mean, 2);
                }
                variance /= marks.size();
                double stdDev = Math.sqrt(variance);

                result.put("mean", mean);
                result.put("variance", variance);
                result.put("standardDeviation", stdDev);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("error", e.getMessage());
        }
        return result;
    }

    // Method to get top performer and return as JSONObject
    public JSONObject getTopPerformer() {
        String url = "jdbc:sqlite:D:\\Java_Project\\backend\\Student.db";
        JSONObject result = new JSONObject();
        try (Connection conn = DriverManager.getConnection(url)) {
            String bestSQL = "SELECT name, marks FROM students ORDER BY marks DESC LIMIT 1";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(bestSQL)) {

                if (rs.next()) {
                    result.put("name", rs.getString("name"));
                    result.put("marks", rs.getInt("marks"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("error", e.getMessage());
        }
        return result;
    }

    // Method to get lowest performer and return as JSONObject
    public JSONObject getLowestPerformer() {
        String url = "jdbc:sqlite:D:\\Java_Project\\backend\\Student.db";
        JSONObject result = new JSONObject();
        try (Connection conn = DriverManager.getConnection(url)) {
            String worstSQL = "SELECT name, marks FROM students ORDER BY marks ASC LIMIT 1";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(worstSQL)) {

                if (rs.next()) {
                    result.put("name", rs.getString("name"));
                    result.put("marks", rs.getInt("marks"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("error", e.getMessage());
        }
        return result;
    }
}
