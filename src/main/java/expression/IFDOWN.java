package expression;

public class IFDOWN implements Expression {

  Expression truebranch;
  Expression falsebranch;

  //state convention [UP, RIGHT, DOWN, LEFT]
  public int eval( int[] state ){
    if( state[ 2 ] == 1 ){
      return truebranch.eval( state );
    } else {
      return falsebranch.eval(state);
    }
  }

  public String print(){
    return "(IFDOWN "+truebranch.print()+" "+falsebranch.print()+")";
  }

  public IFDOWN( Expression t, Expression f ){
    truebranch = t;
    falsebranch = f;
  }
}