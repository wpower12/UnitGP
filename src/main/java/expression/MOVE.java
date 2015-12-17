package expression;

public class MOVE extends Expression {
  int dir;

  public MOVE( int d ){
    dir = d;
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
    return "MOVE"+dir_s;
  }

  @Override
  public boolean terminal(){
    return true;
  }

  @Override
  public int eval( int[] state ){
    return dir;
  }
}
