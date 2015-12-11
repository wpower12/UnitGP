# UnitGP

Evolve the behavior of a simulated unit on a grid.

Educational implementation of the Genetic Programming Paradigm described
in "Koza 92' Genetic Programming".

The structure undergoing adaptation will be simple s-expressions, stored
as rooted trees.

The resulting s-expression tree will be used to decide the behavior of
a <Unit> on a grid.  The grid will contain integer values, with a 0 being
empty space, and a 1 being 'food'.  The commands listed in the terminal
set will move the unit to the corresponding cell.

The unit will have a health value.  This is an integer value that starts
at some positive value.  Each time step, it reduces by 1.  Eating food
increases the health value by some set amount.

When a unit moves into a cell with a 1, it is replaced by 0, or eaten.

The fitness of a given s-expression tree is the number of generations the
cell stays alive.

## Procedure

  The main class, UnitGP performs the actual operation of the GPP.  It uses
  a helper class, the Simulation, to actually perform a simulation of an
  Individual, containing its rooted tree of Expressions.

  The s-expressions are rooted trees where the parameters to any functions
  are either Functions or Terminals.  

## Encoding The S-Expressions

  I'm not totally sure that this is the strictest way to do this, but my
  s-expressions are encoded in the following way.

### Functions

  Four IF functions are used to look at the 4 neighbors of the unit.

  <pre>
  IFDOWN, IFUP, IFRIGHT, IFLEFT

  ex:

  (IFDOWN a b)
  // a and b are either Terminals or Functions
  </pre>


### Terminals

  The program uses a single terminal, with one varying parameter:

  <pre>
  (MOVE a)  //a is in {0,1,2,3} an enum for the 4 directions {u, r, d, l}
  (IFUP (MOVE 2) (MOVE 1))
  </pre>

### Implementation

  To implement the above, an interface Expression is created that defines
  a eval method.

  int eval( int[] state );

  The state represents the current values in the neighbor cells. , [u, r, d, l]

  Then, the 4 function expressions are created, implementing this eval method.
  Each of the four function expressions contains references to its two branch
  expressions.  Upon calling eval, they check the state, and call the appropriate
  branch Expression's eval method.  

  Method execution terminates when the chain of calls reaches a Terminal expression.
  The terminal expression also implements the eval method.  

  The implementation of the MOVE command returns a value equal to its
  parameter.  We interpret this later as a movement to one of the 4 neighbor cells.

  With these classes in place, a rooted s-expression can be built by using the
  ExpressionBuilder.  This returns the root of an s-expression.

  Calling eval( state ) on the root will begin the traversal of the tree, carrying
  the state along with it so conditionals can be evaluated.

### Printing

  In addition to eval, the Expression interface defines a print method.  This is
  basically the same in all function classes, recursivly printing the expression
  and its parameters.  For the terminal function, just a string representing the
  move is returned.

