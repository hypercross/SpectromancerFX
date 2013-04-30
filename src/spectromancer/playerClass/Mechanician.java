package spectromancer.playerClass;

import spectromancer.SpPlayer;
import spectromancer.power.Mechanical;

public class Mechanician extends SpPlayer{
	public static Mechanician instance = new Mechanician();
	
	private Mechanician()
	{
		super(Mechanical.instance);
	}

}
