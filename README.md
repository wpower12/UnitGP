# UnitGP

![alt text](http://i.imgur.com/MXNY51B.png "Grid and Expressions")

Evolving the behavior of a simple food-seeking unit on a grid with
[genetic programming](https://en.wikipedia.org/wiki/Genetic_programming).

___

* [Running](#running)
* [Overview](#overview)
  * [TODO](#o_td)
  * [What Is GP?](#o_gp)
    * [Fitness](#o_gp_fit)
    * [Genetic Operations](#o_gp_ops)
  * [Simulation](#o_sim)
* [Encoding Behavior](#encode)
  * [Choosing Expressions](#en_chose)
  * [Expressions in Java](#en_java)
* [Genetic Programming](#gp)
  * [Initializing](#gp_init)
  * [Evaluating](#gp_eval)
  * [Selecting](#gp_select)
* [Results](#res)
  * [Expected Behavior](#res_expected)
  * [Interesting Results](#res_ints)
  * [Reducing Solutions](#res_solns)
* [References](#ref)

____

<a id="running"></a>
## Running

Requires gradle to run.  Clone this repo and then enter `gradle run`.  A GP run
will process, and the most fit individual will be simulated in a panel.

<a id="overview"></a>
## Overview

UnitGP is my (poorly named) educational project. It consists of a simulation
and a genetic programming module that generates functions for use as the brains
of an 'insect' being modeled in the simulation.

The simulation is an approximation of an insect looking for food.  Placed on a
grid, this simple bug must find food or slowly starve.  Each tick of the sim,
the insect can decide to move to a neighboring cell.  To help make this
decision, the insect is able to see what is in those four cells.

The decision process of the ant could be thought of as a simple chain of
 decisions.  If we see something in one direction, we could either
move that direction, or perhaps go down another path, and check some other direction.

Such simple, binary chains of conditionals can be represented as a tree.  We
start at the root, evaluate some state, and then choose between two paths.

For example, we could have the following tree.  Starting at the root, the unit
evaluates the condition, and chooses a branch.  Each of these branches can be
either a new condition to evaluate, or a movement.  

<p align="center">
<img align="center" src="http://i.imgur.com/kkA3kLZ.png" alt="Basic Tree" >
</p>

This project aims to find an optimal tree, that efficiently finds food in the
simulation, by mimicking the process of evolution through natural selection.
Genetic programming evolves possible solutions by treating the decision trees
as genomes. We can select and recombine these trees to generate new, possible
solutions.    

<a id="o_td"></a>
### TODO

  - Max depth in crossover
  - Generation proportional mutation
  - Abstract Function and Terminal sets.
  - Torodial Grid

<a id="o_gp"></a>
### What Is Genetic Programming?

Genetic programming is a distinct method of generating a procedure.  Normally, a
procedure is hand crafted to solve a problem.  We write software to meet a
requirement, we implement a specific algorithm to accomplish a set task.  
Typically, these things are done in a direct manner.  The problem is reasoned
about, and the procedure is crafted and tested until it does what it needs.

Instead, Genetic Programming  generates a procedure by imitating the process of
natural selection.  A population of individuals is created, each one representing
an attempt at solving the problem.  Each individual would be some possible
procedure.  

These individuals are then evaluated.  Their procedures are used in the context
of their problem, and assigned some fitness value.  The fitness is then used
as a core parameter in selecting a new generation of individuals.  Each iteration,
hopefully, this new population will get closer and closer to containing an optimal
solution.

#### Fitness <a id="o_gp_fit"></a>

It is important to select a measure of fitness that is gradual and positive.  
Gradual implies that a small change in behavior should lead to a small change
in fitness.  Positive implies that a 'good' individal should have a larger
fitness than a 'bad' individual.

Choosing a measure of fitness that meets these two constraints is difficult.

In my simulation, the fitness of an individal is the total number of food found.
The presence of the random function, and the method by which food is place introduce
some variability in the fitness, but this is smoothed out by taking an average
of the food gathered over a set number of runs.  

#### Genetic Operations <a id="o_gp_ops"></a>

  Having a measure of fitness enables us to evaluate individuals for selection.
  Selection is the process by which a new population of individuals is built.  

  Two main processes are used to do this; reproduction and crossover.

  **Reproduction**

  Reproduction is the selection of some individuals to be directly represented in
  the next generation.  These individuals are copied, usually directly, but
  occasionally with some small mutation in their 'genome'.

  This provides some pressure for the best individuals to always contribute to
  the next generation.  However, always selecting the top individuals may lead to
  stagnation.  To combat this possible stagnation, a process of fitness proportional
  selection is used.  The chance for an individual to reproduce, or be copied
  into the next generation is random, but weighted by its fitness.  

  Another name for this is 'Roulette Wheel' selection.  We can imagine the
  individuals in a population all being an area on a roulette wheel, the area
  being proportional to their fitness.  Selecting an individual is a matter of spinning the wheel.

  <p align="center">
  <img align="center" src="http://i.imgur.com/th1MJtk.png" alt="Wheel Example" >
  </p>

  It may seem like a small detail, but using proportional selection instead of
  always choosing from the best individuals is a core element of genetic
  algorithms.  Without it, populations can stagnate early on, settling on some
  good, but not optimal solution.  In the early stages of 'evolution', variety
  is the biggest asset, even if that means occasionally selecting a poor
  individual over a fit one.

  Koza, and Coley both discuss this.  Chapters K# and C# detail the
  mathematical importance of fitness proportional selection on the survival
  of good schemas within a population.  


  **Crossover**

  The second method of selection is crossover.  In this, two parent individuals
  are selected (with fitness proportional selection) and used as source material
  for two new individuals.  A cut node is selected at random in each parent, and
  swapped between the two, creating two new trees.  These two new individuals are
  added to the population.

  <p align="center">
  <img align="center" src="http://i.imgur.com/qKkd4pl.png" alt="Crossover Example" >
  </p>

  There are many other genetic operations, all with specific goals for adding
  complexity, variety, or control to a GP run.  One major operation is mutation.

  **Mutation**

  Mutation acts on a tree by traversing it (visiting every node) and at every node
  checking a random number against a probability of mutation.  This value is
  usually set to be very low, on the order of tenths of a percent.  When a node
  hits this random chance, it 'mutates'.

  The method of mutation can vary, but the simplest form is to just replace that
  node in the tree with another random possible function or terminal.  

  TODO - Example

  There are a huge variety of more complex mutation operations.  Some switch
  the child branches, some replace functions with only terminals.  The simplicity
  of the functions chosen in this project limit the variety of mutation operations
  available.  


## Implementation <a id="imp"></a>

The Java implementation centers around Expressions.  This base class represents
a nominal expression in a tree.  For this project, I chose to keep all possible
functions as binary functions returning integers.  This is a pretty huge simplification
but one of the amazing properties of GP is its ability to synthesize complexity
from simple elements.  

I'll quickly outline the main packages, and what their classes are doing to
expressions.  I think this provides a nice outline of the program.

**expression Package**

Contains the base Expression class, and the various classes representing the
functions.  These classes represent the nodes in the trees.  

**simulation Package**

Contains the actual simulation logic.  Responsible for taking an Indivdual,
and evaluating its root Expression to determine a fitness.

**unitgp Package**

The main UnitGP class contains the logic for the actual GP operation, responsible
for building an initial population of individuals/expressions.  The ExpressionBuilder
class is responsbile for returning new expression trees.


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

### Expressions in Java <a id="imp_sexp"></a>

  These nine expressions are implemented in three classes, each extending the
  base expression class.

  ```java
  public class Expression {
    public Expression truebranch;
    public Expression falsebranch;

    /* ... */
    //All the base classes @Overwrite these methods.  
    public int eval( int[] state ){ ... }
    public String print(){ ... }
  }

  public class IFDIR extends Expression {
    private int direction;
    /* ... */
    public IFDIR(d){ direction = d }; //This covers the 4 If-Expressions
  }

  public class MOVE  extends Expression {
    private int direction;
    /* ... */
    public MOVE(d){ direction = d }; //This covers the 4 Move-Expressions
  }

  public class RAND extends Expression { ... }  

  ```

  The four if-expressions are implemented with the `IFDIR` class.  A direction
  field is set that determines what neighbor the method is checking.  This
  value is set by the `ExpressionBuilder` when a tree is first built, and can be
  changed by the mutation operation during selection.

  The four move-expressions are implemented in a similar manner with the `MOVE`
  class.  

  Finally, the `RAND` class exists mirrors the above, but with no direction field.

  All three classes maintain fields that point to child expressions.  The `IFDIR`
  and `RAND` classes will have non-null pointers.  The `MOVE` class will have null links.
  This makes sense, given that they are the terminal expressions.

  The `eval( int[] state)` method works as a traversal of the expression tree.
  Each class implements this eval method.  Upon calling it, the class then
  evaluates a child branch (RAND, IFDIR) or simply returns a value (MOVE).

  Once a set of classes is instantiated and linked, the `Individual` class just
  needs to hold a pointer to the root of that linked tree.  

  Then to evaluate, we simply call eval() on its root, passing
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

  In addition to `eval()`, the `Expression` interface defines a `print()` method.  This is
  basically the same in all function classes, recursivly printing the expression
  and its parameters.

### Genetic Programming! <a id="gp"></a>

  Now that we have a structure, and a representation of it in code, we can begin to
  look at implementing the actual operations performed in the Genetic Programming paradigm.

  I'll break this apart into three sections:
    1. Initializing the population
    2. Evaluating the population  
    3. Selecting the population

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

  **Full Method**

  Both methods build a tree recursively.  A depth parameter is passed along at
  each method call, reduced by one each time.  For the `Full` method, so long
  as this depth is above 0, we always select a `Function` node, or rather, we
  either add a `IFDIR` class, or a `RAND` class.  Once depth hits 0, we add a
  `Terminal` node, in our case, an instance of the `MOVE` class.  

  Before returning a `Function` node, we must also generate trees representing
  the children.  This is where the recursive method invocation occurs.  The
  `full_re( d )` method is called, with a decreased depth passed in.  

  ```java
  private Expression full_re( int d ){
    Expression ret;
    if( d > 0 ){
      Expression t = full_re(d-1);
      Expression f = full_re(d-1);
      switch( rand.nextInt(2) ){
        case 0:
          ret = new IFDIR( rand.nextInt(4), t, f );
          break;
        case 1:
        default:
          ret = new RAND( t, f );
          break;
      }
    } else {
      ret = new MOVE( rand.nextInt(4) );
    }
    return ret;
  }
  ```

  **Grow Method**

  The grow method is implemented in a similar manner, however instead of always
  returning a `Function` node for depths greater than 0, the method may also
  select a `MOVE` node.

  ```java
  private Expression grow_re( int d ){
    Expression ret;
    if( d > 0 ){
      if( rand.nextFloat() > 0.1f ){
        Expression t = full_re(d-1);
        Expression f = full_re(d-1);
        switch( rand.nextInt(2) ){
          case 0:
            ret = new IFDIR( rand.nextInt(4), t, f );
            break;
          case 1:
          default:
            ret = new RAND( t, f );
            break;
        }
      } else {
        ret = new MOVE( rand.nextInt(4) );
      }
    } else {
      ret = new MOVE( rand.nextInt(4) );
    }
    return ret;
  }
  ```
### Evaluating <a id="gp_eval"></a>

  The simulation is contained in the `GridSimulation` class.  This contains
the logic for simulating an individual, and evaluating its fitness.  

The world of the ant is represnted in code as a integer array.  Values of 0,1,2
represent empty space, food, and the insect respectivly.  Each tick of the simulation
represents the unit moving on this grid.  When updating its position, the unit
can choose one of four directions to move, each one corresponding to the functions
mentioned before.

During the update tick, the expression being evaluated will need to look at the
state of the world around the unit.  To do this, a vector of values is built,
and passed to the expression tree being evaluated.  

The graphic resprsentation of the simulation is done with a JPanel.  The
`GridPanel` class simply repaints the panel with the new state of the world
every update loop.  

### Selecting <a id="gp_select"></a>

**Fitness Proportional Selection**

To implement fitness proportional selection, I somewhat abused the sortability
of a Comparable class.  The `Indivdual` class extends `Comparable`.  Doing this
requires providing a `compareTo()` method.  Each indivudals weightedFitness
is used as the comparison value.

Since we track the collection in an ArrayList object we can easily use the
Collections sort method on our population.  The compareTo method just needs to
be written such that a higher weightedFitness precedes a lower one.  

The indivudals track fitness and weightedFitness seperatly.  Doing this allows
us to apply a random weight to the fitness, and store it for sorting.  The combination
of the weighting function, and the sort provides an easy implementation of FPS.

**Selection**

Reproduction happens in two steps, the first is selection.  In this, a set
percentage of the new population is filled by using FPS to choose old individuals
to copy.  The copying is a deep copy.  All expression classes implement a `copy()`
method that recursivly return copies of themselves and their branches.  

**Crossover**

Crossover was, by far, the hardest operation to implement.  Cleaning it up is a
huge next step.

Crossover produces new individuals in pairs.  FPS is used to select a fresh pair
of parents.  Two traversals happen, one for each parent tree.  During this
traversal, a random die is rolled before continuing the traversal.  If a small
threshold is hit, then the node is selected as the 'cut node' for crossover.

This threshold needs to depend on the current depth in the tree.  By having a
small chance to select the node at lower depths, and a higher chance towards
the bottom, we somewhat mimic resovour sampling.  This better distributes the
cuts about the trees.  

When a node doesn't get selected, the traversal continues.  One of the two
branches is selected at random, pointers to a parent and current expression
are updated, and the loop goes on.

Once two cut nodes are found, the pointers for the parents and the cut nodes
are swapped.  Some flags are tracked that remember which branch of the parent
node should be replaced.  Also, a check for a null parent is done to account
for the root being selected as the cut node.  

**Mutation**

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

### Interesting Results <a id="res_inds"></a>
Initially, there was a bug in my expression.copy() method that prevented the
correct sub trees from being copied over during selection.  While this had a huge
negative impact on the resulting individuals, the simulation still managed to
find some kind of local optimum.  Populations of "line walkers" quickly converged.
Each one was always some expression that reduced to just moving in a single
at each timestep.

Once the copy bug was fixed, a larger variety of individuals evolved.  

### Reducing Solutions <a id="res_solns"></a>

For now, the optimal member of the population has behavior one would expect,
when near a piece of food, it moves to it, when none are to be found, a
random direction is selected.  What is interesting is that this behavior can
be encoded in a variety of trees, all varying in complexity, but simplifying
to the same paradigm of "Move to food, else wander".


## References <a id="ref"></a>

* [Koza-92](https://mitpress.mit.edu/books/genetic-programming) - Koza, John. "Genetic Programming: On the Programming of Computers by Means of Natural Selection". MIT Press, 1992.
