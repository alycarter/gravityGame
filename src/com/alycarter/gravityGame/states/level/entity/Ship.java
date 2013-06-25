package com.alycarter.gravityGame.states.level.entity;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import com.alycarter.crabClawEngine.Game;
import com.alycarter.gravityGame.states.level.Level;

public class Ship extends Entity {
	private Game game;
	private double targetDirectionOffset =0;
	public Ship(Game game, Level level, Double location, Double velocity,
			double width) {
		super(level, location, velocity, width, false,true);
		this.game = game;
	}

	@Override
	public void onUpdate() {
		if(game.getControls().isPressed(KeyEvent.VK_G)){
			turnPrograde();
			velocity.x+=angleAsVector(direction).x*level.getWorldDeltaTime();
			velocity.y+=angleAsVector(direction).y*level.getWorldDeltaTime();
		}
		if(game.getControls().isPressed(KeyEvent.VK_H)){
			turnRetrograde();
			velocity.x+=angleAsVector(direction).x*level.getWorldDeltaTime();
			velocity.y+=angleAsVector(direction).y*level.getWorldDeltaTime();
		}
	}
	
	public void turnPrograde(){
		direction=vectorAsAngle(velocity);
	}
	
	public void turnRetrograde(){
		direction=180+vectorAsAngle(velocity);
	}

	@Override
	public void onRender(Graphics g) {
		Point2D.Double lastLocation = location;
		Entity e=this;
		double timeStep =game.getDeltaTime();
		for(int i=0;i<20/timeStep;i++){
			e =simulateLocationAfterTime(e, timeStep);
			g.drawLine((int)(lastLocation.x*Level.unitResolution), (int)(lastLocation.y*Level.unitResolution),
					(int)(e.location.x*Level.unitResolution), (int)(e.location.y*Level.unitResolution));
			lastLocation= new Point2D.Double(e.location.x, e.location.y);
		}
	}

}
