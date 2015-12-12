package unitgp;
import java.util.*;
import simulation.*;
import expression.*;

public class UnitGP {
  //There are roughly 19 basic parameters of a GP run, according to Koza
  //We only use the ones for the operations we are actually implementing,
  //with another given by the client when calling run.
  private int populationSize    = 8;  //must be power of 2 for now.
  private float probReproduce   = 0.1f;
  private float probCrossover   = 0.9f;
  private int numCrossover;
  private int numReproduce;
  private float probCOFunction  = 0.9f;
  private float probCOTerminal  = 0.1f;
  private int maxCrossoverDepth = 5;
  private int maxInitialDepth   = 4;

  private List<Individual> population;

  private ExpressionBuilder eb;

  private Random rand;
  private GridSimulation sim;

  public UnitGP(){
    rand = new Random();
    eb   = new ExpressionBuilder( );
    sim  = new GridSimulation();
    initialize();
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
    Collections.sort( population );
    System.out.println(population.get(0).print());
    sim.graphicEvaluate(population.get(0));

  }

  public static void main( String args[] ){
    UnitGP exampleRun = new UnitGP();
    exampleRun.run( 10 );
    exampleRun.simulateBest();
  }

  /**
  * Private Methods
  */
  private void initialize(){
    population = new ArrayList<>();
    System.out.print("init depths - ( ");
    int depth;
    for( int i = 0; i < populationSize/2; i++ ){
      depth = (int)(2.0f*(float)i*(float)(maxInitialDepth)/(float)populationSize)+2;
      System.out.print( depth+" " );
      population.add( new Individual( eb.getFullExpression( depth) ) );
      population.add( new Individual( eb.getGrowExpression( depth) ) );
    }
    System.out.println(")");

    numCrossover = (int)Math.floor(((float)populationSize*probCrossover));
    if( numCrossover%2 != 0 ){
      numCrossover--;
    }
    numReproduce = populationSize - numCrossover;
    System.out.println("CO: "+numCrossover+" R: "+numReproduce);
  }

  private void evaluate(){
    for( Individual i : population ){
      sim.evaluate(i);  //Sets an Individuals fitness and standardizedFitness
    }
  }

  private void select(){
    //Fitness proportional selection with sorting and random weights!
    List<Individual> newPop = new ArrayList<>();
    rWeightPopulation();
    Collections.sort( population );
    printPop();

    //Individuals that reproduce
    for( int i = 0; i < numReproduce; i++ ){
      newPop.add( population.get(i) );
    }

    //Pairs that crossover
    List<Individual> newPair = new ArrayList<>();
    for( int c = 0; c < numCrossover/2; c++ ){
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

  private void printPop(){
    //Print old pop
    System.out.print("pop: ( ");
    for( Individual i : population ){
      System.out.print( i.fitness+" " );
    }
    System.out.println(")");
  }
}