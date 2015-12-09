# UnitGP

Evolve the behavior of a simulated unit on a grid.

Educational implementation of the Genetic Programming Paradigm described
in "Koza 92' Genetic Programming".

The structure undergoing adaptation will be simple s-expressions, stored
as rooted trees.

The function set will be
  IFGZ - If Greater Than 0
       - Accepts a terminal
       - executes left child if greater than 0
       - executes right child otherwise
The terminal set will be
  m_l, m_r, m_u, m_d - Move commands, 1 for each cardinal direction
  s_l, s_r, s_u, s_d - Neighbor state, 1 for each cardinal direction

The resulting s-expression tree will be used to decide the behaviour of
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
  a helper class, the <Simulation>, to actually perform a simulation of the
  unit given an <Expression>.

## Encoding The S-Expressions

  //TODO

## Simulating The Unit

  //TODO