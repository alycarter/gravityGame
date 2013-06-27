
package com.alycarter.gravityGame.states.level.entity;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import com.alycarter.gravityGame.states.level.Level;

public class Planet extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double rotation = (Math.random()*180)-90;
	
	public Planet(Level level, Double location, double width) {
		super(Entity.PLANET,level, location, new Point2D.Double(0, 0), width, true, false);
	}

	@Override
	public void onUpdate() {
		direction+= rotation*level.getWorldDeltaTime();
	}

	@Override
	public void onRender(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveCommand(int command) {
		// TODO Auto-generated method stub
		
	}

}
