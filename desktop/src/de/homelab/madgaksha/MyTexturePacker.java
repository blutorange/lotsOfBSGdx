package de.homelab.madgaksha;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyTexturePacker {
	public static void main(String[] args) {
		String inputDir, outputDir, packFileName;
		
		System.out.println("packing 9 patches...");
		inputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/9patch";
		outputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/9patch/packed";
		packFileName = "9patchPacked";
		TexturePacker.process(inputDir, outputDir, packFileName);
		
		System.out.println("packing particle effects...");
		inputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/particle";
		outputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/particle/packed";
		packFileName = "particleEffectPacked";
		TexturePacker.process(inputDir, outputDir, packFileName);	}
}
