package expression;

public class IFLEFT implements Expression {

    public Expression truebranch;
    public Expression falsebranch;

    //state convention [UP, RIGHT, DOWN, LEFT]
    public int eval( int[] state ){
      if( state[ 3 ] == 1 ){
        return truebranch.eval(state);
      } else {
        return falsebranch.eval(state);
      }
    }

    public String print(){
      return "(IFLEFT "+truebranch.print()+" "+falsebranch.print()+")";
    }
    public boolean terminal(){
      return false;
    }
    public IFLEFT( Expression t, Expression f ){
      truebranch = t;
      falsebranch = f;
    }
}