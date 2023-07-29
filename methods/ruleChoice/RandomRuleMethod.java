import java.util.List;

public class RandomRuleMethod extends RuleChoosing {
  public RandomRuleMethod(String description) {
    super(description);
  }
  
  protected Rule chooseRuleFromList(List<Rule> list) {
    int randomIndex = (int)(Math.random() * list.size());
    return list.get(randomIndex);
  }
}