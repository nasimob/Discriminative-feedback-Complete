import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Collections;

public class MethodSet {
  private List<Statisticable> methods;
  public Result result;

  public MethodSet(Statisticable[] methods) {
    super();

    for (Statisticable method : methods)
      this.methods.add(method);
  }

  public MethodSet() {
    methods = new ArrayList<>();
  }

  public Statisticable get(String group) {
    for (Statisticable method : methods)
      if (method.group.equals(group))
        return method;

    return null;
  }

  public void add(Statisticable method) {
    if (!this.contains(method))
      methods.add(method);
  }

  public String imageFilename(String dataset) {
    Collections.sort(methods, new MethodNameComparator());
    
    String name = "./tmp/" + dataset + "/";
    for (Statisticable method : methods)
      name += (method.description + "/");
    
    (new File(name.substring(0, name.length() - 1))).mkdirs();
    
    return name + "image";
  }

  public boolean contains(Statisticable method) {
    return methods.contains(method);
  }

  public boolean contains(String description) {
    return contains(new RuleSize(0, description));
  }

  public boolean equals(MethodSet methodSet) {
    for (Statisticable method : methods)
      if (!methodSet.contains(method))
        return false;

    return true;
  }

  public String toString() {
    return "Teacher: " + get("teacher").description + "\n" +
        "Rule Choosing Mechanism: " + get("rule choosing").description + "\n" +
        "List Ordering: " + get("list ordering").description + "\n" +
        "Max Rule Size: " + get("rule size").description;
  }
}