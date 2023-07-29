import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LearningCurveOrdering extends ListOrdering {

  public LearningCurveOrdering(String description) {
    super(description);
  }
  
  private List<List<Data>> sortListToClusters() {
    List<List<Data>> clusters = new ArrayList<>();
    List<String> classes = new ArrayList<>();

    for(Data data : list) {
      if(!classes.contains(data.getClassification())) {
        classes.add(data.getClassification());
        clusters.add(new ArrayList<>());
      }

      clusters.get(classes.indexOf(data.getClassification())).add(data);
    }

    return clusters;
  }

  private void             sortCluster(List<Data> cluster, double[] percentages) {
    Collections.sort(cluster, new Comparator<Data> () {
      public int compare(Data d1, Data d2) {
        double dist = leastSquares(d1) - leastSquares(d2);
          
        if(dist == 0)
          return 0;
        if(dist < 0)
          return 1;
        return -1;
      }

      private double leastSquares(Data data) {
        double dist = 0;
        boolean[] attributes = data.getAttributes();

        for(int i = 0; i < percentages.length; i++) {
          dist += Math.pow((attributes[i] ? 1 : 0) - percentages[i], 2);
        }

        return dist;
      }
    });
  }
  
  public  List<Data>             rearrange() {
    //sort the list to the different classes
    List<List<Data>> clusters = sortListToClusters();

    //sort each individual list from 'most obvious' to 'least obvious' to be a mameber of the class
    List<Data> newlist = new ArrayList<>();
    for(List<Data> cluster : clusters) {
      String classification = cluster.get(0).getClassification();
      
      double[] percentages = teacher.attributePercentage(classification);
      
      sortCluster(cluster, percentages);
      
      newlist.addAll(cluster);
    }

    return newlist;
  }
}