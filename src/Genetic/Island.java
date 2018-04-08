package Genetic;

public class Island {
  private Population population;
  
  public void setPopulation(Population latestPopulation){
    population = latestPopulation;
  }

  public Population getPopulation(){
    return population;
  }
}
