package org.usfirst.frc.team246.robot.overclockedLibraries;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author michaelsilver
 */
public class Vector2D {
    
    private double x;
    private double y;
    
    public Vector2D(boolean cartesian, double abscissa, double ordinate){
        if(cartesian){
            x = abscissa;
            y = ordinate;
        } else {
            double[] coords = polarToCart(abscissa, ordinate);
            x = coords[0];
            y = coords[1];
        }
    }
    
    public static double[] polarToCart(double r, double theta){
    	theta += 90;
        double[] cartCoords = {r*Math.cos(Math.toRadians(theta)), r*Math.sin(Math.toRadians(theta))};
        return cartCoords;
    }
    
    public Vector2D cloneVector(){
    	return new Vector2D(true, x, y);
    }
    
//    GETTERS
    public double getX(){
        return x;
    } 
    
    public double getY(){
        return y;
    }
    
    public double getAngle(){
        double angle = Math.toDegrees(Math.atan2(y, x)) - 90;
        if(angle < -180) return angle + 360;
        else if(angle > 180) return angle - 360;
        else return angle;
    }
    
    public double getMagnitude() {
        return Math.sqrt(x*x + y*y);
    }
    
//    SETTERS
    public void setX(double x){
        this.x = x;
    }
    
    public void setY(double y){
        this.y = y;
    }
    
    public void setAngle(double angle)
    {
        double[] newCoords = Vector2D.polarToCart(getMagnitude(), angle);
        x = newCoords[0];
        y = newCoords[1];
    }
    
    public void setMagnitude(double magnitude)
    {
        double[] newCoords = Vector2D.polarToCart(magnitude, getAngle());
        x = newCoords[0];
        y = newCoords[1];
    }
       
//    MATH OPERATIONS
    public static Vector2D addVectors(Vector2D vector1, Vector2D vector2){
        Vector2D sum = new Vector2D(true, vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY());
        return sum;
    }
    
    public static Vector2D subtractVectors(Vector2D vector1, Vector2D vector2){
        Vector2D sum = new Vector2D(true, vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY());
        return sum;
    }
    
    public Vector2D unitVector(){
        Vector2D unitVector = new Vector2D(true, x/getMagnitude(), y/getMagnitude());
        return unitVector;
    }
    
    //untested
    public static double dotProduct(Vector2D vector1, Vector2D vector2){
    	return vector1.getX()*vector2.getX() + vector1.getY()*vector2.getY();
    }
    
//    project vector1 onto vector2
  //untested
    public static Vector2D parallelProjection(Vector2D vector1, Vector2D vector2){
    	Vector2D projection = new Vector2D(false, dotProduct(vector1, vector2), vector2.getAngle());
    	return projection;
    }
    
//    the other (than the projection) component of vector1 with a coordinate system relative to vector2
  //untested
    public static Vector2D perpendicularProjection(Vector2D vector1, Vector2D vector2){
    	return subtractVectors(vector1, parallelProjection(vector1, vector2));
    }
}
