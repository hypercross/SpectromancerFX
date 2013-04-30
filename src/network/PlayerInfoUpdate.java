package network;

import javafx.scene.web.WebView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import entity.EntityPlayer;

public class PlayerInfoUpdate {

	public String self_name, opponent_name;
	public int self_hp, opponent_hp;
	public boolean onTurn;
	public boolean gameRunning;
	
	public PlayerInfoUpdate(){}
	
	public PlayerInfoUpdate(EntityPlayer self, EntityPlayer opponent)
	{
		this.onTurn = self.isOnTurn();
		gameRunning = self.game().thread == null || self.game().finished;
		gameRunning = !gameRunning;
		self_hp = self.getHp();
		opponent_hp = opponent.getHp();
		self_name = self.toString();
		opponent_name = opponent.toString();
	}
	
	public void apply(WebView w)
	{
		String text1 = "Opponent (" + opponent_name + "): " + opponent_hp;
		
		String text2 = "Self ("		+ self_name +"): " + self_hp;
		
		String text3 = gameRunning ? (onTurn ? "It's your turn!" : "Waiting for opponent...") : "Game is now inactive.";
		Document doc = w.getEngine().getDocument();
				
		Node paragraphs = doc.getLastChild().getLastChild();
		if(paragraphs.getChildNodes().getLength() == 0)
			for(int i = 0 ; i < 3; i ++)
		{
			Element line = doc.createElement("p");
			line.setAttribute("style", "text-align:center;");
			paragraphs.appendChild(line);
		}
		
		paragraphs.getChildNodes().item(0).setTextContent(text1);
		paragraphs.getChildNodes().item(1).setTextContent(text2);
		paragraphs.getChildNodes().item(2).setTextContent(text3);
		w.getEngine().reload();
	}
}
