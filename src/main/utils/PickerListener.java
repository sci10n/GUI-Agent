package main.utils;

import java.awt.image.BufferedImage;

@FunctionalInterface
public interface PickerListener {

	public void process(BufferedImage image);
}
