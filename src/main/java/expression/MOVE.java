package expression;

public class MOVE extends Expression {
  int dir;

  public MOVE( int d ){
    dir = d;
  }

  @Override
  public String print(){
    return "(MOVE "+dir+")";
  }

  @Override
  public boolean terminal(){
    return true;
  }

  @Override
  public int eval( int[] state ){
    return dir;
  }
}