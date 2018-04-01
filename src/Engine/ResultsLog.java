/*
 * CSV File Writer for results of test
 */
package Engine;

import java.io.FileWriter;
import java.io.IOException;

public class ResultsLog {
  private static final String DELIMITER = ",";
  private static final String NEW_LINE_DELIMITER = "\n";
  
  private static final String LOG_FILE_NAME = "results.csv";
  
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
}
