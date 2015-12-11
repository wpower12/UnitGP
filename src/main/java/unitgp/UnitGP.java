package unitgp;
import java.util.*;
import simulation.*;

public class UnitGP {
  //There are roughly 19 basic parameters of a GP run, according to Koza
  //We only use the ones for the operations we are actually implementing,
  //with another given by the client when calling run.
  private int populationSize    = 200;
  private float probReproduce   = 0.1f;
  private float probCrossover   = 0.9f;
  private float probCOFunction  = 0.9f;
  private float probCOTerminal  = 0.1f;
  private int maxCrossoverDepth = 17;
  private int maxInitialDepth   = 6;

  private List<Individual> population;
  private List<Expression> functions;
  private List<Terminal>  terminals;

  private Random rand;
  private GridSimulation sim;

  public UnitGP(){
    rand = new Random();
    initialize();
    //Set up the simulation.
    sim = new GridSimulation();
  }

  public void run( int generations ){
    for( int i = 0; i < generations; i++ ){
      evaluate();   //Run the simulation on each individual and obtain fitness
      select();     //Apply the reproduction and crossover operations
    }
  }

  public void simulateBest(){
    //TODO - Use some graphics API to show a graphic representation of
    //       an individual unit moving about the grid for a set number of
    //       generations.
  }

  public static void main( String args[] ){
    UnitGP exampleRun = new UnitGP();
    exampleRun.run( 100 );
    exampleRun.simulateBest();
  }

  /**
  * Private Methods
  */
  private void initialize(){
    population = new ArrayList<>();

    //TODO - Implement the ramped 1/2 and 1/2
    //For s = 2 to MaxStartDepth
    //  (s/MaxStartDepth)*populationSize many new individuals
    //  1/2 make with full Method
    //  1/2 make with grow method.

  }

  private void evaluate(){
    for( Individual i : population ){
      sim.evaluate(i);  //Sets an Individuals fitness and standardizedFitness
    }
  }

  private void select(){
    //To implement fitness proportional selection, we the calculate the
    //standardized fitness in such a way as to also randomly multiply
    //each weight by a 0-1 float.  When we sort population by this
    //value, we get an ordered list of candidates for reproduction and
    //crossover

    //To get pairs for crossover, we reapply the random weights and resort
    //the first two individuals are the ones used

    List<Individual> newPop = new ArrayList<>();
    rWeightPopulation();
    Collections.sort( population );

    //Individuals that reproduce
    int numSelected = (int)((float)populationSize*probReproduce);
    for( int i = 0; i < numSelected; i++ ){
      newPop.add( population.get(i) );
    }

    //Pairs that crossover
    List<Individual> newPair = new ArrayList<>();
    int numCrossOver = (int)((float)populationSize*probCrossover);
    for( int c = 0; c < numCrossOver; c++ ){
      rWeightPopulation();
      Collections.sort(population);
      newPair = crossover( population.get(0), population.get(1) );
      newPop.add( newPair.get(0) );
      newPop.add( newPair.get(1) );
    }
    population = newPop;  //Set new population
  }

  private ArrayList<Individual> crossover( Individual p1, Individual p2 ){
    ArrayList<Individual> ret = new ArrayList<>();
    ret.add(p1);
    ret.add(p2);
    return ret;
  }

  private void rWeightPopulation(){
    for( Individual i : population ){
      i.weightedFitness = (int)((float)i.standardizedFitness * rand.nextFloat());
    }
  }

}