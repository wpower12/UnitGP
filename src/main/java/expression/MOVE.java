package expression;

public class MOVE implements Expression {
  int dir;
  public MOVE( int d ){
    dir = d;
  }

  public String print(){
    return "(MOVE "+dir+")";
  }

  public int eval( int[] state ){
    return dir;
  }
}