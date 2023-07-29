import java.util.LinkedList;
import java.util.List;

public class ImprovementMethods {
  /*
  the order is:
  listOrderingMethods,
  teachers,
  ruleChoosingMethods,
  RuleSizes
  */
  public static final MethodSet[] methods = getMethodSets();
  
  private static MethodSet[] getMethodSets() {
    Statisticable[][] methods = {
      getListOrderingMethods(),
      getTeachers(),
      getRuleChoosingMethods(),
      getRuleSizes()
    };

    MethodSet[] all = new MethodSet[methodSetSize(methods)];
    for(int i = 0; i < all.length; i++)
      all[i] = new MethodSet();

    int[] sizes = new int[methods.length];
    for(int i = 0; i < methods.length; i++)
      sizes[i] = methods[i].length;

    int[] indices = new int[methods.length];
    for(int i = 0; i < all.length; i++) {
      for(int j = 0; j < methods.length; j++)
        all[i].add(methods[j][indices[j]]);

      //update the indices
      if(i == all.length - 1)
        break;
      
      indices[methods.length - 1]++;
      for(int j = methods.length - 1; j >= 0; j--)
        if(indices[j] == methods[j].length) {
          indices[j] = 0;
          indices[j-1]++;
        }
    }

    return all;
  }

  public static List<MethodSet> filter(String[] descriptions) {
    List<MethodSet> filtered = new LinkedList<>();

    for(MethodSet method : methods) {
      boolean passes = true;
      for(String description : descriptions)
        if(!method.contains(description)) {
          passes = false;
          break;
        }

      if(passes)
        filtered.add(method);
    }

    return filtered;
  }

  private static int methodSetSize(Statisticable[][] methods) {
    int size = 1;

    for(Statisticable[] methodtype : methods)
      size *= methodtype.length;

    return size;
  }
  
  private static ListOrdering[] getListOrderingMethods() {
    ListOrdering random = new ShuffleList("shuffle");
    ListOrdering leastSquareSort = new LearningCurveOrdering("least_square");
    ListOrdering lazyFurthest = new LazyFurthestData("lazy_furthest");

    return new ListOrdering[] {
      random,
      lazyFurthest
    };
  }

  private static Teacher[] getTeachers() {
    Teacher maximumTeacher = new MaximumDiscriminativeTeacher("most_discriminate_teacher");
    Teacher randomTeacher = new RandomTeacher("random_teacher");

    return new Teacher[] {
      randomTeacher,
      maximumTeacher
    };
  }

  private static RuleChoosing[] getRuleChoosingMethods() {
    RuleChoosing firstRule = new FirstRuleMethod("first_rule");
    RuleChoosing simplestRule = new SimplestRuleMethod("simplest_rule"); //slightly worse than the first rule method
    RuleChoosing randomRule = new RandomRuleMethod("random_rule"); //slightly worse than the simplest rule method
    RuleChoosing mostComplicatedRule = new MostComplicatedRuleMethod("longest_rule"); //way worse than choosing at random

    return new RuleChoosing[] {
      firstRule,
      simplestRule,
      randomRule
    };
  }

  private static RuleSize[] getRuleSizes() {
    RuleSize four = new RuleSize(4, "4");
    RuleSize five = new RuleSize(5, "5");
    RuleSize six = new RuleSize(6, "6");
    RuleSize seven = new RuleSize(7, "7");
    RuleSize eight = new RuleSize(8, "8");
    RuleSize nine = new RuleSize(9, "9");
    RuleSize ten = new RuleSize(10, "10");

    return new RuleSize[] {
      four,
      five,
      six,
      seven,
      eight,
      nine,
      ten
    };
  }

}