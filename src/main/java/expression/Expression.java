package expression;

public interface Expression {

  //must evaluate to some integer
  int eval( int[] state );
  boolean terminal();
  String print();
}