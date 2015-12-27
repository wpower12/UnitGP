package expression;
import java.util.Random;

public class RAND extends Expression {
  private static Random rand = new Random();

  public RAND( Expression l, Expression r ){
    truebranch = l;
    falsebranch = r;
  }

  @Override
  public Expression mutate( float p ){
    Expression ret;

    if( rand.nextFloat() < p ){
      if( rand.nextFloat() < 0.9f ){
        switch( rand.nextInt( 2 ) ){
          case 0:
            ret = new IFDIR( rand.nextInt(4), null, null);
            break;
          case 1:
          default:
            ret = new RAND( null, null );
            break;
        }
        ret.truebranch  = truebranch.mutate( p );
        ret.falsebranch = falsebranch.mutate( p );
      } else {
        ret = new MOVE( rand.nextInt(4) );
      }
    } else {
      ret = new RAND( null, null );
      ret.truebranch  = truebranch.mutate( p );
      ret.falsebranch = falsebranch.mutate( p );
    }
    return ret;
  }

  @Override
  public Expression copy(){
    RAND ret = new RAND( null, null );
    // if( truebranch != null ){
      ret.truebranch = truebranch.copy();
    // }
    // if( falsebranch != null ){
      ret.falsebranch = falsebranch.copy();
    // }
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
