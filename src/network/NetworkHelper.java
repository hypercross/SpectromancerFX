package network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

import provider.IUpdateQueue;

import javafx.application.Platform;
import application.ActionHandler;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import entity.EntityCard;
import entity.EntityGame;
import entity.EntityPlayer;
import entity.EntitySlot;

public class NetworkHelper implements IUpdateQueue{
	public static NetworkHelper instance = new NetworkHelper();
	
	Server server;
	HashMap<EntityPlayer,Connection> controller = new HashMap<EntityPlayer,Connection>();
	Client client;
	
	public void updateGame(EntityGame game)
	{
		for(EntityPlayer p : controller.keySet())updateGame(controller.get(p), game, p);
	}
	
	public void updateGame(Connection c, EntityGame game, EntityPlayer player)
	{
		c.sendTCP(new PlayerInfoUpdate(player, player.getOpponent()));
		
		for(EntitySlot slot : game.slots)
		{
			if(slot != null)c.sendTCP(new SlotUpdate(slot, player == game.self));
		}
		
		for(int i = 0 ; i < player.getPowers().length; i++)
		{
			c.sendTCP(new PowerInfoUpdate(player, i));
		}
		
		EntityCard[][] allCards = player.getCards();
		
		for(int i = 0 ; i < allCards.length;i++)
			for(int j = 0 ; j < allCards[i].length;j++)
				c.sendTCP(new CardUpdate(allCards[i][j], i, j ));
	}
	
	private void registerClasses(Kryo kryo)
	{
		kryo.register(PlayerInfoUpdate.class);
		kryo.register(PowerInfoUpdate.class);
		kryo.register(SlotUpdate.class);
		kryo.register(CardUpdate.class);
		kryo.register(InteractionSubmit.class);
		kryo.register(LogUpdate.class);
	}
	
	public void startIntegrated(final EntityGame game, final ActionHandler actionHandler) throws IOException
	{
		server = new Server();
		registerClasses(server.getKryo());
		server.start();
		server.bind(25671, 25764);
		server.addListener(new Listener()
		{ 
			public void connected(Connection connection)
		    {
				if(!controller.containsKey(game.self))
				{
					controller.put(game.self, connection);
					updateGame(connection, game, game.self);
				}else if(!controller.containsKey(game.opponent))
				{
					controller.put(game.opponent, connection);
					updateGame(connection, game, game.opponent);
					game.start(NetworkHelper.instance);
				}else connection.close();
		    }
			
			public void received (Connection connection, Object object)
			{
				if(object instanceof InteractionSubmit && game.thread != null && controller.get(game.thread.playerOnTurn) == connection)
				{
					InteractionSubmit is = (InteractionSubmit)object;
					if(is.slotID >= 0)
						game.thread.provideInteraction(game.getSlot(is.getSlotID(game)));
					else if(is.cardX >= 0)
						game.thread.provideInteraction(game.thread.playerOnTurn.getCards()[is.cardX][is.cardY]);
				}
			}
		});
		
		startLANClient(actionHandler);
	}
	
	public void startLANClient(final ActionHandler actionHandler) throws IOException
	{
		client = new Client();
		registerClasses(client.getKryo());
		client.start();
		client.addListener(new Listener()
		{
			public void received (Connection connection, Object object)
			{	
				final Object obj = object;
				Platform.runLater(new Runnable()
				{
					public void run()
					{
						actionHandler.receiveUpdate(obj);
					}
				});
			}
		});
		
		InetAddress address = client.discoverHost(25764, 5000);
		client.connect(5000, address, 25671, 25764);
	}
	
	public void sendTCP(Object o)
	{
		client.sendTCP(o);
	}

	@Override
	public void pushUpdate(Object o) {
		for(EntityPlayer p : controller.keySet())controller.get(p).sendTCP(o);
	}

	@Override
	public void announce(String tag, String log) {
		pushUpdate(new LogUpdate(tag,log));
	}
}
