package expression;

public class IFLEFT extends Expression {

    @Override
    public int eval( int[] state ){
      if( state[ 3 ] == 1 ){
        return truebranch.eval(state);
      } else {
        return falsebranch.eval(state);
      }
    }
    @Override
    public String print(){
      return "(IFLEFT "+truebranch.print()+" "+falsebranch.print()+")";
    }
    @Override
    public boolean terminal(){
      return false;
    }
    public IFLEFT( Expression t, Expression f ){
      truebranch = t;
      falsebranch = f;
    }
}