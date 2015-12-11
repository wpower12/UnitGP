package expression;

public class IFUP implements Expression {

    Expression truebranch;
    Expression falsebranch;

    //state convention [UP, RIGHT, DOWN, LEFT]
    public int eval( int[] state ){
      if( state[ 0 ] == 1 ){
        return truebranch.eval(state);
      } else {
        return falsebranch.eval(state);
      }
    }

    public String print(){
      return "(IFUP "+truebranch.print()+" "+falsebranch.print()+")";
    }
    
    public IFUP( Expression t, Expression f ){
      truebranch = t;
      falsebranch = f;
    }
}