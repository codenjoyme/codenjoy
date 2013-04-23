package com.leokom;


/**
 * User: sanja
 * Date: 21.04.13
 * Time: 3:13
 */
public class DirectionSolver {
	private static Position previous;
	public static Position getPreviousPosition() {
		return previous;
	}
	
    public Direction get(String board) {
    	Matrix matrix = new Matrix( board );
    	System.out.println( matrix.current() );
    	
    	Direction direction = matrix.getNotWallDirection();
    	
    	previous = matrix.current();
    	
        System.out.println( direction );
		return direction;
    }
}
