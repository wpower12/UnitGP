package expression;

public class IFRIGHT implements Expression {

    public Expression truebranch;
    public Expression falsebranch;

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
    public boolean terminal(){
      return false;
    }
    public IFRIGHT( Expression t, Expression f ){
      truebranch = t;
      falsebranch = f;
    }
}