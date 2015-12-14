package expression;

public class MOVE implements Expression {
  int dir;
  public Expression truebranch = null;
  public Expression falsebranch = null;

  public MOVE( int d ){
    dir = d;
  }

  public String print(){
    return "(MOVE "+dir+")";
  }

  public boolean terminal(){
    return true;
  }

  public int eval( int[] state ){
    return dir;
  }
}