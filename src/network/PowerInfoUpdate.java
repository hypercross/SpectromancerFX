package network;

import javafx.scene.text.Text;
import entity.EntityPlayer;

public class PowerInfoUpdate {

	public int index;
	public int value;
	public String powerName;
	
	public PowerInfoUpdate(){}
	
	public PowerInfoUpdate(EntityPlayer player, int i)
	{
		index = i;
		value = player.getPowerValue(i);
		powerName = player.getPowers()[i].getDisplayName();
	}
	
	public void apply(Text texts)
	{
		texts.setText(powerName + ": " + value);
	}
}
