import java.util.List;

public class RandomTeacher extends Teacher {
  public RandomTeacher(String description) {
    super(description);
  }
  
  protected int getIndex(Data dataToClassify, Rule rule, List<Integer> diff) {
    return (int) (Math.random() * diff.size());
  }
}