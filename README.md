# UnitGP

![alt text](http://imgur.com/vEVqXSd.png "Grid and Expressions")

Evolving the behavior of a simple food-seeking unit on a grid with
[genetic programming](https://en.wikipedia.org/wiki/Genetic_programming).

___

## ToC

+ [Overview](#overview)

## Overview <a id="overview"></a>

Educational implementation of the Genetic Programming Paradigm described
in [Koza 92' Genetic Programming](http://www.amazon.com/exec/obidos/ASIN/0262111705/geneticprogrammi).

The structure undergoing adaptation will be simple s-expressions, stored
as rooted trees.

The resulting s-expression tree will be used to decide the behavior of
a unit on a grid containing food. Eating food will keep the unit alive, while
every turn the unit loses health.

By defining a set of s-expressions that interact with the unit and read
state from the neighbors of the unit, we can then build nested s-expressions which
will alter the state of the unit (move it) based on the state of the grid they
are shown.  

By allowing food to replenish the health of the unit, we can use the age of the
unit as its fitness.  Then longer an individual stays alive, the more its
'genome' will contribute to the subsequent population of individuals.

Hopefully, an optimal strategy for gathering food is found.

## Encoding The S-Expressions

  For us to grow the behavior of the unit, we need to be able to express its
  possible actions and the state it can view as a structure that can be
  manipulated by the genetic programming operations.

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

### Implementing the S-Expressions in Java

  Now we need a way to store the trees of expressions in java, so they
  can be manipulated by the GP operations.  An interface `Expression` is created that defines an `eval` method.

  ```java
  int eval( int[] state ){ ... }
  ```
  As mentioned before, the unit can see the four adjacent cells.  An
  array representing these cell's values are passed to the expression
  on each update.

  Five java classes implement the `Expression` interface. Four for the conditionals, and one for the move expression.  

  The four conditional `Expressions` store references to their `true` and
  `false` branch expressions.  When we evaluate an Individuals expression,
  we simply call evaluate on the root 'node', which then calls the eval method of the correct branch expression.  This ultimately finishes executing when a branch ends with a `MOVE` expression.

  The `MOVE` expression class implements the `Expression` interface, but
  does not track a reference to any other expressions.  When it is
  evaluated, all it does is return its direction.

### Printing

  In addition to eval, the `Expression` interface defines a <pre>print()</pre> method.  This is
  basically the same in all function classes, recursivly printing the expression
  and its parameters.  For the terminal function, just a string representing the
  move is returned.

## Genetic Programming!

  Now that we have a structure, and a representation of it in code, we can begin to
  look at the actual operations performed by the Genetic Programming Paradigm.

  I'll break this apart into three sections:
    1) Initializing the population
    2) Evaluating the population  
    3) Selecting the population

### Initializing

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

### Evaluating
