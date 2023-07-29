import java.util.List;

public class MaximumDiscriminativeTeacher extends Teacher {
  public MaximumDiscriminativeTeacher(String description) {
    super(description);
  }
  
  protected int getIndex(Data dataToClassify, Rule rule, List<Integer> diff) {
    double[] perPrediction = this.attributePercentage(rule.getLabel());
    double[] perReal = this.attributePercentage(this.correctClassification(dataToClassify));

    double maxDist = 0;
    int maxIndex = -1;
    for (int index : diff) {
      int indexabs = (int) Math.abs(index);
      if (Math.abs(perPrediction[indexabs - 1] - perReal[indexabs - 1]) > maxDist) {
        maxIndex = index;
        maxDist = Math.abs(perPrediction[indexabs - 1] - perReal[indexabs - 1]);
      }
    }

    return diff.indexOf(maxIndex);
  }
}