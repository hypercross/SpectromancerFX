package spectromancer.playerClass;

import spectromancer.SpPlayer;
import spectromancer.power.Death;

public class Necromancer extends SpPlayer{
	public static Necromancer instance = new Necromancer();
	
	private Necromancer()
	{
		super(Death.instance);
	}

}
