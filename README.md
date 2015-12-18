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
+ [Results](#res)
  * [Expected Behavior](#res_expected)
  * [Interesting Solutions](#res_solns)
+ [References](#ref)

## Overview <a id="overview"></a>

This is a personal project, the purpose of which is to learn about a concept
known as Genetic Programming.  UnitGP consists of a simulation, a genetic programming
module, and a set of functions and terminals, representing the simulation.

The UnitGP simulation is that of a simple unit on a grid.  Similar to the ants
that make up some of the first explanatory implementations of genetic programming.
All the unit can do is move in the 4 cardinal directions.  All the unit can see
are the 4 cells adjacent to it.  A unit starts with a certain amount of health,
and dies when it reaches 0.  By moving to a cell with food in it, the unit gains
a small amount of health, and the food is removed.

The behavior of the unit, whether it moves up, down, left, or right could be thought
of as a simple chain of decisions.  If we see something in one direction, we could either
move that direction, or perhaps go down another path, and check some other direction.

Such simple, binary chains of conditionals can be represented as a tree.  We
start at the root, evaluate some state, and then choose between two paths.

For example, we could have the following tree:

TODO - SHOW A TREE FORM OF AN Expression

Which the unit would process at each time step, to decide on its action.  

These trees are the core structure that we evolve with the Genetic Programming
methods.  A random initial population of these trees is created, and through
generations of random crossover, reproduction, and evaluation an 'optimal' tree,
or rather, optimal behavior can be found.

This process attempts to mimic the selection found in nature.  Evolution by
natural selection is a search of an almost infinite problem space, with each
set of genetic code an attempt at a more fit individual.  By creating approximations
of these processes, the problem space of possible trees is explored in leaps via
mutation, and via hill climbing with fitness-proportional selection.

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

  With behavior set, we now need a method by which to evaluate the success of a
  given tree.  

  The simulation involves gather food, so some initial attempts at a fitness
  function could be as simple as the total number of food cells found in a given
  run, or the total number of generations survived.

  Regardless, it is important to select a measure of fitness that is a positive,
  monotonic function, and that reacts smoothly to changes in the individual.

  With the current set up of the simulation, meeting these requirements is difficult.
  Using generation directly as a fitness leads to a stagnation problem.  Individuals
  with a basic, and simple tree that do just better than the minimum will quickly
  overwhelm the population.  The number of generations survived jumps very quickly,
  not smoothly.  Also, due to the random nature of the placement of food, fitness
  becomes very noisy.

  Fixing this is a big TODO.

#### Genetic Operations <a id="o_gp_ops"></a>

  Having a measure of fitness enables us to evaluate individuals for selection.
  Selection is the process by which a new population of individuals is built.  

  Two main processes are used to do this; reproduction and crossover.

  Reproduction is the selection of some individuals to be directly represented in
  the next generation.  These individuals are copied, usually directly, but
  occasionally with some small mutation in their 'genome'.

  This provides some pressure for the best individuals to always contribute to
  the next generation.  However, always selecting the top individuals may lead to
  stagnation.  To combat this possible stagnation, a process of fitness proportional
  selection is used.  The chance for an individual to reproduce, or be copied
  into the next generation is random, but weighted by its fitness.  

  This variation is a huge asset to the emergence of an optimal solution, and is
  a core assumption when deriving mathematical representations of the processes
  of GP.

  The second method of selection is crossover.  In this, two parent individuals
  are selected (with fitness proportional selection) and used as source material
  for two new individuals.  A cut node is selected at random in each parent, and
  swapped between the two, creating two new trees.  These two new individuals are
  added to the population.

## Implementation <a id="imp"></a>

  Now it comes time to actually encode the context of the simulation into some
  atomic expressions.  A best practice is to keep the number of atomic Operations
  small and simple.  

### Choosing Expressions <a id="imp_choose"></a>

A total of 9 expressions are encoded by the UnitGP classes.  Their implementation
described later.  The functions take Expressions as parameters, which may be
nested expressions, or terminals.  The terminals are expressions that end the
execution of the procedure, they simply return a result.  This set can be
considered closed if we assume all expressions return a value representing a
direction.  All functions evaluate either to a terminal representing a value, or
to another expression that in turn should evaluate to a terminal.

```lisp
  //Functions
  (IFUP E_TRUE E_FALSE)
  (IFRIGHT E_TRUE E_FALSE)
  (IFDOWN E_TRUE E_FALSE)
  (IFLEFT E_TRUE E_FALSE)
  (RAND E_LEFT, E_RIGHT)
  //Terminals
  (MOVEUP)
  (MOVERIGHT)
  (MOVEDOWN)
  (MOVELEFT)
```

### S-Expressions in Java <a id="imp_sexp"></a>

  TODO - write about the Expression classes.

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

### Genetic Programming! <a id="gp"></a>

  Now that we have a structure, and a representation of it in code, we can begin to
  look at the actual operations performed by the Genetic Programming Paradigm.

  I'll break this apart into three sections:
    * 1 Initializing the population
    * 2 Evaluating the population  
    * 3 Selecting the population

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

  TODO - Implementing the simulation itself.  Choosing parameters.  Grid behavior on outofbounds.

### Selecting <a id="gp_select"></a>

  Meat of GP.  Implementing Fitness proportional selection, reproduction, crossover, mutation.-

## Results <a id="res"></a>

### Expected Behavior <a id="res_expected"></a>

I had imagined that there is some optimum solution, atleast one that would behave
very well.  Take a decision tree like the following:

```lisp
(IFUP MOVEUP
  (IFRIGHT MOVERIGHT
    (IFDOWN MOVEDOWN
      (IFLEFT MOVELEFT
        (RAND MOVEUP MOVERIGHT)))))
```
Always moving to available food, and then moving either up or right.  This seems
like it should easily outperform any random walk.  

### Interesting Results <a id="res_solns"></a>
So far no luck. The GP runs are converging on very simple behavior.  It seems that many
possible trees do very bad, generally scoring as low as possible in the simulation.
These are trees that result in very bad meandering, with lots of backtracking.

Quickly, any solution that atleast moves in some consistant direction overwhelm
the population.  Solutions that simplfy to moving in one direction, or very small
trees that are biases in one axes are quick to succeed.  This quick jump might
be dealt with by introducing actual node mutation.  

As for now, the population has stagnated to 'Line Walkers'.  Very few trees that
include a RAND node survive.

I hope that the addition of mutation yields better results.  It would seem
possible that a unit could evolve with maximum fitness, one that collects if not
all food, a large selection of it.
