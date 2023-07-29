import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

public class Statistics {
  public static final int GRAPH_PRECISION = 3;

  public static void run(MethodSet methods, String csvFilePath, int iterations) {
    Teacher teacher = (Teacher)methods.get("teacher");
    RuleChoosing ruleChoosingMechanism = (RuleChoosing)methods.get("rule choosing");
    ListOrdering listOrderingMethod = (ListOrdering)methods.get("list ordering");
    RuleSize maxRuleSize = (RuleSize)methods.get("rule size");
    
    List<int[]> graphs = new LinkedList<int[]>();

    //calculate the mistake percentages for each rule choosing mechanism and every teacher type
    for (int i = 0; i < iterations; i++) {
      Mistakes graph = learn("datasets/" + csvFilePath + ".csv", teacher, ruleChoosingMechanism, maxRuleSize.size, listOrderingMethod);
      graphs.add(graph.getValues(GRAPH_PRECISION));
    }

    //print the graph
    GraphGenerator.createGraph(graphs, GRAPH_PRECISION, methods.imageFilename(csvFilePath), methods.toString());
  }

  private static Mistakes learn(String csvFilePath, Teacher teacher, RuleChoosing ruleChoiceMechanism, int maxRuleSize, ListOrdering listOrderingMethod) {
    List<Data> dataList = teacher.preprocess(csvFilePath, listOrderingMethod);
    List<Rule> L = new LinkedList();

    Rule defaultRule = new Rule(teacher);

    int mistakes = 0;
    int numExamplesSeen = 0;

    Mistakes graph = new Mistakes();

    // Perform the learning algorithm using the dataList and the Teacher object
    for (Data data : dataList) {
      numExamplesSeen++;// for charts

      Rule prediction = ruleChoiceMechanism.choose(L, data);
      FeedbackResult feedback = null;

      if (prediction != null) {
        // predict 'label(exists)' with explanation 'exists'
        feedback = teacher.feedback(data, prediction);

        if (!feedback.getResult()) {
          mistakes++;

          // get correct label and feature
          if(prediction.getConjunction().getSize() + 1 == maxRuleSize)
            L.remove(prediction);
          else
            prediction.add(-feedback.getDiscriminativeFeature());
        }

      } else {
        prediction = defaultRule;
        feedback = teacher.feedback(data, defaultRule);

        if (!feedback.getResult()) {
          mistakes++;

          data.setClassification(feedback.getAnswer());

          Rule rule = new Rule(data);
          L.add(rule);
          rule.add(feedback.getDiscriminativeFeature());
        }
      }

      // printIteration(data, prediction.getExplanation(), feedback.getResult());

      double mistakesSoFar = (double) mistakes / (double) numExamplesSeen * 100;
      graph.add(mistakesSoFar);
    }

    // these are for charts also
    //System.out.println("Total Mistakes: " + mistakes);
    double mistakePercentage = (double) mistakes / dataList.size() * 100;
    //System.out.println("Mistake Percentage: " + mistakePercentage + "%");

    graph.setMistakes(mistakePercentage);
    return graph;
  }

}
