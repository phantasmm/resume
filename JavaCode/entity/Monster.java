package entity;

import java.io.InputStream;
import java.util.Random;

import getImagable.GetImagable;
import javafx.scene.image.Image;

public class Monster extends Entity implements GetImagable {

	
	public Monster(String name, int maxHp, int atk) {
		
		super(name,maxHp,atk);
		
		Random r = new Random();
		int newMaxHp= (int) (maxHp*(1+r.nextFloat()/4));
		int range = atk/2;
		
		if (range ==0) range++;
		
		int newAttack = atk + r.nextInt(range+1);
		this.setMaxHp(newMaxHp);
		this.setHp(newMaxHp);
		this.setAtk(newAttack);
	}
	
	
	public Image getCharImage() {
		String str = "/monsterImage/"+this.getName()+".png";
		InputStream inp  = getClass().getResourceAsStream(str);
		Image img = new Image(inp);
		return img; 
	}

	public Image getDeadImage() {
		String str = "/monsterImage/"+this.getName()+"D.png";
		InputStream inp  = getClass().getResourceAsStream(str);
		Image img = new Image(inp);
		return img; 
	}
}
