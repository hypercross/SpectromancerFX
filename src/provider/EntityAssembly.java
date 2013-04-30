package provider;

import java.util.Random;

import spectromancer.playerClass.Cleric;
import spectromancer.playerClass.Necromancer;
import entity.EntityCard;
import entity.EntityGame;
import entity.EntityPlayer;
import game.PlayerClass;
import game.Power;

public class EntityAssembly {
	public static Random rand = new Random();

	public static EntityGame setupTestGame() 
	{
		return setupGameByClass(Necromancer.instance, Cleric.instance);
	}
	
	public static void assignRandomCards(EntityPlayer p)
	{
		for(Power pwr : p.getPowers())
		{
			int[] ids = new int[4];
			
			for(int i = 0 ; i < 4;i++)
			{
				ids[i] = rand.nextInt(pwr.getSize());
				while(true)
				{
					boolean done = true;
					for(int j = 0 ; j < i; j ++)if(ids[j] == ids[i])done = false;
					if(done)break;
					ids[i] = rand.nextInt(pwr.getSize());
				}
			}
			
			for(int i : ids)
			{
				p.giveCard(new EntityCard(pwr.getCard(i), p));
			}
		}
	}

	public static EntityGame setupGameByClass(PlayerClass value, PlayerClass value2) {
		
		EntityGame game = new EntityGame();
		EntityPlayer a = new EntityPlayer(false, game, value);
		EntityPlayer b = new EntityPlayer(true, game, value2);
		HandAssembly.assignCards(a, b);
		game.setup(a, b);
		
		return game;
	}
}
