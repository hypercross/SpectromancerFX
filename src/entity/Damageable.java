package entity;

import game.DamageType;

public interface Damageable {
	public boolean damage(int x, DamageType type);
}
