package unitgp;

import expression.*;
import java.util.Random;

public class ExpressionBuilder {
  private static Random rand = new Random();

  public ExpressionBuilder(){

  }

  public Expression getFullExpression( int depth ){
    //Hack to ensure no expression starts with a terminal
    Expression t_branch = full_re( depth-1 );
    Expression f_branch = full_re( depth-1 );
    return new IFDIR( rand.nextInt(4), t_branch, f_branch );
  }

  private Expression full_re( int d ){
    Expression ret;
    if( d > 0 ){
      Expression t = full_re(d-1);
      Expression f = full_re(d-1);
      return new IFDIR( rand.nextInt(4), t, f );
    } else {
      return new MOVE( rand.nextInt(4) );
    }
  }

  public Expression getGrowExpression( int depth ){
    Expression t_branch = grow_re( depth-1 );
    Expression f_branch = grow_re( depth-1 );
    return new IFDIR( rand.nextInt(4), t_branch, f_branch );
  }

  private Expression grow_re( int d ){
    Expression ret;
    if( d > 0 ){
      if( rand.nextFloat() > 0.1f ){
        Expression t = full_re(d-1);
        Expression f = full_re(d-1);
        return new IFDIR( rand.nextInt(4), t, f );
      } else {
        return new MOVE( rand.nextInt(4) );
      }
    } else {
      return new MOVE( rand.nextInt(4) );
    }
  }

  public static void main( String args[]){
    ExpressionBuilder eb = new ExpressionBuilder();
    Expression a = eb.getFullExpression(3);
    System.out.println( "Expression: "+a.print() );

    Expression b = eb.getFullExpression(3);
    System.out.println( "Expression: "+b.print() );
  }

}
