package simulation;
import unitgp.Individual;
import java.util.Random;

public class GridSimulation {
  private int MAXGENERATIONS;
  private int STARTINGHEALTH;
  private int FOODVALUE;
  private int SIZE;
  private float FOODDENSITY;
  private int[][] grid;
  private Random rand;
  private int x, y; //Unit Location
  private int health;
  private boolean alive;

  public GridSimulation(){
    SIZE = 20;
    STARTINGHEALTH = 15;
    FOODVALUE = 5;
    MAXGENERATIONS = 200; //Should be less than 20*20
    FOODDENSITY = 0.1f;   //How much food to place.
    rand = new Random();
    grid = new int[SIZE][SIZE];
  }

  public void evaluate( Individual ind ){
    placeFood();
    placeUnit();

    int g = 0;
    while( ( g++ < MAXGENERATIONS) && alive() ){
      moveUnit( ind );  //Uses the s-expression stored in ind to pick a move
    }
    //g is the raw fitness of the unit.
    ind.fitness = g;
    ind.standardizedFitness = g;  //For now.
  }

  private void placeFood(){
    //Randomly places food (sets a 1) in the grid
    for( int i = 0; i < SIZE; i++ ){
      for( int j = 0; j < SIZE; j++){
        if( rand.nextFloat() < FOODDENSITY ){
          grid[i][j] = 1;
        } else {
          grid[i][j] = 0;
        }
      }
    }
  }

  private void placeUnit(){
    //picks a new x/y location for the unit
    x = (int)(rand.nextFloat() * (float)SIZE);
    y = (int)(rand.nextFloat() * (float)SIZE);
    //Also we reset the starting health
    health = STARTINGHEALTH;
  }

  private boolean alive(){
    return health > 0;
  }

  private void moveUnit(Individual i){
    health--;
    int[] state;
    state = getState();

    //we interpret the result of the expression as a move:
    switch( i.evaluate(state) ){
      case 0:
        //Up
        break;
      case 1:
        //Right
        break;
      case 2:
        //Down
        break;
      case 3:
        //Left
        break;
      default:
        //Do nothing
        break;
    }

    //Did you find food?
    if( grid[x][y] == 1 ){
      health += FOODVALUE;
      grid[x][y] == 0;
    }
  }

  private int[] getState(){
    //return 0/1 values for the up,right,down,left cells
    int[] ret = new int[4];
    return ret;
  }
}