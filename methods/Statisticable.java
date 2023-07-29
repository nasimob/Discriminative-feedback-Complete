public class Statisticable {
  public final String group;
  public final String description;

  public Statisticable(String group, String description) {
    this.group = group;
    this.description = description;
  }

  // compare by description
  @Override
  public boolean equals(Object method) {
    return this.description.equals(((Statisticable)method).description);
  }
}