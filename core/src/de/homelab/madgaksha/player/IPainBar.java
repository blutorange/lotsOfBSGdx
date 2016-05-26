package de.homelab.madgaksha.player;

public interface IPainBar {

	public long getPainPoints();

	public long getMaxPainPoints();

	public float getPainPointsRatio();

	public boolean takeDamage(long damage);

	public boolean untakeDamage(long health);

	public boolean takeDamage(int damage);

	public boolean untakeDamage(int health);

	public boolean isDead();

	public boolean isUndamaged();

}