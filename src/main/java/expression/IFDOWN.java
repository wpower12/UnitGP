package expression;

public class IFDOWN extends Expression {

  @Override
  public int eval( int[] state ){
    if( state[ 2 ] == 1 ){
      return truebranch.eval( state );
    } else {
      return falsebranch.eval(state);
    }
  }

  @Override
  public String print(){
    return "(IFDOWN "+truebranch.print()+" "+falsebranch.print()+")";
  }

  @Override
  public boolean terminal(){
    return false;
  }

  public IFDOWN( Expression t, Expression f ){
    truebranch = t;
    falsebranch = f;
  }
}