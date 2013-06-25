package com.alycarter.gravityGame.states.level.entity;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import com.alycarter.gravityGame.states.level.Level;

public class Planet extends Entity {

	public Planet(Level level, Double location, double width) {
		super(level, location, new Point2D.Double(0, 0), width, true, false);
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRender(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
