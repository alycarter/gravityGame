package com.alycarter.gravityGame.states.level;

import java.awt.Color;
import java.awt.Graphics;
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
	
	public static int unitResolution =128;
	
	public Level(Game game) {
		this.game=game;
		entities.add(new Planet(this, new Point2D.Double(3, 3), 1));
		entities.add(new Planet(this, new Point2D.Double(8, 3), 1));
		entities.add(new Ship(game, this, new Point2D.Double(2, 3), new Point2D.Double(0, 1), 0.2));
	}

	@Override
	public void update() {
		for(int i=0;i<entities.size();i++){
			entities.get(i).update();
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, game.getResolutionWidth(), game.getResolutionHeight());
		for(int i=0;i<entities.size();i++){
			entities.get(i).render(g);
		}
	}
	
	public double getWorldDeltaTime(){
		return game.getDeltaTime()/5;
	}

}
