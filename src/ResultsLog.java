/*
 * CSV File Writer for results of test
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultsLog {
  private static final String DELIMITER = ",";
  private static final String NEW_LINE_DELIMITER = "\n";
  
  private static final String LOG_FILE_NAME = "results.csv";
  private static final String READ_WEIGHTS_FILE_NAME = "readweights.csv";
  private static final String POPULATION_LOG_FILE_NAME = "populations.csv";
  
  static Date date = new Date();
  static DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
  static String dateFormatted = formatter.format(date);
  
  private static final String TRAINING_ROUND_FILE_NAME = "traininground-" + dateFormatted+".csv";
  private static final String BEST_NODES_FILE_NAME = "bestnodes-" + dateFormatted+".csv";
  
  public static void writeBestNodes(double[] weights, long fitness){
    try {
      FileWriter pw = new FileWriter(BEST_NODES_FILE_NAME,true);
      
      for(int q = 0; q < weights.length; q++) {
        pw.append(Double.toString(weights[q]));
        pw.append(DELIMITER);
      }
      
      pw.append(Long.toString(fitness));
      pw.append(NEW_LINE_DELIMITER);
      pw.flush();
      pw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }   
  }
  
  public static void writeTrainingRound(long cycle, long island, double[] weights, long fitness){
    try {
      FileWriter pw = new FileWriter(TRAINING_ROUND_FILE_NAME,true);
      
      pw.append(Long.toString(cycle));
      pw.append(DELIMITER);
      
      pw.append(Long.toString(island));
      pw.append(DELIMITER);
      
      for(int q = 0; q < weights.length; q++) {
        pw.append(Double.toString(weights[q]));
        pw.append(DELIMITER);
      }
      
      pw.append(Long.toString(fitness));
      pw.append(NEW_LINE_DELIMITER);
      pw.flush();
      pw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }     
  }
  
  public static void writeBestNodesPerRound(long score, double[] weights){
    
  }
  
  public static void writeTrainerToFile(Trainer t, double score){
    try {
      FileWriter pw = new FileWriter(LOG_FILE_NAME,true);
      double[] weights = t.getWeights();
    
      /*
       * Format of output
       * Result - Weights
       */
    
      pw.append(Double.toString(score));
      pw.append(DELIMITER);
    
      for(int q = 0; q < weights.length; q++) {
        pw.append(Double.toString(weights[q]));
        pw.append(DELIMITER);
      }
      
      pw.append(NEW_LINE_DELIMITER);
      pw.flush();
      pw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
  }
  
  public static void writePopulationToFile(long pNum, long highest, long lowest, double mean) {
    try {
      FileWriter pw = new FileWriter(POPULATION_LOG_FILE_NAME, true);
      
      /*
       * Format of output
       * populationNumber (pNum) | highestFitnessScore | lowestFitnessScore | meanFitnessScore
       */

      pw.append(Long.toString(pNum));
      pw.append(DELIMITER);
      
      pw.append(Long.toString(highest));
      pw.append(DELIMITER);
      
      pw.append(Long.toString(lowest));
      pw.append(DELIMITER);

      pw.append(Double.toString(mean));
      pw.append(DELIMITER);
      
      pw.append(NEW_LINE_DELIMITER);
      pw.flush();
      pw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static double[] readWeightsFromFile(int lineNumber) {

    if (lineNumber < 1) {
      throw new Error("lineNumber must be larger than 0");
    }
    
    BufferedReader br;
    String line = "";
    double[] weights = new double[Genetic.INDIVIDUAL_NUMBER_OF_HEURISTICS];
    int lineCount = 1;
    String[] strWeights;
    try {
     
      br = new BufferedReader(new FileReader(READ_WEIGHTS_FILE_NAME));
      
      while((line = br.readLine()) != null) {
        System.out.printf("SDFSFD");
        if (lineCount != lineNumber) {
          lineCount++;
          continue;
        }
        
        strWeights = line.split(DELIMITER);
        
        // Start from 1 because we are ignoring the score (aka fitness Value)
        for(int i = 1; i < strWeights.length; i++) {
          weights[i - 1] = Double.parseDouble(strWeights[i]);
        }
        
        break;
      }
      System.out.printf("DONE");
      br.close();
      
      return weights;
    
    } catch (NumberFormatException | IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    return weights;
    
  }
}
