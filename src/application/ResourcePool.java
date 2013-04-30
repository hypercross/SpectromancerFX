package application;

import javafx.scene.image.ImageView;

public class ResourcePool {
	public static ImageView get(String s)
	{
		String[] format = new String[]{".JPG", ".tga", "Big.png"};
		
		for(String suffix : format)
		{
			if(ResourcePool.class.getResource(s + suffix) == null)continue;
			s += suffix;
			return new ImageView(s);
		}
		return null;
	}
}
