# UnitGP

![alt text](http://imgur.com/vEVqXSd.png "Grid and Expressions")

Evolving the behavior of a simple food-seeking unit on a grid with
[genetic programming](https://en.wikipedia.org/wiki/Genetic_programming).

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

### Implementation

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

  A rooted s-expression can be built using the `ExpressionBuilder` class.  This
  can return the root of an s-expression, generated using the 'Full' or
  'Grow' methods of tree generation as defined in Koza Chp-6.

### Printing

  In addition to eval, the `Expression` interface defines a <pre>print()</pre> method.  This is
  basically the same in all function classes, recursivly printing the expression
  and its parameters.  For the terminal function, just a string representing the
  move is returned.

## Drawing the sim

  The simulation is represented graphically with `java.awt` and `javax.swing`
  frames, panels, and graphics.  

