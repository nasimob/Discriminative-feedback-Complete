import java.util.List;

public class MostComplicatedRuleMethod extends RuleChoosing {
  public MostComplicatedRuleMethod(String description) {
    super(description);
  }
  
  protected Rule chooseRuleFromList(List<Rule> list) {
    int longestLength = Integer.MAX_VALUE;
    Rule mostComplicatedRule = null;

    for(Rule rule : list) {
      int size = rule.getConjunction().getSize();
      if(size > longestLength) {
        longestLength = size;
        mostComplicatedRule = rule;
      }
    }

    return mostComplicatedRule;
  }
}