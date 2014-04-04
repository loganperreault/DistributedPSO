package tools;

import java.util.Random;

public class Tools {

	// a seeded random object to be used by all
	public static Random random = new Random(11235);
	
	/**
	 * Returns a random value between any two double values.
	 * 
	 * @param minValue	The minimum value that can be produced.
	 * @param maxValue	The maximum value that can be produced.
	 * @return			A random value in the range (minValue,maxValue)
	 */
	public static double getRandomDouble(double minValue, double maxValue) {
		return getRandomDouble(minValue, maxValue, random);
	}
	
	/**
	 * Returns a random value between any two double values.
	 * 
	 * @param minValue	The minimum value that can be produced.
	 * @param maxValue	The maximum value that can be produced.
	 * @param random	The random generator to use for producing doubles.
	 * @return			A random value in the range (minValue,maxValue)
	 */
	public static double getRandomDouble(double minValue, double maxValue, Random random) {
		return minValue + (maxValue - minValue) * random.nextDouble();
	}
	
	/**
	 * Rounds a double to a specified number of decimal places.
	 * 
	 * @param value		The value to be rounded.
	 * @param decimals	The number of decimal places to round to.
	 * @return			The rounded value.
	 */
	public static double round(double value, int decimals) {
		int pow = (int) Math.pow(10, decimals);
		double val = Math.round(value * pow);
		return (double) Math.round(val) / pow;
	}
	
	public static int toInt(double value) {
		return (int)Math.round(value);
	}
	
	public static double euclidean(double[] vector1, double[] vector2) {
		double distance = 0.0;
		verifyLengths(vector1, vector2);
		for (int i = 0; i < vector1.length; i++) {
			distance += Math.pow(vector1[i] - vector2[i], 2);
		}
		distance = Math.sqrt(distance);
		return distance;
	}
	
	public static void verifyLengths(double[] vector1, double[] vector2) {
		if (vector1.length != vector2.length) {
			System.out.println("ERROR: Vector lengths must match");
			System.exit(1);
		}
	}
	
}
