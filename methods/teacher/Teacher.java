import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.lang.Math;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.text.DecimalFormat;

abstract class Teacher extends Statisticable {
  protected List<Data> dataList;
  private Set<String> classes;
  private Clusters clusters;

  private String readFromFile;

  private int[] shuffledIndices;

  public Teacher(String description) {
    super("teacher", description);
    readFromFile = null;
  }

  public List<Data> preprocess(String csvFilePath, ListOrdering orderingMethod) {
    // Read the CSV file and populate the dataList with Data objects
    if(readFromFile == null || !readFromFile.equals(csvFilePath)) {
      dataList = new LinkedList();
      classes = new HashSet();
      readDataFromFile(csvFilePath);
      calculateClusters();
    }
      
    readFromFile = csvFilePath;

    // Shuffle it to random order
    shuffledIndices = orderingMethod.order(this, dataList);

    // Return list of data without classifications
    return resetData();
  }

  public void setList(List<Data> list) {
    dataList = list;
  }

  private void calculateClusters() {
    clusters = new Clusters(classes, defaultData().getAttributes().length);

    for(Data data : dataList) {
      boolean[] attributes = data.getAttributes();
      clusters.getCluster(data.getClassification()).addData(attributes);
    }
  }

  public double[] attributePercentage(String className) {
    return clusters.getCluster(className).getAttributes();
  }

  private void readDataFromFile(String csvFilePath) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
      String line;

      int id = 1;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        boolean[] attributes = new boolean[values.length - 1];

        for (int i = 0; i < values.length - 1; i++) {
          attributes[i] = Boolean.parseBoolean(values[i].equals("1") ? "true" : "false");
        }

        String classification = values[values.length - 1];
        classes.add(classification);

        Data data = new Data(id, attributes, classification);
        dataList.add(data);

        id++;
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Data defaultData() {
    return dataList.get(0);
  }

  public String correctClassification(Data data) {
    return dataList.get(shuffledIndices[data.getID()] - 1).getClassification();
  }

  private List<Data> resetData() {
    List<Data> reset = new LinkedList<>();

    for (Data data : dataList)
      reset.add(new Data(data));

    return reset;
  }

  public FeedbackResult feedback(Data dataToClassify, Rule rule) {
    boolean result = correctClassification(dataToClassify).equals(rule.getLabel());

    return new FeedbackResult(
        result,
        correctClassification(dataToClassify),

        // unique part of teacher
        result ? -1 : discriminativeFeature(dataToClassify, rule));
  }

  private int discriminativeFeature(Data dataToClassify, Rule rule) {
    List<Integer> diff = new LinkedList();
    
    boolean[] attributes = dataToClassify.getAttributes();
    List<Integer> ruleList = rule.getConjunction().getList();
    boolean[] exampleAttributes = rule.getExplanation().getAttributes();
    
    for (int i = 0; i < attributes.length; i++) {
      int index = (i+1) * (attributes[i] ? 1 : -1);
      
      if (attributes[i] != exampleAttributes[i])
        diff.add(index);
    }

    if (diff.size() == 0) {
      throw new RuntimeException("no discriminating feature");
    }
      
    int index = getIndex(dataToClassify, rule, diff);
    return diff.get(index);
  }

  protected abstract int getIndex(Data dataToClassify, Rule rule, List<Integer> diff);
}