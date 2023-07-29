import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Iterator;

public class LazyFurthestData extends ListOrdering {
  public LazyFurthestData(String description) {
    super(description);
  }
  
  protected List<Data> rearrange() {
    Tree tree = new Tree(list);
    //System.out.println(tree);

    // pick a random starting point
    Data curr = list.get((int) (Math.random() * list.size()));
    List<Data> newlist = new LinkedList();
    newlist.add(curr);
    list.remove(curr);

    while (!tree.isEmpty()) {
      //System.out.println("looking for " + curr.getID());

      Data temp = curr;
      curr = tree.furthestLeaf(curr);

      if (curr == null) {
        if(list.size() == 0) {
          break;
          //System.out.println(tree);
          
          //for(Data data : newlist)
          //  System.out.print(data.getID() + " ");
          //System.out.println("(" + newlist.size() + ")");
        }
        
        curr = list.get((int) (Math.random() * list.size()));
      }
      
      tree.delete(temp);
      list.remove(curr);

      newlist.add(curr);
      
    }

    return newlist;
  }

  private class Tree {
    private Node root;

    public String toString() {
      return root.toString();
    }

    public Tree(List<Data> list) {
      init(list);
    }

    public void init(List<Data> dataset) {
      root = new Node();

      int k = dataset.get(0).getAttributes().length;
      for (Data data : dataset) {
        Node curr = root;
        boolean[] attr = data.getAttributes();

        for (int i = 0; i < k; i++) {
          if (!attr[i]) {
            if (curr.left == null) {
              curr.left = new Node();
              curr.left.parent = curr;
            }

            curr = curr.left;
          }

          else {
            if (curr.right == null) {
              curr.right = new Node();
              curr.right.parent = curr;
            }

            curr = curr.right;
          }
        }

        curr.add(data);
        //System.out.println("tree #" + data.getID() + ":\n" + root.toString() + "\n");
      }
    }

    public Data furthestLeaf(Data data) {
      if(data == null)
        throw new RuntimeException("data is null in furthestLeaf");
      
      int bestLength = 0;
      Data bestLeaf = null;

      boolean[] attributes = data.getAttributes();
      Node original = reachLeaf(attributes);
      Node curr = original;
      if (curr == null) {
        System.out.println(toString());
        throw new RuntimeException("curr is null (" + data.getID() + ")");
      }

      List<Boolean> path = new LinkedList<>();

      while (curr != root) {
        Node temp = curr;
        curr = curr.parent;

        path.add(curr.left == temp);
        
        temp = curr;
        Node leaf = null;
        int length = 0;
        Iterator<Boolean> iterator = path.iterator();
        boolean dir = false;

        while (!temp.isLeaf()) {
          dir = iterator.next();
          Node direction = dir ? temp.right : temp.left;

          if (direction != null) {
            temp = direction;
            length++;
          } else {
            temp = dir ? temp.left : temp.right;
          }
        }

        leaf = temp;

        if(leaf.hasUniqueData() && leaf.getUniqueData().getID() == data.getID())
          length = 0;
        
        if (bestLength < length) {
          bestLength = length;
          bestLeaf = leaf.getRandomData(data.getID());
        }
      }

      if(bestLeaf == null) {
        //System.out.println(root.toString());

        if(!original.hasUniqueData())
          return original.getRandomData(data.getID());
      }
      
      return bestLeaf;
    }

    public void delete(Data data) {
      Node curr = reachLeaf(data.getAttributes());
      //System.out.println("DELETING " + curr.data.getID());

      if(!curr.hasUniqueData()) {
        curr.delete(data.getID());
        return;
      }
      
      while (curr != root && curr.isLeaf()) {
        Node temp = curr;
        curr = curr.parent;

        if (curr.left == temp)
          curr.left = null;
        else
          curr.right = null;
      }
    }

    public boolean isEmpty() {
      return root.isLeaf();
    }

    // returns null if the tree is not initialized or when the data doesn't exist in
    // the tree
    private Node reachLeaf(boolean[] attributes) {
      Node curr = root;
      if (curr == null)
        throw new RuntimeException("root is null");

      for (boolean attr : attributes) {
        if (curr == null)
          return null;

        if (attr)
          curr = curr.right;
        else
          curr = curr.left;
      }
      
      return curr;
    }

    private class Node {
      public Node left, right, parent;
      private List<Data> datalist; // data is not null if and only if the node is a leaf

      public Node() {
        datalist = new LinkedList<Data>();
      }

      public void add(Data data) {
        datalist.add(data);
      }

      public Data getData(int ID) {
        for(Data data : datalist)
          if(data.getID() == ID)
            return data;

        return null;
      }

      public Data getRandomData(int ID) {
        Data data = null;
        
        do {
          data = datalist.get((int)(Math.random() * datalist.size()));
        } while(data.getID() == ID);

        return data;
      }

      public void delete(int ID) {
        Data toRemove = null;
        
        for(Data data : datalist)
          if(data.getID() == ID) {
            toRemove = data;
            break;
          }

        datalist.remove(toRemove);
      }

      public boolean hasUniqueData() {
        return datalist.size() == 1;
      }

      public Data getUniqueData() {
        return datalist.get(0);
      }

      public boolean isLeaf() {
        return left == null && right == null;
      }

      public String toString() {
        return "" + (this.left == null ? "" : this.left.toString()) + "" + toStringDatalist() + "" + (this.right == null ? "" : this.right.toString()) + "";
      }

      private String toStringDatalist() {
        String str = "[";

        for(Data data : datalist)
          str += data.getID();

        return str + "]";
      }
    }
  }
}
