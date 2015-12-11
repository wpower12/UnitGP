package unitgp;

import expression.*;
import java.util.Random;

public class ExpressionBuilder {
  private static Random rand = new Random();

  public ExpressionBuilder(){

  }

  public Expression getFullExpression( int depth ){
    return full_re(depth);
  }

  Expression full_re( int d ){
    Expression ret;
    if( d > 0 ){
      Expression t = full_re(d-1);
      Expression f = full_re(d-1);
      switch( rand.nextInt(4) ){
        case 0:
          ret = new IFUP( t, f );
          break;
        case 1:
          ret = new IFRIGHT(t, f);
          break;
        case 2:
          ret = new IFDOWN(t, f);
          break;
        case 3:
        default:
          ret = new IFLEFT(t, f);
          break;
      }
      return ret;
    } else {
      return new MOVE( rand.nextInt(4) );
    }
  }

  public static void main( String args[]){
    ExpressionBuilder eb = new ExpressionBuilder();

    Expression a = eb.getFullExpression(3);

    System.out.println( "Expression: "+a.print() );
  }

}