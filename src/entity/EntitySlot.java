package entity;

public class EntitySlot extends Entity{

	public EntityCreature creature;
	public EntityGame game;
	public EntityPlayer player;
	
	public static final int SLOT_PLAYER_MASK = 8;
	public static final int SLOT_LOCATION_MASK = 7;
	public final int slotID;
	
	public EntitySlot(EntityPlayer player, int slotID)
	{
		game = player.game;
		this.player = player;
		this.slotID = slotID;
	}
	
	public String toString()
	{
		return "Slot#" + slotID;
	}
	
	public EntitySlot getOpposite()
	{
		return game.getSlot(slotID ^ SLOT_PLAYER_MASK);
	}
	
	public EntitySlot[] getNeighbors()
	{
		int location = slotID & SLOT_LOCATION_MASK;
		int base     = slotID & SLOT_PLAYER_MASK;
		
		if(location == 0)return new EntitySlot[] {game.getSlot(base | 1)};
		if(location == 5)return new EntitySlot[] {game.getSlot(base | 4)};
		
		return new EntitySlot[] {game.getSlot(base | location + 1), game.getSlot(base | location - 1)};
	}
	
	public EntitySlot[] getAllies()
	{
		EntitySlot[] slots = new EntitySlot[6];
		int base     = slotID & SLOT_PLAYER_MASK;
		
		for(int i = 0 ; i < 6 ; i ++)
			slots[i] = game.getSlot(base | i);
		
		return slots;
	}
	
	public static EntitySlot[] getSlotsOf(EntityPlayer player)
	{
		EntitySlot[] slots = new EntitySlot[6];
		int base = player.side ? SLOT_PLAYER_MASK : 0;
		
		for(int i = 0 ; i < 6 ; i ++)
			slots[i] = player.game.getSlot(base | i);
		
		return slots;
	}
}
