package expression;

public class IFDIR extends Expression {

  public IFDIR ( int d, Expression l, Expression r ){
    dir = d;
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
      ret = new IFDIR( dir, null, null );
      ret.truebranch  = truebranch.mutate( p );
      ret.falsebranch = falsebranch.mutate( p );
    }
    return ret;
  }

  @Override
  public Expression copy(){
    IFDIR ret = new IFDIR( this.dir, null, null );
    if( truebranch != null ){
      ret.truebranch = truebranch.copy();
    }
    if( falsebranch != null ){
      ret.falsebranch = falsebranch.copy();
    }
    return ret;
  }

  @Override
  public int eval( int[] state ){
    if( state[ dir ] == 1 ){
      return truebranch.eval(state);
    } else {
      return falsebranch.eval(state);
    }
  }

  @Override
  public String print(){
    String dir_s;
    switch( dir ){
      case 0:
        dir_s = "UP ";
        break;
      case 1:
        dir_s = "RIGHT ";
        break;
      case 2:
        dir_s = "DOWN ";
        break;
      case 3:
      default:
        dir_s = "LEFT ";
    }
    return "(IF"+dir_s+truebranch.print()+" "+falsebranch.print()+")";
  }

  @Override
  public boolean terminal(){
    return false;
  }
}
