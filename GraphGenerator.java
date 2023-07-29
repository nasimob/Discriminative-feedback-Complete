import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class GraphGenerator {
  private static final int width = 800;
  private static final int height = 600;

  private static int[] averageGraph(List<int[]> valuesList) {
    int[] average = new int[valuesList.get(0).length];
    Iterator<int[]> iter = valuesList.iterator();

    while (iter.hasNext()) {
      int[] next = iter.next();

      for (int i = 0; i < average.length; i++)
        average[i] += next[i];
    }

    for (int i = 0; i < average.length; i++)
      average[i] /= valuesList.size();

    return average;
  }

  private static Color[] colors(int size) {
    Color[] colors = new Color[size + 1];
    for (int i = 0; i < colors.length; i++)
      colors[i] = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));

    return colors;
  }

  private static BufferedImage drawGraphAverage(List<int[]> valuesList, int precision, int maxValue, int minValue, String description) {
    // Create a BufferedImage object with the given width and height
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();

    // Set background color
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, width, height);

    // Set graph area dimensions
    int graphWidth = width - 100;
    int graphHeight = height - 100;
    int startX = 50;
    int startY = height - 50;

    // Draw x-axis and y-axis
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(2f));
    g2d.drawLine(startX, startY, startX + graphWidth, startY); // x-axis
    g2d.drawLine(startX, startY, startX, startY - graphHeight); // y-axis

    // Draw y-axis labels
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.BOLD, 18));
    for (int i = 0; i <= maxValue; i += maxValue / 10) {
      int x = startX;
      int y = startY - (i * graphHeight) / maxValue;
      String label = String.valueOf(i / (int) Math.pow(10, precision));
      g2d.drawString(label, x - g2d.getFontMetrics().stringWidth(label) - 5, y + 5);
    }

    // Draw the values as points on the graph
    // compute all the different colors for the graph
    int colorID = 0;
    Color[] colors = colors(valuesList.size());

    for (int[] values : valuesList) {
      g2d.setColor(colors[colorID++]);

      for (int i = 0; i < values.length; i++) {
        int x = startX + (i * graphWidth) / (values.length - 1);
        int y = startY - (values[i] * graphHeight) / maxValue;
        Ellipse2D.Double point = new Ellipse2D.Double(x - 3, y - 3, 4, 4);
        g2d.fill(point);
      }
    }

    return image;
  }
  
  private static BufferedImage drawGraph(List<int[]> valuesList, int precision, int maxValue, int minValue, String description, double relativeError) {
    // Create a BufferedImage object with the given width and height
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();

    // Set background color
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, width, height);

    // Set graph area dimensions
    int graphWidth = width - 100;
    int graphHeight = height - 100;
    int startX = 50;
    int startY = height - 50;

    // Draw x-axis and y-axis
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(2f));
    g2d.drawLine(startX, startY, startX + graphWidth, startY); // x-axis
    g2d.drawLine(startX, startY, startX, startY - graphHeight); // y-axis

    // Draw y-axis labels
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.BOLD, 18));
    for (int i = 0; i <= maxValue; i += maxValue / 10) {
      int x = startX;
      int y = startY - (i * graphHeight) / maxValue;
      String label = String.valueOf(i / (int) Math.pow(10, precision));
      g2d.drawString(label, x - g2d.getFontMetrics().stringWidth(label) - 5, y + 5);
    }

    // Calculate the average mistakes percentage and print it
    // Alongside additional, relevant information about the graphs
    int[] average = valuesList.get(valuesList.size() - 1);
    double averagePoint = (double) average[average.length - 1] / Math.pow(10, precision);
    String labelAverage = "Average: " + averagePoint + "% (RE: " + (relativeError * 100) + "%)";
    String labelIterations = "#iterations: " + (valuesList.size() - 1);
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.BOLD, 18));
    g2d.drawString(labelAverage, graphWidth - startX - 300, 50);

    description += ("\n" + labelIterations);
    String[] info = description.split("\n");
    for (int i = 0; i < info.length; i++)
      g2d.drawString(info[i], graphWidth - startX - 300, 50 + 30 * (i + 1));

    // Draw the values as points on the graph
    // compute all the different colors for the graph
    int colorID = 0;
    Color[] colors = colors(valuesList.size());

    for (int[] values : valuesList) {
      g2d.setColor(colors[colorID++]);

      for (int i = 0; i < values.length; i++) {
        int x = startX + (i * graphWidth) / (values.length - 1);
        int y = startY - (values[i] * graphHeight) / maxValue;
        Ellipse2D.Double point = new Ellipse2D.Double(x - 3, y - 3, 4 * (colorID == valuesList.size() ? 3 : 1),
            4 * (colorID == valuesList.size() ? 3 : 1));
        g2d.fill(point);
      }
    }

    return image;
  }

  private static int maxValue(List<int[]> valuesList) {
    int maxValue = Integer.MIN_VALUE;
    for (int[] values : valuesList)
      for (int value : values)
        if (value > maxValue)
          maxValue = value;

    return maxValue;
  }

  private static int minValue(List<int[]> valuesList) {
    int minValue = Integer.MAX_VALUE;
    for (int[] values : valuesList)
      for (int value : values)
        if (value < minValue)
          minValue = value;

    return minValue;
  }

  private static int lastMax(List<int[]> valuesList) {
    int max = 0;
    for (int[] values : valuesList)
      if (values[values.length - 1] > max)
        max = values[values.length - 1];

    return max;
  }

  private static int lastMin(List<int[]> valuesList) {
    int min = 0;
    for (int[] values : valuesList)
      if (values[values.length - 1] < min)
        min = values[values.length - 1];

    return min;
  }

  public static void createAverageGraph(List<int[]> valuesList, int precision, String filepath, String description) {
    // Calculate the maximum and minimum values in the array
    int maxValue = maxValue(valuesList);
    int minValue = minValue(valuesList);

     // Draw everything
    BufferedImage image = drawGraphAverage(valuesList, precision, maxValue, minValue, description);

    // Save the image as a PNG file
    saveFiles(image, description, filepath);
  }

  public static void createGraph(List<int[]> valuesList, int precision, String filepath, String description) {
    // compute the average graph
    int[] average = averageGraph(valuesList);
    valuesList.add(average);

    // Calculate the maximum and minimum values in the array
    int maxValue = maxValue(valuesList);
    int minValue = minValue(valuesList);

    double relativeError = (double)(lastMax(valuesList) - lastMin(valuesList)) / average[average.length - 1];
    relativeError = ((int)(relativeError * Math.pow(10, precision))) / Math.pow(10, precision);

    // Draw everything
    BufferedImage image = drawGraph(valuesList, precision, maxValue, minValue, description, relativeError);

    // Set string to write
    String write = description + "\n#Iterations: " + (valuesList.size() - 1);
    int[] averageGraphInteger = averageGraph(valuesList);
    double[] averageGraph = new double[averageGraphInteger.length];
    for(int i = 0; i < averageGraph.length; i++)
      averageGraph[i] = ((double)averageGraphInteger[i]) / Math.pow(10, precision);
    
    write = ("Average Graph: " + Arrays.toString(averageGraph) + "\n") + write;
    write = ("Average Mistakes Percentage: " + averageGraph[averageGraph.length - 1] + "\n") + write;
    
    // Save the image as a PNG file
    saveFiles(image, write, filepath);
  }

  private static void saveFiles(BufferedImage image, String text, String filepath) {
    try {
      // Save Image
      File output = new File(filepath + ".png");
      ImageIO.write(image, "png", output);
      System.out.println("Graph image saved successfully.");

      // Save txt file
      Path path = Paths.get(filepath + ".txt");
      Files.writeString(path, text, StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.out.println("Error while saving the graph or text files.");
      e.printStackTrace();
    }
  }

}
