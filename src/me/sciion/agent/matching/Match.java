package me.sciion.agent.matching;

public class Match {
    		int x;
		int y;
		double score;
		public Match(int x, int y, double score){
			this.x = x;
			this.y = y;
			this.score = score;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public double getScore() {
			return score;
		}
		
		
		public String toCSV(){
		    return "";
		}
		@Override
		public String toString() {
		return super.toString() + "{ x = " + x + " y = " + y + " score = " + score +"}";
		}
}
