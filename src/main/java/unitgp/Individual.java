package unitgp;
import expression.*;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Individual implements Comparable {
Expression root;
public int fitness;
public int standardizedFitness;
public int weightedFitness;

public int depth;

private static Random rand = new Random();

static Logger logger = LogManager.getLogger( Individual.class.getName() );

public static void crossover( Individual p1, Individual p2 ){
	//Exchange random nodes in the two Individuals expressions.
	logger.debug( "Crossing over ----------: " );
	logger.debug( "p1: "+p1.print() );
	logger.debug( "p2: "+p2.print() );
	int maxdepth = p1.getDepth();
	boolean hit = false;
	Expression prev_a = null;
	Expression a = p1.root;
	boolean a_branch = true;

	int depth = 1;
	if( !a.terminal() ) {
		while( !(hit || a.terminal()) ) {
			//See if we get a hit
			if( rand.nextFloat() < 1.0f/(float)depth ) {
				//If we do, we use this as our node.
				hit = true;
			} else {
				Expression t = a;
				if( rand.nextFloat() > 0.5f ) {
					a = prev_a.truebranch;
					a_branch = true;
				} else {
					a = prev_a.falsebranch;
					a_branch = false;
				}
				prev_a = t;
				depth++;
			}
		}
	}
	maxdepth = p2.getDepth();
	hit = false;
	Expression prev_b = null;
	Expression b = p2.root;
	boolean b_branch = true;
	depth = 2;

	if( !b.terminal() ) {
		while( !(hit || b.terminal()) ) {
			//See if we get a hit
			if( rand.nextFloat() < 1.0f/(float)depth ) {
				//If we do, we use this as our node.
				hit = true;
			} else {
				prev_b = b;
				if( rand.nextFloat() > 0.5f ) {
					b = b.truebranch;
					b_branch = true;
				} else {
					b = b.falsebranch;
					b_branch = false;
				}
				depth++;
			}
		}
	}

	if( (prev_a != null) && (prev_b != null) ) {
		//Swap the two nodes by reassinging the pointers in the expressions
		if( a_branch ) {
			prev_a.truebranch = b;
		} else {
			prev_a.falsebranch = b;
		}
		if( b_branch ) {
			prev_b.truebranch = a;
		} else {
			prev_b.falsebranch = a;
		}
	}  else {
		if( (prev_a == null) && (prev_b == null) ) {
			//Just do nothing.
		} else if( prev_a == null ) {
			//Swapping out a's terminal root.
			if( b_branch ) {
				prev_b.truebranch = p1.root;
			} else {
				prev_b.falsebranch = p1.root;
			}
			p1.root = b;
		} else {
			//Swapping out b's terminal root.
			if( a_branch ) {
				prev_a.truebranch = p2.root;
			} else {
				prev_a.falsebranch = p2.root;
			}
			p2.root = a;
		}
	}
}
public Individual( Expression r ){
	root = r;
}

public void applyMutation( float p ){
	root = root.mutate( p );
}

public String print(){
	return root.print();
}

public int evaluate( int[] state ){
	//Evaluates the given expression using the state provided.
	return root.eval(state);
}

//Return the depth of the root expression
public int getDepth(){
	return root.getDepth();
}

/**
 * Returns a deep copy of the individual and its expression tree.
 *
 * @return Individual the deep copy of the Individual
 */
public Individual copy(){
	Individual ret = new Individual( null );
	ret.root = this.root.copy();
	ret.fitness = this.fitness;
	ret.standardizedFitness = this.standardizedFitness;
	ret.weightedFitness = this.weightedFitness;
	return ret;
}

@Override
public int compareTo(Object o) {
	Individual p = (Individual) o;
	int res = (new Integer(weightedFitness)).compareTo(p.weightedFitness);
	int ret;
	if( res > 0 ) {
		ret = -1;
	} else if ( res < 0 ) {
		ret = 1;
	} else {
		ret = 0;
	}
	return ret;
}
}
