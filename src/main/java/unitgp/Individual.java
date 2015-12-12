package unitgp;
import expression.*;

public class Individual implements Comparable {
  Expression root;
  public int fitness;
  public int standardizedFitness;
  public int weightedFitness;

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