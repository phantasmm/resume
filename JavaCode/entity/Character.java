package entity;

import java.io.InputStream;
import java.util.Random;

import getImagable.GetImagable;
import javafx.scene.image.Image;

public class Character extends Entity implements GetImagable{

	private boolean isBoy;
	
	
	
	public Character(String name,int maxHp,int atk,boolean isBoy) {
		super(name,maxHp,atk);
		this.setIsDead(false);
		this.isBoy=isBoy;
		
	}



	
	public Image getPortrait() {
		
		String str = "/characterImage/steveP.png";
		if (this.isBoy) str ="/characterImage/boyP.png";
		if (!this.isBoy) str ="/characterImage/girlP.png";
		InputStream inp  = getClass().getResourceAsStream(str);
		Image img = new Image(inp);
		return img; 
	}
	
	public Image getCharImage() {
		
		String str = "/characterImage/steveC.png";
		if (this.isBoy) str ="/characterImage/boy.png";
		if (!this.isBoy) str ="/characterImage/girl.png";
		InputStream inp  = getClass().getResourceAsStream(str);
		Image img = new Image(inp);
		return img; 
	}
	
}
