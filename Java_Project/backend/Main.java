import java.util.Arrays;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        StudentMarkAnalysis analysis = new StudentMarkAnalysis();

        // Insert sample student marks
        JSONObject insertResponse = analysis.insertStudentMarks("John Doe", Arrays.asList(85, 92, 78));
        System.out.println(insertResponse.toString());

        // Calculate statistics
        JSONObject stats = analysis.calculateStatistics();
        System.out.println(stats.toString());

        // Get top performer
        JSONObject topPerformer = analysis.getTopPerformer();
        System.out.println(topPerformer.toString());

        // Get lowest performer
        JSONObject lowestPerformer = analysis.getLowestPerformer();
        System.out.println(lowestPerformer.toString());
    }
}
