package entity;

import provider.EntityAssembly;
import provider.GameThread;
import provider.IUpdateQueue;

public class EntityGame extends Entity{

	public EntityPlayer self, opponent;
	public EntitySlot[] slots;
	public GameThread thread;
	
	public boolean finished;
	
	public EntityGame()
	{
		slots = new EntitySlot[16];
	}
	
	public void setup(EntityPlayer self, EntityPlayer opponent)
	{
		this.self = self;
		this.opponent = opponent;
		
		for(int i = 0 ; i < 6;i++)
		{
			slots[i] = new EntitySlot(self, i);
			slots[i + 8] = new EntitySlot(opponent, i + 8);
		}
		self.join();
		opponent.join();
	}
	
	public EntitySlot getSlot(int i)
	{
		return slots[i];
	}
	
	public void start()
	{
		thread = new GameThread(this, EntityAssembly.rand.nextBoolean() ? self : opponent);
		new Thread(thread).start();
	}
	
	public void start(IUpdateQueue queue)
	{
		start();
		thread.setUpdateHandler(queue);
	}
	
	public void terminate()
	{
		finished = true;
	}
}
