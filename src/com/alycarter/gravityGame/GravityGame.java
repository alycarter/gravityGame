package com.alycarter.gravityGame;

import java.awt.Color;
import java.awt.Graphics;

import com.alycarter.crabClawEngine.Game;
import com.alycarter.gravityGame.states.level.Level;

public class GravityGame extends Game{

	public GravityGame() {
		super("gravity game", 1280, 720);
	}

	
	@Override
	public void onUpdate() {
		
	}


	@Override
	public void onRender(Graphics g) {
		g.setColor(Color.BLACK);
		
		g.drawString(String.valueOf(getFramesLastSecond()), 0, 10);
		g.drawString(String.valueOf(getUpdatesLastSecond()), 0, 30);
	}


	@Override
	public void onInitialize() {
		setFrameLimit(60);
		stateMachine.push(new Level(this));	
	}

	public static void main(String[] args) {
		GravityGame gravityGame= new GravityGame();
		gravityGame.play();
	}
}

