package com.alycarter.gravityGame.states.level.entity;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import com.alycarter.gravityGame.states.level.Level;

public class Planet extends Entity {
	private double rotation = (Math.random()*180)-90;
	public Planet(Level level, Double location, double width) {
		super(level, location, new Point2D.Double(0, 0), width, true, false);
	}

	@Override
	public void onUpdate() {
		direction+= rotation*level.getWorldDeltaTime();
	}

	@Override
	public void onRender(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
