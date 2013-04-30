package spectromancer.playerClass;

import spectromancer.SpPlayer;
import spectromancer.power.Fire;

public class TestPlayerClass extends SpPlayer{
	public static TestPlayerClass instance = new TestPlayerClass();
	
	private TestPlayerClass()
	{
		super(Fire.instance);
	}

}
