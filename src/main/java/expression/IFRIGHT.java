package expression;

public class IFRIGHT extends Expression {
    @Override
    public int eval( int[] state ){
      if( state[ 1 ] == 1 ){
        return truebranch.eval(state);
      } else {
        return falsebranch.eval(state);
      }
    }

    @Override
    public String print(){
      return "(IFRIGHT "+truebranch.print()+" "+falsebranch.print()+")";
    }
    
    @Override
    public boolean terminal(){
      return false;
    }
    public IFRIGHT( Expression t, Expression f ){
      truebranch = t;
      falsebranch = f;
    }
}