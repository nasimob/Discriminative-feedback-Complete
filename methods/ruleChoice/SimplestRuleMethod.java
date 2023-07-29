import java.util.List;

public class SimplestRuleMethod extends RuleChoosing {
  public SimplestRuleMethod(String description) {
    super(description);
  }
  
  protected Rule chooseRuleFromList(List<Rule> list) {
    int shortestLength = Integer.MAX_VALUE;
    Rule simplestRule = null;

    for(Rule rule : list) {
      int size = rule.getConjunction().getSize();
      if(size < shortestLength) {
        shortestLength = size;
        simplestRule = rule;
      }
    }

    return simplestRule;
  }
}