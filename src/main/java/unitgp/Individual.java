package unitgp;

public class Individual implements Comparable {
  Expression root;
  public int fitness;
  public int standardizedFitness;
  public int weightedFitness;

  public Individual(){

  }

  public int evaluate( int[] state ){
    //Evaluates the given expression using the state provided.
    return 1;
  }

  @Override
  public int compareTo(Object o) {
    Individual p = (Individual) o;
    return (new Integer(weightedFitness)).compareTo(p.weightedFitness);
  }
}