package spectromancer.playerClass;

import spectromancer.SpPlayer;
import spectromancer.power.Holy;

public class Cleric extends SpPlayer{
	public static Cleric instance = new Cleric();
	
	private Cleric()
	{
		super(Holy.instance);
	}

}
