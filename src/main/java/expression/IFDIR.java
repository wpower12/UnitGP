package expression;

public class IFDIR extends Expression {
  private int dir;
  public IFDIR ( int d, Expression l, Expression r ){
    dir = d;
    truebranch = l;
    falsebranch = r;
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
