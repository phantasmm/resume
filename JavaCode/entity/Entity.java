package entity;

import java.io.InputStream;
import java.util.Random;

import javafx.scene.image.Image;

public abstract class Entity {

	private String name;
	
	private int hp;
	private int maxHp;
	private int atk;
	private boolean dead=false;
	
	
	public Entity(String name,int maxHp,int atk) {
		this.setName(name);
		
		this.setMaxHp(maxHp);
		this.setAtk(atk);
		this.setHp(maxHp);
		this.setIsDead(false);
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	

	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		if (hp<=0) {hp=0;this.setIsDead(true);}
		if (hp>=maxHp) this.hp =maxHp;
		
	}
	public void setMaxHp(int maxHp) {
		if (maxHp<0) maxHp=0;
		this.maxHp=maxHp;
	}
	public int getMaxHp() {
		return maxHp;
	}
	
	public void decreaseHp(int amount) {
		if (amount>=hp) {this.hp = 0;this.setIsDead(true);return;}
		this.hp-=amount;
	}
	public void increaseHp(int amount) {
		if (this.hp+amount >= maxHp) {this.hp=maxHp;return;}
		this.hp+=amount;
	}


	public int getAtk() {
		
		return atk;
	}
	public void setAtk(int atk) {
		if (atk<0) atk=0;
		this.atk = atk;
	}

	
	public boolean isDead() {
		return dead;
	}
	public void setIsDead(boolean isDead) {
		this.dead=isDead;
	}



	public void attack(Entity target) {
		
		Random r = new Random();
		int dmg = this.getAtk()+r.nextInt(this.getAtk()/2+1);
		
		target.decreaseHp(dmg);
		
		if (target.getHp()==0) target.setIsDead(true);
		
		
	}
	
	
}