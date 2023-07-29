import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Arrays;

public class Clusters {
  private Cluster[] clusters;
  private String[] classes;

  public Clusters(Set<String> classes, int attrLength) {
    this.classes = new String[classes.size()];
    System.arraycopy(classes.toArray(), 0, this.classes, 0, classes.size());
    
    clusters = new Cluster[classes.size()];
    for(int i = 0; i < clusters.length; i++)
      clusters[i] = new Cluster(attrLength);
  }

  private int classToIndex(String classStr) {
    for(int i = 0; i < classes.length; i++)
      if(classes[i].equals(classStr))
        return i;

    return -1;
  }


  public Cluster getCluster(String classStr) {
    return clusters[classToIndex(classStr)];
  }

  public class Cluster {
    private List<Integer> attributes;
    private double[] sumOfAttributes;
    private int size;

    public Cluster(int attrLength) {
      attributes = new ArrayList<>();
      sumOfAttributes = new double[attrLength];
      size = 0;
    }

    public int getSize() {
      return size;
    }

    public void addData(boolean[] attributes) {
      for(int i = 0; i < attributes.length; i++)
        sumOfAttributes[i] += (attributes[i] ? 1 : 0);

      size ++;
    }

    public void printSumsOfAttributes() {
      System.out.println(Arrays.toString(sumOfAttributes));
    }

    double[] getAttributes() {
      double[] lst = new double[sumOfAttributes.length];
      for(int i = 0; i < sumOfAttributes.length; i++)
        lst[i] = sumOfAttributes[i] / size;

      return lst;
    }
  }
}