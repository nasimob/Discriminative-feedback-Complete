import java.util.LinkedList;
import java.lang.Math;
import java.awt.*;
import java.io.IOException;//same as above
import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

class Main {
  public static final String[] csvFilePaths = new String[] {
    "chess",
    "nursery",
    "zoo"
  };

  public static void main(String[] args) {
    int iterations = 100;

    //updateMethodSetsWithResults();
  }

  private static void runAll(int iterations) {
    runAllForFiles(csvFilePaths);
  }

  private static void runAllForFiles(String[] files) {
    for(String filepath : files)
      for (MethodSet methods : ImprovementMethods.methods)
        Statistics.run(methods, filepath, 100);
  }

  private static void createGraphs() {
    List<MethodSet> comparingRuleChoosing = ImprovementMethods.filter(new String[] {
      "lazy_furthest", "most_discriminate_teacher", "10"
    });

    
  }

  private static void updateMethodSetsWithResults() {
    //for(String filepath : csvFilePaths)
      //for(MethodSet methods : ImprovementMethods.methods) {
      String filepath = csvFilePaths[2];
      List<MethodSet> methods = ImprovementMethods.filter(new String[] {
        "first_rule", "most_discriminate_teacher", "10"
      });

      List<int[]> averages = new LinkedList<>();
      for(MethodSet methodSet : methods) {
        String data = "";
        
        try {  
          File myObj = new File(methodSet.imageFilename("chess") + ".txt");
          Scanner myReader = new Scanner(myObj);
          while (myReader.hasNextLine()) {
            data += myReader.nextLine();
            
            if(myReader.hasNextLine())
              data += "\n";
          }

          myReader.close();
        } catch (FileNotFoundException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }

        methodSet.result = new Result(data);
        averages.add(methodSet.result.getValues());
      }

      GraphGenerator.createAverageGraph(averages, 3, "./tmp/chess/average", "test");
      //}
  }

  private Teacher whichTeacherIsBetter() {
    return null;
  }

  private static void printL(List<Rule> L) {
    for (Rule rule : L) {
      System.out.println(rule.getLabel() + " : " + rule.getConjunction().toString());
    }
  }

  private static void printIteration(Data data, Data prediction, boolean teacherResponse) {
    String predictionStr = prediction == null ? "Default Data" : Arrays.toString(prediction.getAttributes());

    System.out.println("Example: " + Arrays.toString(data.getAttributes()));
    System.out.println("Predicted Label: " + prediction.getClassification());
    System.out.println("Explanation Example: " + predictionStr);
    System.out.println("Teacher Response: " + teacherResponse + "\n");
  }
}