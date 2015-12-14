# UnitGP

![alt text](http://imgur.com/vEVqXSd.png "Grid and Expressions")

Evolving the behavior of a simple food-seeking unit on a grid with
[genetic programming](https://en.wikipedia.org/wiki/Genetic_programming).

___

## ToC

+ [Overview](#overview)
  * [What Is GP?](#o_gp)
    - [S-Expressions](#o_gp_sexp)
    - [Fitness](#o_gp_fit)
    - [Genetic Operations](#o_gp_ops)
  * [Simulation](#o_sim)
+ [Encoding Behavior](#encode)
  * [Choosing Expressions](#en_chose)
  * [Expressions in Java](#en_java)
+ [Genetic Programming](#gp)
  * [Initializing](#gp_init)
  * [Evaluating](#gp_eval)
  * [Selecting](#gp_select)
+ [Conclusion](#con)
  * [Expected Behavior](#con_expected)
  * [Interesting Solutions](#con_solns)
  * [Next Steps](#con_next)
+ [References](#ref)

## Overview <a id="overview"></a>

Educational implementation of the Genetic Programming Paradigm described
in [Koza 92' Genetic Programming](http://www.amazon.com/exec/obidos/ASIN/0262111705/geneticprogrammi).

### What Is Genetic Programming? <a id="o_gp"></a>

Genetic programming is a method of generating a procedure.  Normally, a procedure
is created to solve a problem.  We write software to meet a requirement, we implement
a specific algorithm to accomplish a set task.  Typically, these things are done in
a direct manner.  The problem is reasoned about, and the procedure is crafted
and tested until it does what it needs.

Genetic programming instead generates a procedure by imitating the process of
natural selection.  A population of individuals is created, each one representing
a random attempt at solving the problem.  Each individual would be some possible
procedure.  

This population is then evaluated.  Each individual, in the context of the problem,
is given some numerical evaluation.

If the procedure represents a possible mathematical function, the fitness might
be the error when fed a test set.  Or if the procedure represents a path through
a grid filled with coins, the fitness may be the number of coins collected.  

Representing a problem or procedure in this way can be difficult, but there is a
mathematical construct that helps us visualize the ways procedures can be connected,
manipulated, and built.

The S-Expression!  While this topic is very deep, for our purposes we can consider
the s-expressions as representations of functions.  The main benefit of them is
the ability to view functions and procedures as trees.

#### S-Expressions <a id="o_gp_sexp"></a>

  S-expressions are defined formally as...

  As an example, consider the following s-expressions representing simple
  arathmatic functions.

  ```lisp
  (ADD A B)
  (SUB A B)
  (MUL A B)
  ```
  Each expression can take as a parameter either an expression, or a terminal
  value.  Suppose we want to only deal with integers. Then the following would
  be well-defined s-expressions.

  ```lisp
  (ADD (ADD 2 3) 1)  //evaluates to 6
  (ADD (MUL 2 (SUB 5 2)) 1) //evaluates to 7
  ```

  By selecting a set of functions that all return the same type, and a set of
  terminals that are of that type, we create a set of expressions that are
  closed.  This is a very important property.  This gives us the assurance that
  no matter how we nest the expressions, if we eventually fill all possible
  leafs with terminal nodes, the full expression will evaluate correctly.

  When we are able to encode a problems possible solutions into a set of atomic
  s-expressions, we open up the possibility of using genetic programming.  The
  problem of evolving the new procedures now becomes a problem of building trees
  of s-expressions, and clipping and recombining them into new possible solutions.

#### Fitness <a id="o_gp_fit"></a>

  Determining fitness...

  Using the simulation...

#### Genetic Operations <a id="o_gp_ops"></a>

  Reproduction...

  Crossover...

## Encoding Behavior <a id="encode"></a>

  Now it comes time to actually encode the context of the simulation into some
  atomic expressions.  A best practice is to keep the number of atomic Operations
  small and simple.  

### Choosing Expressions <a id="en_choose"></a>

  If all the unit can do is move in the four cardinal directions, and all it
  can see is the value of the cells next to it, then we could decide on
  a set of methods like the following to encode the behavior.

  ```lisp
    (IFUP    TEXP FEXP)
    (IFDOWN  TEXP FEXP)
    (IFLEFT  TEXP FEXP)
    (IFRIGHT TEXP FEXP)
    //TEXP and FEXP are other expressions

    (MOVE DIR)
    //Dir is the dir [0,1,2,3] = [u, r, d, l] to move
  ```

  Every iteration of the simulation, an expression composed of these
  atomic operations will run.  Eventually, a `MOVE` command will be
  executed, and the unit will update.

  I'm not totally sure that this is the strictest way to do this, but its
  working for now.

  ```lisp
  Example Expression (Randomly Generated Full Tree of Maxdepth 4):

  (IFUP (IFUP (IFUP (IFDOWN (MOVE 2) (MOVE 2)) (IFUP (MOVE 1) (MOVE 2))) (IFLEFT
  (IFDOWN (MOVE 1) (MOVE 1)) (IFUP (MOVE 1) (MOVE 2)))) (IFDOWN (IFLEFT (IFRIGHT
  (MOVE 1) (MOVE 2)) (IFLEFT (MOVE 3) (MOVE 3))) (IFDOWN (IFLEFT (MOVE 3) (MOVE 0))
  (IFDOWN (MOVE 2) (MOVE 2)))))
  ```

### S-Expressions in Java <a id="en_java"></a>

  Now we need a way to store the trees of expressions in java, so they
  can be manipulated by the GP operations.  An interface `Expression` is created that defines an `eval` method.

  ```java
  int eval( int[] state ){ ... }
  ```
  As mentioned before, the unit can see the four adjacent cells.  An
  array representing these cell's values are passed to the expression
  on each update.

  Five java classes implement the `Expression` interface. Four for the conditionals, and one for the move expression.  

  The four conditional `Expressions` store references to their true and
  false branch `Expressions`.  These are then followed based on the logic
  of the expression.  In general, they look like:

  ```java
  public class IFDOWN implements Expression {
    Expression truebranch;
    Expression falsebranch;

    //...

    //state convention [UP, RIGHT, DOWN, LEFT]
    public int eval( int[] state ){
      if( state[ 2 ] == 1 ){
        return truebranch.eval( state );
      } else {
        return falsebranch.eval( state );
      }
    }

    //...
  }  
  ```

  The `MOVE` class also implements the `Expression` interface, but
  does not track a reference to any other expressions.  When it is
  evaluated, all it does is return its direction.  This shows how the move
  function behaves as a terminal.  It returns a value up the stack of
  calls instead of evaluating a new expression.

  ```java
  public class MOVE implements Expression {
    int dir;
    //...
    public int eval( int[] state ){
      return dir;
    }
    //...
  }
  ```

  To evaluate an Individuals expression, we simply call eval() on its root, passing
  along the state.  This occurs in the `GridSimulation` class, which contains
  the logic specific to the simulation of the unit.  Below we see an overview
  of the state being passed to the unit during evaluation.

  ```java
  //from the Individual class
  public int evaluate( int[] state ){
    //Evaluates the given expression using the state provided.
    return root.eval(state);
  }
  ```

  ```java
  //from the GridSimulation class - while updating a unit within the simulation
  int[] state;
  state = getState();
  switch( i.evaluate(state) ){
    //Move the Unit
  }
  ```

  In addition to eval, the `Expression` interface defines a <pre>print()</pre> method.  This is
  basically the same in all function classes, recursivly printing the expression
  and its parameters.  For the terminal function, just a string representing the
  move is returned.

## Genetic Programming! <a id="gp"></a>

  Now that we have a structure, and a representation of it in code, we can begin to
  look at the actual operations performed by the Genetic Programming Paradigm.

  I'll break this apart into three sections:
    1) Initializing the population
    2) Evaluating the population  
    3) Selecting the population

### Initializing <a id="gp_init"></a>

  The population undergoing adaptation is just a list of objects, each one
  holding a reference to an `Expression` and some value representing the
  fitness of the individual.  In the current implementation, multiple fields
  are tracked for an `Individual` that all represent the fitness after some
  transformation.  These have their uses and will be explored later.  For now
  just know an individual tracks its expression root, and its fitness.

  To create the initial set of these individuals, we need a way to generate
  s-expressions.  Enter the `ExpressionBuilder` class.  This object contains
  methods that return s-expressions using either the `Full` or `Grow` methods of
  tree building.

  Both methods operate by recursively adding new nodes to a tree.  At each
  step the expression added to the tree is selected randomly from either
  the set of function expressions, or terminal expressions.

  In the `Full` method, as long as the current depth is less than the max depth,
  the expression selected will always come from the set of function expressions.
  Once the depth hits the max depth, all expressions are selected from the
  terminal set.  This builds full trees (in our case, full binary trees) of
  expressions, thus giving the method its name.

  The `Grow` method creates a new random tree in a similar manner to Full, but
  when at a depth less than the max depth, the expression may be selected from
  either the function or terminal sets.  This leads to trees that may be full,
  but on average will not be.

  ```java
  int depth = 3;
  ExpressionBuilder eb = new ExpressionBuilder();
  Expression e_full = eb.getFullExpression( depth );
  Expression e_grow = eb.getGrowExpression( depth );

  Individual i = new Individual( e_full );
  ```

  A good practice is to start with a population with variety in its depth, and
  'fullness'.  To accomplish this, a method known as `Ramped Half and Half` is
  implemented.

  In `Ramped Half and Half` we divide the population into equally sized segments.
  Each segment represents a depth, starting at 2, and incrementing till the
  max initial depth.  For each of these, half of the individuals are instantiated
  with expressions using the `Full` method, and have with the `Grow` method, with
  a depth equal to that of the segment depth.

  This gives a good variety in the initial population.  This is an asset in GP, as
  every individual, regardless of fitness or size, has a chance to contain
  valuable information about the search space.  Too little variety at the
  start of a GP run can hinder the search of solutions, as a poor local optimum
  will quickly over take the stagnating population.

### Evaluating <a id="gp_eval"></a>

  The simulation itself.  Choosing parameters.  Grid behavior on outofbounds.

### Selecting <a id="gp_select"></a>

  Meat of GP.  Fitness proportional selection.  Reproduction, crossover, mutation.-