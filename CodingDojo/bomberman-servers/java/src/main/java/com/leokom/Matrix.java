package com.leokom;

import static com.leokom.Direction.*;
import java.util.ArrayList;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Matrix {
	public static final int ROWS = 30;
	public static final int COLUMNS = 30;
	
	private Position my;
	final Objects[] [] matrix = new Objects[ ROWS ][ COLUMNS ];
	
	private List< Position > enemies = new ArrayList<Position>();
	private List< Position > diabchiks = new ArrayList<Position>();
	private List< Position > wallsToBomb = new ArrayList<Position>();
	
	public Matrix( String board ) {
		for ( int row = 0; row < ROWS; row++ ) {
    		for ( int column = 0; column < COLUMNS; column++ ) {
    			Objects object = Objects.forChar( board.charAt( row * COLUMNS + column ) );
    			
    			
    			Position currentPosition = new Position( row, column );
    			
				if ( object == Objects.BOMBERMAN || object == Objects.BOMB_BOMBERMAN ) {
    				System.out.println( "My position: " + row + ":" + column );
    				this.my = currentPosition;
    			}
    			
    			if ( object == Objects.OTHER_BOMBERMAN || object == Objects.OTHER_BOMB_BOMBERMAN ) {
    				enemies.add( currentPosition );
    			}
    			
    			if ( object == Objects.MEAT_CHOPPER ) {
    				diabchiks.add( currentPosition );
    			}
    			
    			if ( object == Objects.DESTROY_WALL ) {
    				wallsToBomb.add( currentPosition );
    			}
    			
				matrix[ row ][ column ] = object;
    		}
    	}
	}
	
	public Position current() {
		return my;
	}
	
	Objects getLeft() {
		return getObject( my != null ? my.left() : null );
	}
	
	Objects getRight() {
		return getObject( my != null ? my.right() : null );
	}
	
	Objects getTop() {
		return getObject( my != null ? my.top() : null );
	}
	
	Objects getBottom() {
		return getObject( my != null ? my.bottom() : null );
	}
	
	Objects get( Direction direction ) {
		
		switch ( direction ) {
		case LEFT:
			return getLeft();
		case RIGHT:
			return getRight();
		case UP:
			return getTop();
		case DOWN:
			return getBottom();
		default:
			throw new IllegalArgumentException( direction + " is not supported" );
		}
	}

	private Objects getObject(Position position) {
		return position == null ? null : matrix[ position.getRow() ][ position.getColumn() ];
	}
	
	
	private static Direction[] MOVEMENT = new Direction[]{LEFT, RIGHT, UP, DOWN};

	private static boolean bombed = false;
	public Direction getNotWallDirection() {
		Direction result = getNextMove();
		if ( result == ACT ) {
			bombed = true;
		}
		else {
			bombed = false;
		}
		
		if ( result != ACT ) {
			Set<Direction> spacesAround = getSpacesAround();
			if ( spacesAround.isEmpty() ) { //zamurovali
				result = ACT;
			}
		}
		
		//don't want to move to the previous
		if ( result != ACT && isLastChance( result ) ) {
			Set<Direction> spacesAround = getSpacesAround();
			spacesAround.remove(result);
			if ( spacesAround.isEmpty()  ) {
				System.out.println( "There is only last-chance move..." );
				return result;
			}
			
			System.out.println( "Urra, found not last-chance" );
			return spacesAround.iterator().next(); //any not-last-chance
		}
		
		return result;
	}

	//I want reduce possibility of returning back to the same position
	private boolean isLastChance(Direction result) {
		return my.to(result).equals( DirectionSolver.getPreviousPosition() );
	}

	private Direction getNextMove() {
		System.out.println( "My : " + my + "LEft: " + getLeft() + " Right: " + getRight() + " top : " + getTop() + " bottom: " + getBottom() );
		
		Set<Direction> allowed = getSpacesAround();
		
		if ( allowed.isEmpty() ) {
			return Direction.ACT; //no choice?? set up bomb...	
		}
		
		Position enemy = getNearestEnemy();
		System.out.println( "Nearest enemy: " + enemy );
		if ( enemy == null ) {
			return allowed.iterator().next(); //maybe random from allowed?
		}
		
//		if ( enemy.distanceFrom(my) > 25 ) { //if too far no sense?...
//			System.out.println( "Enemies too far... Chase after walls..." );
//			enemy = getNearestWallToBomb(); //maybe diabchik?
//		}
//		
		

		
		if ( bombed ) { //need to move away from bomb
			return findStrategicallyBetter(allowed);
		}
		
		//less bombing
		int distanceToEnemy = enemy.distanceFrom(my);
		if ( distanceToEnemy <= 1 ) { //2 * 2? hmmm
			return ACT;
		}
		
		Position diabchik = getNearestDiabchik();
		if ( diabchik.distanceFrom( my ) <= 4 ) {
			System.out.println( "Diabchik is near...bombing " );
			return ACT;
		}
		
		Position wallToBomb = getNearestWallToBomb();
		if ( wallToBomb.distanceFrom( my ) <= 1 ) { //no sense to bomb if not near wall - only connected
			System.out.println( "Wall to bomb is near...bombing " );
			return ACT;
		}
		

		Set<Direction> prioritizedSuggestions = chaseAfterEnemy(allowed, enemy);

		Direction strategical = findStrategicallyBetter(prioritizedSuggestions);
		if ( strategical != null && getAmountOfSpaces(my.to(strategical) ) != 0 ) { //avoid putting down into trap by prioritized suggestion!
			System.out.println( "PRioritized decision is fine" );
			return strategical;
		}
		if ( strategical != null && getAmountOfSpaces(my.to(strategical) ) == 0 && my.distanceFrom(enemy) == 4)  { //same line or row
			System.out.println( "2 squares from..." );
			return strategical;
		}
		
		if ( strategical != null ) {
			System.out.println( "PRioritized is bad" );
		}

		System.out.println( "No optimal solution... " );
		
		return findStrategicallyBetter(allowed);
	}

	private Set<Direction> chaseAfterEnemy(Set<Direction> allowed,
			Position enemy) {
		int rowsToMove = enemy.getRow() - my.getRow();
		int columnsToMove = enemy.getColumn() - my.getColumn();
		
		Set< Direction > prioritizedSuggestions = new HashSet<Direction>();
		
		if ( Math.abs( rowsToMove ) <  Math.abs( columnsToMove ) && columnsToMove > 0 ) {
			prioritizedSuggestions.add(RIGHT);
		}
		
		if ( Math.abs( rowsToMove ) <  Math.abs( columnsToMove ) && columnsToMove < 0 ) {
			prioritizedSuggestions.add(LEFT);
		}
		
		if ( Math.abs( rowsToMove ) >  Math.abs( columnsToMove ) && rowsToMove > 0 ) {
			prioritizedSuggestions.add(DOWN);
		}
		
		if ( Math.abs( rowsToMove ) >  Math.abs( columnsToMove ) && rowsToMove < 0 ) {
			prioritizedSuggestions.add(UP);
		}
		
		if ( Math.abs( rowsToMove ) == Math.abs( columnsToMove ) ) {
			prioritizedSuggestions.add( rowsToMove > 0 ? DOWN : UP );
			prioritizedSuggestions.add( columnsToMove > 0 ? RIGHT : LEFT );
		}
		
		prioritizedSuggestions.retainAll( allowed );
		return prioritizedSuggestions;
	}

	//go to direction with BIGGER amount of moves!!!
	private Direction findStrategicallyBetter(Collection<Direction> allowed) {
		if ( allowed.isEmpty() ) {
			return null;
		}
		Direction subOptimal = allowed.iterator().next();
		int maxWays = Integer.MIN_VALUE;
		
		for ( Direction direction : allowed ) {
			Position position = my.to( direction );
			int amount = getAmountOfSpaces( position );
			System.out.println( amount );
			if ( amount > maxWays ) {
				maxWays = amount;
				subOptimal = direction;
			}
		}
		return subOptimal;
	}

	private int getAmountOfSpaces(Position position) {
		if ( position == null ) {
			return 0;
		}
		int amount = 0;
		for ( Direction direction : MOVEMENT ) {
			if ( getObject( position.to(direction) ) == Objects.SPACE ) { //TODO: maybe some destroyed results as well
				amount++;
			}
		}
		return amount;
	}

	private Set<Direction> getSpacesAround() {
		Set< Direction > allowed = new HashSet<Direction>(); 
		for ( Direction direction : MOVEMENT ) {
			if ( get( direction ) == Objects.SPACE ) { 
				allowed.add(direction); 
			}
		}
		return allowed;
	}
	
	private Position getNearestEnemy() {
		return findNearestTarget(enemies);
	}
	
	private Position getNearestDiabchik() {
		return findNearestTarget(diabchiks);
	}
	
	private Position getNearestWallToBomb() {
		return findNearestTarget(wallsToBomb);
	}

	private Position findNearestTarget(List<Position> targets) {
		if ( targets.isEmpty() ) {
			return null;
		}
		
		int nearestIndex = -1;
		int minimalDistance = Integer.MAX_VALUE;
		
		for ( int enemyIndex = 0; enemyIndex < targets.size(); enemyIndex ++ ) {
			if ( minimalDistance > my.distanceFrom( targets.get(enemyIndex) ) ) {
				minimalDistance = my.distanceFrom( targets.get(enemyIndex) );
				nearestIndex = enemyIndex;
			}
		}
		
		if ( nearestIndex == -1 ) {
			return null;
		}
		
		return targets.get(nearestIndex);
	}
}
