package expression;
import java.util.Random;

public class RAND extends Expression {
  private static Random rand = new Random();

  public RAND( Expression l, Expression r ){
    truebranch = l;
    falsebranch = r;
  }

  @Override
  public Expression copy(){
    RAND ret = new RAND( null, null );
    if( truebranch != null ){
      ret.truebranch = truebranch.copy();
    }
    if( falsebranch != null ){
      ret.falsebranch = truebranch.copy();
    }
    return ret;
  }

  @Override
  public boolean terminal(){
    return false;
  }

  @Override
  public int eval( int[] state ){
    if( rand.nextInt(2) == 0 ){
      return truebranch.eval(state);
    } else {
      return falsebranch.eval(state);
    }
  }

  @Override
  public String print(){
    return "(RAND "+truebranch.print()+" "+falsebranch.print()+")";
  }
}
