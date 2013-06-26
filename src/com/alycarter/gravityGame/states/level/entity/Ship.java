package com.alycarter.gravityGame.states.level.entity;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import com.alycarter.gravityGame.states.level.Level;

public class Ship extends Entity {
	
	private double targetOffset =0;
	private double currentOffset = 0;
	private boolean burning=false;
	private double thrust=0;
	public Ship(Level level, Double location, Double velocity, double width) {
		super(level, location, velocity, width, false,true);
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
		}else{
			thrust =0;
		}
		burning = false;
		for(int i=0;i<level.planets.size();i++){
			Planet p = level.planets.get(i);
			if(location.distance(p.location)<p.width/2){
				level.entities.remove(this);
				level.ships.remove(this);
			}
		}
	}
	
	public void turnPrograde(){
		targetOffset=0;
	}
	
	public void turnRetrograde(){
		targetOffset=180;
	}
	
	public void burn(){
		if(currentOffset==targetOffset){
			burning = true;
		}
	}

	@Override
	public void onRender(Graphics g) {
		if(level.selectedShip==this){
			Point2D.Double lastLocation = location;
			Entity e=this;
			double timeStep =0.004;
			for(int i=0;i<10/timeStep;i++){
				e =simulateLocationAfterTime(e, timeStep);
				g.drawLine((int)(lastLocation.x*Level.unitResolution)-level.getOffset().x, (int)(lastLocation.y*Level.unitResolution)-level.getOffset().y,
						(int)(e.location.x*Level.unitResolution)-level.getOffset().x, (int)(e.location.y*Level.unitResolution)-level.getOffset().y);
				lastLocation= new Point2D.Double(e.location.x, e.location.y);
			}
		}
	}

}
