import java.util.List;
import java.util.LinkedList;

public class ShuffleList extends ListOrdering {
  public ShuffleList(String description) {
    super(description);
  }
  
  public List<Data> rearrange() {
    List<Data> rearranged = new LinkedList<>();
    
    while (list.size() != 0) {
      int randomIndex = (int) (Math.random() * list.size());
      Data data = list.remove(randomIndex);
      rearranged.add(data);
    }

    list.addAll(rearranged);
    return list;
  }
}