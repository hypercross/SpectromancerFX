package network;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import application.ResourcePool;
import entity.EntityCard;

public class CardUpdate {

	public int cost;
	public boolean usable;
	public String cardName;
	public int posx;
	public int posy;

	public CardUpdate(){}

	public CardUpdate(EntityCard card, int x,int y)
	{
		posx = x;
		posy = y;
		cardName = card == null ? "NoCard" : card.card.toString();

		if(card != null)
		{
			cost = card.cost;
			usable = card.usable && card.sufficientPower();
		}
	}

	public void apply(Button b)
	{
		b.setDisable(!usable);

		Pane p = (Pane) b.getGraphic();		
		List<Node> nodes = p.getChildren();
		nodes.clear();

		if(nodes.isEmpty())
		{
			Node n = ResourcePool.get("/Cards/" + cardName.replaceAll(" ", "").replaceAll("'", ""));
			if(n != null)nodes.add(n);

			Rectangle r = new Rectangle();
			r.setWidth(80);
			r.setHeight(20);
			r.setY(60);
			r.setFill(new Color(1d,1d,1d,0.5d));
			nodes.add(r);

			Text t = new Text("C " + cost);
			t.setY(75);
			t.setX(5);
			t.setEffect( new DropShadow());
			nodes.add(t);
		}
	}

	private boolean isEmpty()
	{
		return cardName.equals("NoCard");
	}
}
