import java.util.Map;
import java.util.HashMap;

public class Result {
  private Map<ResultName, String> results;
  public int[] values;
  
  public Result(String textFromFile) {
    results = new HashMap();

    textFromFile = textFromFile.replace(": " , ":");
    String[] resultsArray = textFromFile.split("\n");

    for(int i = 0; i < resultsArray.length; i++)
      results.put(ResultName.values()[i], resultsArray[i].split(":")[1]);

    values = getValues();
  }

  public int[] getValues() {
    int precision = 3;

    double[] arr = parseStringToDoubleArray(results.get(ResultName.AVERAGE_GRAPH));
    return getValues(precision);
  }

  private int[] getValues(int precision) {
    precision = (int) Math.pow(10, precision);

    double[] graph = parseStringToDoubleArray(results.get(ResultName.AVERAGE_GRAPH));
    int[] values = new int[graph.length];
    for (int i = 0; i < graph.length; i++)
      values[i] = (int) (graph[i] * precision);

    return values;
  }

  private double[] parseStringToDoubleArray(String input) {
        // Remove leading and trailing spaces and brackets
        input = input.trim().substring(1, input.length() - 1);

        // Split the string by commas
        String[] values = input.split(",\\s*");

        // Create a double array to store the parsed values
        double[] result = new double[values.length];

        // Parse each value and store it in the result array
        for (int i = 0; i < values.length; i++) {
            try {
                result[i] = Double.parseDouble(values[i]);
            } catch (NumberFormatException e) {
                // Handle any parsing errors if necessary
                e.printStackTrace();
                // You can throw an exception or return null/empty array depending on your use case
            }
        }

        return result;
    }


  public String get(ResultName resultName) {
    return results.get(resultName);
  }

  
}