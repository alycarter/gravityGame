package com.alycarter.gravityGame.states.level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.alycarter.crabClawEngine.Game;
import com.alycarter.crabClawEngine.state.State;
import com.alycarter.gravityGame.states.level.entity.Entity;
import com.alycarter.gravityGame.states.level.entity.Planet;
import com.alycarter.gravityGame.states.level.entity.Ship;

public class Level implements State{
	private Game game;
	
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Ship> ships = new ArrayList<Ship>();
	public ArrayList<Planet> planets = new ArrayList<Planet>();
	public Ship selectedShip =null;
	
	public static double unitResolution =128;
	public Point2D.Double cameraLocation = new Point2D.Double(4, 3);
	
	public Level(Game game) {
		this.game=game;
		addPlanet(new Planet(this, new Point2D.Double(3, 3), 1));
		addPlanet(new Planet(this, new Point2D.Double(8, 3), 1));
		addShip(new Ship(this, new Point2D.Double(2, 3), new Point2D.Double(0, 1), 0.2));
		addShip(new Ship(this, new Point2D.Double(7, 3), new Point2D.Double(0, 1), 0.2));
	}
	
	public void addShip(Ship s){
		ships.add(s);
		entities.add(s);
		if(selectedShip== null){
			selectedShip=s;
		}
	}
	
	public void addPlanet(Planet p){
		planets.add(p);
		entities.add(p);
	}

	@Override
	public void update() {
		double xm=0;
		double ym=0;
		if(game.getControls().isPressed(KeyEvent.VK_RIGHT)){
			xm+=1;
		}
		if(game.getControls().isPressed(KeyEvent.VK_LEFT)){
			xm-=1;
		}
		if(game.getControls().isPressed(KeyEvent.VK_DOWN)){
			ym+=1;
		}
		if(game.getControls().isPressed(KeyEvent.VK_UP)){
			ym-=1;
		}
		cameraLocation.x+=Entity.angleAsVector(Entity.vectorAsAngle(new Point2D.Double(xm,ym))).x*game.getDeltaTime()*Math.abs(xm);
		cameraLocation.y+=Entity.angleAsVector(Entity.vectorAsAngle(new Point2D.Double(xm,ym))).y*game.getDeltaTime()*Math.abs(ym);
		if(game.getControls().leftMouseClicked()){
			selectedShip=null;
			double x = (game.getControls().mouseLocation.x+getOffset().x)/Level.unitResolution;
			double y = (game.getControls().mouseLocation.y+getOffset().y)/Level.unitResolution;
			for(int i=0;i< ships.size();i++){
				double dx = x-ships.get(i).location.x;
				double dy = y-ships.get(i).location.y;
				if(Math.abs(ships.get(i).width/2)>Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2))){
					selectedShip=ships.get(i);
				}
			}
		}
		if(game.getControls().isPressed(KeyEvent.VK_MINUS)){
			unitResolution-=50*game.getDeltaTime();
		}
		if(game.getControls().isPressed(KeyEvent.VK_EQUALS)){
			unitResolution+=50*game.getDeltaTime();
		}
		if(selectedShip !=null){
			if(game.getControls().isPressed(KeyEvent.VK_G)){
				selectedShip.turnPrograde();
			}
			if(game.getControls().isPressed(KeyEvent.VK_H)){
				selectedShip.turnRetrograde();
			}
			if(game.getControls().isPressed(KeyEvent.VK_SPACE)){
				selectedShip.burn();
			}
		}
		for(int i=0;i<entities.size();i++){
			entities.get(i).update();
		}
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g.setColor(Color.white);
		g.fillRect(0, 0, game.getResolutionWidth(), game.getResolutionHeight());
		for(int i=0;i<entities.size();i++){
			entities.get(i).render(g2,getOffset());
		}
	}
	
	public Point getOffset(){
		Point offset =new Point((int)(cameraLocation.x *unitResolution), (int) (cameraLocation.y *unitResolution));
		offset.x-=game.getResolutionWidth()/2;
		offset.y-=game.getResolutionHeight()/2;
		return offset;
	}
	
	public double getWorldDeltaTime(){
		return game.getDeltaTime()/5;
	}

}
