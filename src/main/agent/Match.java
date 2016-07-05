package main.agent;

import org.opencv.core.Mat;

public class Match {
		double x;
		double y;
		double score;
		public Match(double x, double y, double score){
			this.x = x;
			this.y = y;
			this.score = score;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public double getScore() {
			return score;
		}
		
		@Override
		public String toString() {
		return super.toString() + "{ x = " + x + " y = " + y + " score = " + score +"}";
		}
}
