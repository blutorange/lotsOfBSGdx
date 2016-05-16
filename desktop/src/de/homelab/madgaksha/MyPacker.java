package de.homelab.madgaksha;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyPacker {
	public static void main(String[] args) {
		String inputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/bullets/basi";
		String outputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/bullets/basic-packe";
		String packFileName = "bulletsBasic";
		TexturePacker.process(inputDir, outputDir, packFileName);
	}
}
