package expression;

public class IFUP extends Expression {

    public Expression truebranch;
    public Expression falsebranch;

    @Override
    public int eval( int[] state ){
      if( state[ 0 ] == 1 ){
        return truebranch.eval(state);
      } else {
        return falsebranch.eval(state);
      }
    }

    @Override
    public String print(){
      return "(IFUP "+truebranch.print()+" "+falsebranch.print()+")";
    }

    @Override
    public boolean terminal(){
      return false;
    }
    
    public IFUP( Expression t, Expression f ){
      truebranch = t;
      falsebranch = f;
    }
}