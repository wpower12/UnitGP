package unitgp;
import expression.*;
import java.util.Random;

public class Individual implements Comparable {
  Expression root;
  public int fitness;
  public int standardizedFitness;
  public int weightedFitness;

  public int depth;

  private static Random rand = new Random();

  public static void crossover( Individual p1, Individual p2 ){

    //Exchange random nodes in the two Individuals expressions.
    int maxdepth = p1.getDepth();
    boolean hit = false;
    Expression prev_a = p1.root;
    Expression a;
    boolean a_branch;
    if( rand.nextFloat() > 0.5f ){
      a = prev_a.truebranch;
      a_branch = true;
    } else {
      a = prev_a.falsebranch;
      a_branch = false;
    }
    int depth = 2;
    // System.out.println("COStart A_Prev: "+ prev_a.print());
    // System.out.println("COStartA: "+ prev_a.terminal());
    while( !(hit || a.terminal()) ){
      //See if we get a hit
      if( rand.nextFloat() < 1.0f/(float)depth ){
        //If we do, we use this as our node.
        hit = true;
      } else {
        Expression t = a;
        if( rand.nextFloat() > 0.5f ){
          a = prev_a.truebranch;
          a_branch = true;
        } else {
          a = prev_a.falsebranch;
          a_branch = false;
        }
        prev_a = t;
        depth++;
      }
      // System.out.println("COA_Prev: "+ prev_a.print());
      // System.out.println("COA: "+ a.print());
    }

    maxdepth = p2.getDepth();
    hit = false;
    Expression prev_b = p2.root;
    Expression b = p2.root;
    boolean b_branch;
    if( rand.nextFloat() > 0.5f ){
      b = b.truebranch;
      b_branch = true;
    } else {
      b = b.falsebranch;
      b_branch = false;
    }
    depth = 2;
    // System.out.println("COStart B_Prev: "+ prev_a.print());
    // System.out.println("COStartB: "+ a.print());
    while( !(hit || b.terminal()) ){
      //See if we get a hit
      if( rand.nextFloat() < 1.0f/(float)depth ){
        //If we do, we use this as our node.
        hit = true;
      } else {
        prev_b = b;
        if( rand.nextFloat() > 0.5f ){
          b = b.truebranch;
          b_branch = true;
        } else {
          b = b.falsebranch;
          b_branch = false;
        }
        depth++;
      }
      // System.out.println("COB_Prev: "+ prev_b.print());
      // System.out.println("COB: "+ b.print());
    }

    //Swap the two nodes by reassinging the pointers in the expressions
    if( a_branch ){
      prev_a.truebranch = b;
    } else {
      prev_a.falsebranch = b;
    }
    if( b_branch ){
      prev_b.truebranch = a;
    } else {
      prev_b.falsebranch = a;
    }
  }

  public Individual( Expression r ){
    root = r;
  }

  public String print(){
    return root.print();
  }

  public int evaluate( int[] state ){
    //Evaluates the given expression using the state provided.
    return root.eval(state);
  }

  //Return the depth of the root expression
  public int getDepth(){
    return 5;
  }

  /**
   * Returns a deep copy of the individual and its expression tree.
   *
   * @return Individual the deep copy of the Individual
   */
  public Individual copy(){
    Individual ret = new Individual( null );
    ret.root = this.root.copy();
    ret.fitness = this.fitness;
    ret.standardizedFitness = this.standardizedFitness;
    ret.weightedFitness = this.weightedFitness;
    return ret;
  }

  @Override
  public int compareTo(Object o) {
    Individual p = (Individual) o;
    int res = (new Integer(weightedFitness)).compareTo(p.weightedFitness);
    int ret;
    if( res > 0 ){
      ret = -1;
    } else if ( res < 0 ){
      ret = 1;
    } else {
      ret = 0;
    }
    return ret;
  }
}
