package provider;

import util.Logger;
import entity.EntityCard;
import entity.EntityGame;
import entity.EntityPlayer;
import entity.EntitySlot;
import game.Event;

public class GameThread implements Runnable{

	private EntityCard selectedCard;
	private EntitySlot selectedSlot;
	private boolean interactionAvailable;

	private EntityGame game;
	public EntityPlayer playerOnTurn;
	
	private static IUpdateQueue updateQueue;
	
	
	public static void announce(String tag, String log)
	{
		if(updateQueue == null)return;
		updateQueue.announce(tag,log);
	}
	
	public static void announce(String log)
	{
		announce("p",log);
	}

	public GameThread(EntityGame game, EntityPlayer starter)
	{
		this.game = game;
		playerOnTurn = starter;
	}
	
	public EntityCard selectedCard()
	{
		return selectedCard;
	}
	
	public EntitySlot selectedSlot()
	{
		return selectedSlot;
	}
	
	public void setUpdateHandler(IUpdateQueue queue)
	{
		updateQueue = queue;
	}

	@Override
	public void run(){
		Logger.log("game starting..." + this);
		EventFactory.post(new Event.GameStart(playerOnTurn));
		while(!game.finished)
		{	
			announce("HR", "");
			EventFactory.post(new Event.TurnStart(playerOnTurn));
			Logger.log("waiting for interaction...");
			if(updateQueue!=null)updateQueue.updateGame(game);
			while(true)
			{
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(interactionAvailable)
					Logger.log("trying interaction...");
				else continue;
				
				interactionAvailable = false;

				if(selectedCard == null)continue;
				Event.TryUseCard use = new Event.TryUseCard(selectedCard, playerOnTurn, selectedSlot);
				EventFactory.post(use);
				if(use.valid)
				{
					Logger.log("interaction valid");
					use.user.savagePower(selectedCard.card.getPower(), selectedCard.cost);
					selectedCard = null;
					selectedSlot = null;
					break;
				}
			}
			EventFactory.post(new Event.TurnEnd(playerOnTurn));
			
			for(EntitySlot slot : EntitySlot.getSlotsOf(playerOnTurn)){
				if(slot.creature != null && !slot.creature.summonSickness)slot.creature.performAttack();
			}
			
			playerOnTurn = playerOnTurn.getOpponent();
		}
		announce("Game Finished!");
		if(updateQueue!=null)updateQueue.updateGame(game);
		Logger.log("game finished");
	}

	public void provideInteraction(EntityCard card)
	{
		selectedCard = card;
		interactionAvailable = true;
		Logger.log("provided card selection ");
	}

	public void provideInteraction(EntitySlot slot)
	{
		selectedSlot = slot;
		interactionAvailable = true;
		Logger.log("provided slot selection ");
	}
}
