import java.util.List;
import java.util.Arrays;

public abstract class ListOrdering extends Statisticable {
  protected int currentID;
  protected Teacher teacher;
  protected List<Data> list;

  public ListOrdering(String description) {
    super("list ordering", description);
  }
  
  //returns shuffled indices
  public int[] order(Teacher teacher, List<Data> list) {
    currentID = 1;
    this.teacher = teacher;
    this.list = list;
    
    list = rearrange();
    teacher.setList(list);
    
    int[] shuffledIndices = new int[list.size() + 1];
    for(Data data : list)
      shuffledIndices[data.getID()] = currentID++;

    return shuffledIndices;
  }

  protected abstract List<Data> rearrange();
}