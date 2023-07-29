import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

public class Mistakes {
  private List<Double> graph;
  private double mistakesPercentage;

  public Mistakes() {
    graph = new ArrayList<>();
  }

  public int[] getValues(int precision) {
    precision = (int) Math.pow(10, precision);

    int[] values = new int[graph.size()];
    for (int i = 0; i < graph.size(); i++)
      values[i] = (int) (graph.get(i) * precision);

    return values;
  }

  public double getGraphPoint(int index) {
    return graph.get(index);
  }

  public void setMistakes(double mistakesPercentage) {
    this.mistakesPercentage = mistakesPercentage;
  }

  public double getMistakes() {
    return this.mistakesPercentage;
  }

  public void add(double mistake) {
    graph.add(mistake);
  }

  public void divideBy(double div) {
    for(int i = 0; i < graph.size(); i++)
      graph.set(i, graph.get(i) / div);
  }
  
  public void add(Mistakes mistake) {
    for(int i = 0; i < graph.size(); i++)
      graph.set(i, this.graph.get(i) + mistake.getGraphPoint(i));
  }

  public void printGraph() {
    for (double data : graph)
      System.out.println(data);
  }
}