package expression;

public class IFRIGHT implements Expression {

    Expression truebranch;
    Expression falsebranch;

    //state convention [UP, RIGHT, DOWN, LEFT]
    public int eval( int[] state ){
      if( state[ 1 ] == 1 ){
        return truebranch.eval(state);
      } else {
        return falsebranch.eval(state);
      }
    }

    public String print(){
      return "(IFRIGHT "+truebranch.print()+" "+falsebranch.print()+")";
    }
    
    public IFRIGHT( Expression t, Expression f ){
      truebranch = t;
      falsebranch = f;
    }
}