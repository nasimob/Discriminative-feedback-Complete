public class RuleSize extends Statisticable {
  public final int size;

  public RuleSize(int size, String description) {
    super("rule size", description);

    this.size = size;
  }
}