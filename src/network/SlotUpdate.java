package network;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import application.ResourcePool;
import entity.EntitySlot;

public class SlotUpdate {

	public int slotID;
	public String creatureName;
	public int cost;
	public int hp;
	public int atk;

	public SlotUpdate(){}

	public SlotUpdate(EntitySlot slot, boolean invert)
	{
		slotID = slot.slotID;
		if(invert)slotID ^= 8;
		creatureName = slot.creature == null ? "NoCreature" : slot.creature.card.toString();

		if(slot.creature!=null)
		{
			cost = slot.creature.card.getCost();
			hp   = slot.creature.hp;
			atk  = slot.creature.getAttack();
		}
	}
	public void apply(Button b)
	{		
		b.setDisable(false);
		Pane p = (Pane) b.getGraphic();		
		List<Node> nodes = p.getChildren();
		nodes.clear();

		if(nodes.isEmpty())
		{
			ImageView n = ResourcePool.get("/Cards/" + creatureName.replaceAll(" ", "").replaceAll("'", ""));
			if(n != null)
			{
				n.setFitHeight(64);
				n.setFitWidth(64);
				nodes.add(n);
			}

			Rectangle r = new Rectangle();
			r.setWidth(64);
			r.setHeight(16);
			r.setY(48);
			r.setFill(new Color(1d,1d,1d,0.5d));
			nodes.add(r);

			Text t = new Text(atk + "/" + hp + " Cost " + cost);
			t.setY(60);
			t.setX(4);
			t.setEffect( new DropShadow());
			nodes.add(t);
		}
	}

	private boolean empty()
	{
		return creatureName.equals("NoCreature");
	}
}
