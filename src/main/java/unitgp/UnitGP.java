package unitgp;
import java.util.*;
import simulation.*;
import expression.*;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

public class UnitGP {
  /**
  According to Koza there are roughly 19 basic parameters of a GP run. We only
  use those required for the operations we are actually implementing.  For example,
  no mutation is being applied during the selection phase, so we can omit the
  parameters related to it.
  */
  private static int generations = 200;
  private int populationSize    = 128;  //must be power of 2 for now.
  private float perReproduce   = 0.1f;
  private float perCrossover   = 0.9f;
  private float probCOFunction  = 0.9f;
  private float probCOTerminal  = 0.1f;
  private int maxCrossoverDepth = 5;
  private int maxInitialDepth   = 7;
  //Calculated later.
  private int numCrossover;
  private int numReproduce;

  private Random rand;
  private GridSimulation sim;
  private ExpressionBuilder eb;
  private List<Individual> population;
  // private static Logger logger = LogManager.getLogger();

  /**
   * Represents a Genetic Programming procedure that evolves expressions
   * representing the behavior of a unit in a simulation.
   *
   * The Function and Terminal set of expressions are currently tightly coupled
   * between the expression package, the GridSimulation class, and the
   * ExpressionBuilder class.  Abstracting the sets of functions and terminals
   * is a big next step.  However, the current system is working and a decent
   * starting point.
   *
   * @see GridSimulation
   * @see ExpressionBuilder
   */
  public UnitGP(){
    rand = new Random();
    eb   = new ExpressionBuilder( );
    sim  = new GridSimulation();
    initialize();
  }

  /**
   * Runs the Genetic Programming process.
   *
   * @param generations number of generations to run
   */
  public void run( int g ){
    // logger.info("Running for "+generations+" generations.");
    for( int i = 0; i < g; i++ ){
      evaluate();
      // logger.info( "Pop: "+printPop() );
      // logger.info( "Selecting...");
      select();
    }
  }

  /**
   * Simulates the current best individual in the population.  Uses an instance
   * of the GridSimulation class to display the behavior of an Individual.
   *
   * @see GridSimulation
   * @see Individual
   */
  public void simulateBest(){
    cWeightPopulation();
    Collections.sort( population );
    System.out.println(population.get(0).print());
    sim.graphicEvaluate(population.get(0));
  }

  /**
   * Main entry point of the app.  Runs a simulation for a set number of
   * generations and then simulates the best resulting indvidual.
   *
   * @param args command line arguments.
   */
  public static void main( String args[] ){
    // logger.info("Entered Main");
    UnitGP exampleRun = new UnitGP();
    exampleRun.run( generations );
    exampleRun.simulateBest();
  }

  /***************************************************************************
  * Private Methods
  */

  /**
   * Creates the inital population of individuals.  Implements the 'Ramped Half and Half'
   * method outlined in Koza-92.  The possible individuals are grouped into a
   * (n-2) many equal groupings, each group representing a depth, from 2 to n.
   * For each of these groups, half the individuals are grown with the 'Full'
   * method of expression tree growth, and the other half with the 'Grow' method.
   * These are outlined in the ExpressionBuilder class.
   *
   * @see ExpressionBuilder Details 'Full' and 'Grow' methods.
   */
  private void initialize(){
    population = new ArrayList<>();
    int depth;
    for( int i = 0; i < populationSize/2; i++ ){
      depth = (int)(2.0f*(float)i*(float)(maxInitialDepth)/(float)populationSize)+2;
      population.add( new Individual( eb.getFullExpression( depth) ) );
      population.add( new Individual( eb.getGrowExpression( depth) ) );
    }

    numCrossover = (int)Math.floor(((float)populationSize*perCrossover));
    if( numCrossover%2 != 0 ){
      numCrossover--;
    }
    numReproduce = populationSize - numCrossover;
  }

  /**
   * Each individual is passed to the simulation to be evaluated.  The simulation
   * runs its procedures and updates the fitness field of the indvidual.
   *
   * @see GridSimulation.evaluate()
   */
  private void evaluate(){
    for( Individual i : population ){
      sim.evaluate(i);
    }
  }

  /**
   * 'Selects' members of the current population for reproduction and/or
   * crossover.
   *
   * Treats the current population as immutable wrt the individuals
   * genome (its root expression).  The newPop collection is filled
   * with copies of members of the original population or the crossovers of copies.
   *
   * For reproduction, this means doing the initial random weighted sort
   * and copying the top members.
   *
   * For crossover, randomly weight and sort, and make copies of the top two
   * individuals.  These two copies can have their expressions messed with, and
   * then they are added to the new population.
   *
   * @see Individual.crossover() Clusterfuck of a crossover method.
   */
  private void select(){
    List<Individual> newPop = new ArrayList<>();

    //Individuals that reproduce
    rWeightPopulation();  //Re-randomly-weighting and sorting gives us a simple
                          //way to handle fitness proportional selection.  This
                          //is also used later to pick parents for crossover.
    Collections.sort( population );
    for( int i = 0; i < numReproduce; i++ ){
      newPop.add( population.get(i).copy() );
    }

    //Pairs that crossover
    for( int c = 0; c < numCrossover/2; c++ ){
      rWeightPopulation(); //re-weighting/sort happens for every pair.
      Collections.sort(population);
      Individual p1 = population.get(0).copy();
      Individual p2 = population.get(1).copy();
      Individual.crossover( p1, p2 );
      newPop.add( p1 );
      newPop.add( p2 );
    }
    population = newPop;
  }

  /**
   * Applys a random weighting to all members of the population.  The
   * weightedFitness field of each indivdual is set to its fitness*randFloat.
   */
  private void rWeightPopulation(){
    for( Individual i : population ){
      i.weightedFitness = (int)((float)i.standardizedFitness * rand.nextFloat());
    }
  }

  private void cWeightPopulation(){
    for( Individual i : population ){
      i.weightedFitness = i.standardizedFitness;
    }
  }
  /**
   * Returns a string of a summary of the population.
   */
  private String printPop(){
    //Print old pop
    String ret = "pop: ( ";
    for( Individual i : population ){
      ret += i.fitness+"/"+i.weightedFitness+" ";
    }
    ret +=")";
    return ret;
  }
}
