import java.util.List;

public class FirstRuleMethod extends RuleChoosing {
  public FirstRuleMethod(String description) {
    super(description);
  }
  
  protected Rule chooseRuleFromList(List<Rule> list) {
    return list.get(0);
  }
}