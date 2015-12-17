package expression;

public class Expression {
  public Expression truebranch;
  public Expression falsebranch;

  //must evaluate to some integer
  public int eval( int[] state ){
    return 1;
  }

  public boolean terminal(){
    return true;
  };
  public String print(){
    return "(EXPRESSION "+truebranch.print()+" "+falsebranch.print()+")";
  }
}