package simulation;
import unitgp.Individual;
import java.util.Random;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

//For testing
import unitgp.ExpressionBuilder;

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

  //Graphics!

  public GridSimulation(){
    SIZE = 20;
    STARTINGHEALTH = 10;
    FOODVALUE = 3;
    MAXGENERATIONS = 400; //Should be around the SIZE*SIZE*FOODDENSITY*FOODVALUE
    FOODDENSITY = 0.2f;   //How much food to place.
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

  public void graphicEvaluate( Individual ind ){

    placeFood();
    placeUnit();

    //Creates the context.
    int SCALE = 20;
    JFrame f = new JFrame("Simulating Individual");
    f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    f.setSize(SIZE*(SCALE+1)+1, SIZE*(SCALE+1)+1);

    JPanel p = new GridPanel( grid, SIZE, SCALE );
    f.add(p);
    f.pack();
    f.setVisible(true);

    int g = 0;
    while( ( g++ < MAXGENERATIONS) && alive() ){
      moveUnit( ind );  //Uses the s-expression stored in ind to pick a move
      try {
        Thread.sleep(250);
        p.removeAll();
        p.validate();
        p.repaint();
      } catch(InterruptedException e) { Thread.currentThread().interrupt(); }
    }
    System.out.println("Finished "+g);
  }

  public static void main( String args[]){
    ExpressionBuilder eb = new ExpressionBuilder();
    GridSimulation sim = new GridSimulation();
    sim.graphicEvaluate( new Individual( eb.getFullExpression(2) ) );
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
    grid[x][y] = 2;
    //Also we reset the starting health
    health = STARTINGHEALTH;
  }

  private boolean alive(){
    return health > 0;
  }

  private void moveUnit(Individual i){
    grid[x][y] = 0;
    health--;
    int[] state;
    state = getState();

    //we interpret the result of the expression as a move:
    switch( i.evaluate(state) ){
      case 0:
      //Up
      y = (y == 0) ? SIZE-1 : y-1;
      break;
      case 1:
      //Right
      x = (x == SIZE-1) ? 0 : x+1;
      break;
      case 2:
      //Down
      y = (y == SIZE-1) ? 0 : y+1;
      break;
      case 3:
      //Left
      x = (x == 0) ? SIZE-1 : x-1;
      break;
      default:
      //Do nothing
      break;
    }

    //Did you find food?
    if( grid[x][y] == 1 ){
      health += FOODVALUE;
    }
    grid[x][y] = 2;
  }

  private int[] getState(){
    //return 0/1 values for the up,right,down,left cells
    int[] ret = new int[4];
    ret[0] = grid[x][ (y == 0) ? SIZE-1 : y-1];
    ret[1] = grid[(x == SIZE-1) ? 0 : x+1][y];
    ret[2] = grid[x][(y == SIZE-1) ? 0 : y+1];
    ret[3] = grid[(x == 0) ? SIZE-1 : x-1][y];

    return ret;
  }
}