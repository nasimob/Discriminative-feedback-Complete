import java.util.List;
import java.util.ArrayList;

public abstract class RuleChoosing extends Statisticable {
  public RuleChoosing(String description) {
    super("rule choosing", description);
  }
  
  // returns a Data object y in L such that x satisfies the conjunction C[y], or
  // null when such an object doesn't exist
  public Rule choose(List<Rule> L, Data data) {
    List<Rule> filteredL = new ArrayList<>();
    for (Rule y : L)
      if (y.holdsTrue(data))
        filteredL.add(y);

    if (filteredL.size() == 0)
      return null;

    return chooseRuleFromList(filteredL);
  }

  // list is not empty
  protected abstract Rule chooseRuleFromList(List<Rule> list);
}