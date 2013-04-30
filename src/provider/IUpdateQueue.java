package provider;

import entity.EntityGame;

public interface IUpdateQueue {

	public void updateGame(EntityGame game);
	
	public void pushUpdate(Object o);
	
	public void announce(String tag, String log);
}
