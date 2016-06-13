package de.homelab.madgaksha;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyTexturePacker {
	public static void main(String[] args) {
		String inputDir, outputDir, packFileName;
		
		System.out.println("packing bullets basic...");
		inputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/bullets/basic";
		outputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/bullets/basic-packed";
		packFileName = "bulletsBasic";
		TexturePacker.process(inputDir, outputDir, packFileName);
		
		System.out.println("packing bullets large...");
		inputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/bullets/large";
		outputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/bullets/large-packed";
		packFileName = "bulletsLarge";
		TexturePacker.process(inputDir, outputDir, packFileName);
		
		System.out.println("packing 9 patches...");
		inputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/9patch";
		outputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/9patch/packed";
		packFileName = "9patchPacked";
		TexturePacker.process(inputDir, outputDir, packFileName);
		
		System.out.println("packing particle effects...");
		inputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/particle";
		outputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/particle/packed";
		packFileName = "particleEffectPacked";
		TexturePacker.process(inputDir, outputDir, packFileName);
		
		System.out.println("packing misc textures...");
		inputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/texture/misc";
		outputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/texture/misc/packed";
		packFileName = "miscPacked";
		TexturePacker.process(inputDir, outputDir, packFileName);
		
		System.out.println("packing face textures...");
		outputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/texture/face-packed";

		inputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/texture/face/estelle";
		packFileName = "estellePacked";
		TexturePacker.process(inputDir, outputDir, packFileName);
		
		inputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/texture/face/joshua";
		packFileName = "joshuaPacked";
		TexturePacker.process(inputDir, outputDir, packFileName);
		
		System.out.println("packing status screen textures...");
		inputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/statusscreen";
		outputDir = "/home/madgaksha/git/lotsOfBSGdx/android/assets/statusscreen/packed";
		packFileName = "statusScreenPacked";
		TexturePacker.process(inputDir, outputDir, packFileName);
	}
}
