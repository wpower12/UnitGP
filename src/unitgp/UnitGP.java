package unitgp;
import java.util.*;
/**
  UnitGP

  Evolve the behaviour of a simulated unit on a grid.

  Educational implementation of the Genetic Programming Paradigm described
  in "Koza 92' Genetic Programming".

  The structure undergoing adaptation will be simple s-expressions, stored
  as rooted trees.

  The function set will be
    IFGZ - If Greater Than 0
         - Accepts a terminal
         - executes left child if greater than 0
         - exectures right child otherwise
  The terminal set will be
    m_l, m_r, m_u, m_d - Move commands, 1 for each cardinal direction
    s_l, s_r, s_u, s_d - Neighbor state, 1 for each cardinal direction

  The resulting s-expression tree will be used to decide the behaviour of
  a <Unit> on a grid.  The grid will contain integer values, with a 0 being
  empty space, and a 1 being 'food'.  The commands listed in ther terminal
  set will move the unit to the corresponding cell.

  The unit will have a health value.  This is an integer value that starts
  at some positive value.  Each time step, it reduces by 1.  Eating food
  increases the health value by some set amount.

  When a unit moves into a cell with a 1, it is replaced by 0, or eaten.

  The fitness of a given s-expression tree is the number of generations the
  cell stays alive.

*/
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

  public UnitGP(){
    //TODO - Initialize the population
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
    System.out.println("Somethings working");
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

  }

  private void evaluate(){

  }

  private void select(){

  }



}