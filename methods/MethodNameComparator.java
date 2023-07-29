import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;

class MethodNameComparator implements Comparator<Statisticable> {
  private static final Map<String, Integer> groupNames = groupNames();

  private static Map<String, Integer> groupNames() {
    Map<String, Integer> map = new HashMap<>();

    String[] names = new String[] {
      "list ordering",
      "teacher",
      "rule choosing",
      "rule size"
    };

    for(int i = 0; i < names.length; i++)
      map.put(names[i], i);

    return map;
  }
  
  @Override
  public int compare(Statisticable method1, Statisticable method2) {
    return groupNames.get(method1.group) - groupNames.get(method2.group);
  }
}