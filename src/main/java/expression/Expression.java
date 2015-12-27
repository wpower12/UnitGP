package expression;
import java.util.Random;
//import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

public class Expression {
  public Expression truebranch;
  public Expression falsebranch;
  public int dir;

  //static final Logger logger = LogManager.getLogger( Expression.class.getName() );

  protected static Random rand = new Random();

  //must evaluate to some integer
  public int eval( int[] state ){
    return 1;
  }

  public boolean terminal(){
    return true;
  };

  public Expression copy(){
    return null;
  }

  public Expression mutate( float p ){
    return null;
  }

  public int getDepth(){
    if( (truebranch == null) && (falsebranch == null) ){
      return 1;
    } else {
      if( truebranch == null ){
        return 1+falsebranch.getDepth();
      } else if ( falsebranch == null ){
        return 1+truebranch.getDepth();
      } else {
        return 1+Math.max(falsebranch.getDepth(), truebranch.getDepth());
      }
    }
  }

  public String print(){
    return "(EXPRESSION "+truebranch.print()+" "+falsebranch.print()+")";
  }
}
