package de.homelab.madgaksha.lotsofbs.entityengine.component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeListComponent.AnimationMode;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;

public class AnimationModeQueueComponent implements Component, Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AnimationModeQueueComponent.class);

	public final static class AnimationModeTransition {
		public final static boolean DEFAULT_RESET = true;
		public final static boolean DEFAULT_WAIT_FOR_COMPLETION = false;
		public AnimationMode targetMode = AnimationMode.NORMAL;
		public boolean reset = DEFAULT_RESET;
		public boolean waitForCompletion = DEFAULT_WAIT_FOR_COMPLETION;
	}
	
	public AnimationMode currentMode = AnimationMode.NORMAL;
	public final Deque<AnimationModeTransition> queue = new ArrayDeque<AnimationModeTransition>(16);


	public void queue(AnimationMode targetMode, boolean waitForCompletion, boolean reset) {
		AnimationModeTransition smt = ResourcePool.obtainAnimationModeTransition();
		smt.targetMode = targetMode;
		smt.reset = reset;
		smt.waitForCompletion = waitForCompletion;
		queue.push(smt);
	}

	
	public void queue(AnimationMode targetMode) {
		queue(targetMode, AnimationModeTransition.DEFAULT_WAIT_FOR_COMPLETION, AnimationModeTransition.DEFAULT_RESET);
	}
	
	public void queue(AnimationModeTransition spriteModeTransition) {
		queue.push(spriteModeTransition);
	}
	public void queue(List<AnimationModeTransition> spriteModeTransitionList) {
		queue.addAll(spriteModeTransitionList);
	}
	
	@Override
	public void reset() {
		currentMode = AnimationMode.NORMAL;
		queue.clear();
	}
}
