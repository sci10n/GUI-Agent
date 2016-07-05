package main.agent;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.imgproc.Imgproc;

public class Matcher {

	private int method;
	private double threshold = 0.97;
	public Matcher(int method, double threshold){
		this.method = method;
	}
	
	public void setMethod(int method){
		this.method = method;
	}
	
	public int getMethod(){
		return method;
	}
	
	public Match match(Mat screen, Mat template){
		double score;
		int result_cols = screen.cols() - template.cols() + 1;
		int result_rows = screen.rows() - template.rows() + 1;
		
		Mat result = new Mat(result_rows, result_cols, CvType. CV_32FC1);
		Mat templateMat =  new Mat(template.cols(), template.rows(), CvType. CV_32FC1);
		Mat screenMat =  new Mat(screen.cols(), screen.rows(), CvType. CV_32FC1);
		
		screen.convertTo(screenMat, CvType.CV_32F);
		template.convertTo(templateMat, CvType.CV_32F);
		
		Imgproc.matchTemplate(screenMat, templateMat, result, method);
		MinMaxLocResult mmr = Core.minMaxLoc(result);

		Point matchLoc;
		if (method == Imgproc.TM_SQDIFF || method == Imgproc.TM_SQDIFF_NORMED) {
			matchLoc = mmr.minLoc;
			score = mmr.minVal; 
		} else {
			matchLoc = mmr.maxLoc;
			score = mmr.maxVal; 
		}
		//Imgproc.rectangle(screen, matchLoc, new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),new Scalar(0, 255, 0));
		Match match = new Match(matchLoc.x, matchLoc.y, score);
		return match;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
}
