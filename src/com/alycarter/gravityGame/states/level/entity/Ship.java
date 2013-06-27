package com.alycarter.gravityGame.states.level.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.Serializable;

import com.alycarter.gravityGame.states.level.Level;

public class Ship extends Entity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double targetOffset =0;
	private double currentOffset = 0;
	private boolean burning=false;
	private double thrust=0;
	
	public static final int TURNPROGRADE=0;
	public static final int TURNRETROGRADE=1;
	public static final int BURN=3;
	public static final int STOPBURN=2;
	
	public Ship(Level level, Double location, Double velocity, double width) {
		super(Entity.SHIP,level, location, velocity, width, false,true);
	}

	@Override
	public void onUpdate() {
		direction=vectorAsAngle(velocity)+currentOffset;
		double turnRate = level.getWorldDeltaTime()*360;
		if(Math.abs(targetOffset-currentOffset)<turnRate){
			currentOffset=targetOffset;
		}else{
			if(targetOffset>currentOffset){
				currentOffset+=turnRate;
			}else{
				if(targetOffset<currentOffset){
					currentOffset-=turnRate;
				}
			}
		}
		if(burning){
			thrust+=level.getWorldDeltaTime()*5;
			if(thrust>1){
				thrust=1;
			}
			velocity.x+=Entity.angleAsVector(direction).x*level.getWorldDeltaTime()*thrust;
			velocity.y+=Entity.angleAsVector(direction).y*level.getWorldDeltaTime()*thrust;
		}
		for(int i=0;i<level.entities.size();i++){
			Entity p = level.entities.get(i);
			if(p.entityType==Entity.PLANET && location.distance(p.location)<p.width/2){
				level.entities.remove(this);
			}
		}
	}
	
	private void turnPrograde(){
		targetOffset=0;
	}
	
	private void turnRetrograde(){
		targetOffset=180;
	}
	
	private void burn(){
		if(currentOffset==targetOffset){
			burning = true;
		}
	}
	
	private void stopBurn(){
		burning=false;
		thrust=0;
	}

	@Override
	public void onRender(Graphics g) {
		if(level.selectedShip!=null){
			if(level.selectedShip.intValue()==id){
				Point2D.Double lastLocation = location;
				Point2D.Double lastVelocity = velocity;
				Entity e=this;
				double timeStep =0.004;
				Entity fastest=this;
				Entity slowest=this;
				double angle=0;
				int i=0;
				g.setColor(new Color(0, 0, 255));
				while(i<20/timeStep&&angle<360){
					e =simulateLocationAfterTime(e, timeStep);
					angle+= Math.abs(Math.abs(vectorAsAngle(e.velocity))-Math.abs(vectorAsAngle(lastVelocity)));
					g.drawLine((int)(lastLocation.x*Level.unitResolution)-level.getOffset().x, (int)(lastLocation.y*Level.unitResolution)-level.getOffset().y,
							(int)(e.location.x*Level.unitResolution)-level.getOffset().x, (int)(e.location.y*Level.unitResolution)-level.getOffset().y);
					lastLocation= new Point2D.Double(e.location.x, e.location.y);
					lastVelocity= new Point2D.Double(e.velocity.x, e.velocity.y);
					if(e.velocity.distance(0, 0)>fastest.velocity.distance(0, 0)){
						fastest=e;
					}else{
						if(e.velocity.distance(0, 0)<slowest.velocity.distance(0, 0)){
							slowest=e;
						}
					}
					i++;
				}
				g.setColor(Color.RED);
				g.drawLine((int)(fastest.location.x*Level.unitResolution)-level.getOffset().x, (int)(fastest.location.y*Level.unitResolution)-level.getOffset().y,
						(int)((fastest.location.x+0.25)*Level.unitResolution)-level.getOffset().x, (int)(fastest.location.y*Level.unitResolution)-level.getOffset().y);
				g.setColor(Color.GREEN);
				g.drawLine((int)(slowest.location.x*Level.unitResolution)-level.getOffset().x, (int)(slowest.location.y*Level.unitResolution)-level.getOffset().y,
						(int)((slowest.location.x+0.25)*Level.unitResolution)-level.getOffset().x, (int)(slowest.location.y*Level.unitResolution)-level.getOffset().y);
			}
		}
	}

	@Override
	public void giveCommand(int command) {
		switch (command){
		case BURN: burn();
			break;
		case STOPBURN: stopBurn();
			break;
		case TURNPROGRADE:turnPrograde();
			break;
		case TURNRETROGRADE: turnRetrograde();
			break;
		default:break;
		}
	}

}
