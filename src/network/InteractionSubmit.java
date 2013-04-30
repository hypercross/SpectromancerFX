package network;

import entity.EntityGame;

public class InteractionSubmit {

	public boolean slotSide;
	public int slotID = -1;
	public int cardX = -1;
	public int cardY = -1;
	
	public InteractionSubmit(){}
	
	public InteractionSubmit(int x,int y)
	{
		cardX = x;
		cardY = y;
	}
	
	public InteractionSubmit(int slot, boolean side)
	{
		slotID = slot;
		slotSide = side;
	}
	
	public int getSlotID(EntityGame game)
	{
		int slotID = this.slotID;
		if(game.thread.playerOnTurn == game.opponent)slotID ^= 8;
		if(slotSide)slotID ^= 8;
		
		return slotID;
	}
	
	public String toString()
	{
		return slotID + "," + cardX +"," + cardY;
	}
}
