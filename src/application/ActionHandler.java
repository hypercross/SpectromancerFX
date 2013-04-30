package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

import javax.swing.JOptionPane;

import util.Logger;

import network.CardUpdate;
import network.InteractionSubmit;
import network.LogUpdate;
import network.NetworkHelper;
import network.PlayerInfoUpdate;
import network.PowerInfoUpdate;
import network.SlotUpdate;
import entity.EntityGame;

@SuppressWarnings("unused")
public class ActionHandler {
	
	@FXML
	private GridPane grid_slots;
	
	private Button slots[] = new Button[12];
	
	@FXML 
	private GridPane grid_cards;
	
	private Button cards[] = new Button[30];
	
	private Text texts[] = new Text[5];
	
	@FXML
	private WebView webview_players;
	
	@FXML
	private WebView webview_log;
	
	@FXML
	private void onConnectTo(ActionEvent event) throws IOException
	{
		setupGUI();
		NetworkHelper.instance.startLANClient(this);
	}
	
	@FXML
	private void onNewGame(ActionEvent event) throws IOException
	{
		setupGUI();
		new DialogStartGame(this);
	}
	
	@FXML
	private void onClose(ActionEvent event)
	{
		System.exit(0);
	}
	
	@FXML
	private void onAbout(ActionEvent event)
	{
		JOptionPane.showMessageDialog(null, "hypercross's Spectromancer" ,"About",  JOptionPane.PLAIN_MESSAGE);  
	}
	
	private void handleSlotClick(int i)
	{
		NetworkHelper.instance.sendTCP(new InteractionSubmit(i%6, i<6));
	}
	
	private void handleCardClick(int x,int y)
	{
		NetworkHelper.instance.sendTCP(new InteractionSubmit(x,y));
	}
	
	private class ClickHandler implements EventHandler<MouseEvent>
	{
		int i=  -1,x = -1,y = -1;
		ClickHandler(int i)
		{
			this.i = i; 
		}
		ClickHandler(int x,int y)
		{
			this.x = x;
			this.y = y;
		}
		@Override
		public void handle(MouseEvent event) {
			if(i>=0)handleSlotClick(i);
			else handleCardClick(x,y);
		}
	}
	
	private void setupGUI()
	{
		for(int i = 0 ; i < 12;i++)
		{
			Button b = new Button();
			b.setDisable(true);
			AnchorPane p = new AnchorPane();
			b.setGraphic(p);
			b.setPrefSize(72,72);
			b.addEventHandler(MouseEvent.MOUSE_CLICKED, new ClickHandler(i));
			slots[i] = b;
			grid_slots.add(b, i%6, i/6);
		}
		
		for(int i = 0 ; i < 5 ; i ++)
		{
			Text t = new Text("Power: x" );
			grid_cards.add(t, i, 0);
			texts[i] = t;
		}
		
		for(int i = 0; i<5;i++)
			for(int j = 1;j<5;j++)
			{
				Button b = new Button();
				b.setDisable(true);
				AnchorPane p = new AnchorPane();
				p.setPrefSize(80,80);
				b.setGraphic(p);
				b.addEventHandler(MouseEvent.MOUSE_CLICKED, new ClickHandler(i,j-1));
				b.setPrefSize(90, 90);
				grid_cards.add(b, i,j);
				cards[j*5 + i - 5] = b;
			}
	}

	public void receiveUpdate(Object object) {
		if(object instanceof CardUpdate)
		{
			CardUpdate update = (CardUpdate)object;
			update.apply(cards[update.posx + update.posy * 5]);
		}
		else if(object instanceof SlotUpdate)
		{
			SlotUpdate update = (SlotUpdate)object;
			update.apply(slots[ (update.slotID & 7) + update.slotID/8*6 ]);
		}
		else if(object instanceof PlayerInfoUpdate)
		{
			PlayerInfoUpdate update = (PlayerInfoUpdate)object;
			update.apply(webview_players);
		}else if (object instanceof PowerInfoUpdate)
		{
			PowerInfoUpdate update = (PowerInfoUpdate)object;
			update.apply(texts[update.index]);
		}else if(object instanceof LogUpdate)
		{
			LogUpdate lu = (LogUpdate)object;
			lu.apply(webview_log);
		}
	}
}
