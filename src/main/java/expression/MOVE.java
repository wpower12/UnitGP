package expression;

public class MOVE extends Expression {
  public MOVE( int d ){
    dir = d;
  }

  @Override
  public Expression copy(){
    return new MOVE(this.dir);
  }

  @Override
  public Expression mutate( float p ){
    Expression ret;

    if( rand.nextFloat() < p ){
      ret = new MOVE( rand.nextInt(4) );
    } else {
      ret = new MOVE( dir );
    }
    return ret;
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
