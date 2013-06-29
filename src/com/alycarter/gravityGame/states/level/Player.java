package com.alycarter.gravityGame.states.level;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.alycarter.crabClawEngine.Game;
import com.alycarter.gravityGame.states.level.entity.Entity;
import com.alycarter.gravityGame.states.level.entity.Ship;

public class Player {
	public Game game;
	public Level level;
	public Socket commandSocket;
	public ServerSocket server;
	public BufferedReader commandInput;
	public BufferedWriter commandOutput;
	public Socket entitySocket;
	public ObjectInputStream entityInput;
	public ObjectOutputStream entityOutput;
	private static String ip ="localhost";
	private static int port = 1234;
	private double lastSent=0;
	public int team;
	
	public Player(Level level, Game game) {
		this.level=level;
		this.game=game;
		try {
			commandSocket = new Socket(ip, port);
			entitySocket = new Socket(ip, port);
			System.out.println("connected to server");
			commandInput=new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
			commandOutput = new BufferedWriter(new PrintWriter(commandSocket.getOutputStream()));
			entityInput= new ObjectInputStream(entitySocket.getInputStream());
			team=2;
		} catch (Exception e) {
			System.out.println("no server found setting up new server");
			level.host=true;
			try {
				server = new ServerSocket(port);
				System.out.println("new server set up waiting for connection");
				team=2;
				commandSocket = server.accept();
				entitySocket = server.accept();
				commandInput=new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
				commandOutput = new BufferedWriter(new PrintWriter(commandSocket.getOutputStream()));
				entityOutput= new ObjectOutputStream(entitySocket.getOutputStream());
				System.out.println("server has connected to a client");
			} catch (Exception e1) {
				e1.printStackTrace();
				System.out.println("failed to set up a server");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void update(){
		try {
			while(commandInput.ready()){
				level.sendCommand(commandInput.read(),commandInput.read());
			}
		} catch (IOException e1) {e1.printStackTrace();}
		if(level.host){
			if(lastSent>1/10){
				try {
					entityOutput.writeObject(level.entities);
					entityOutput.flush();
					entityOutput.reset();
					
				} catch (IOException e) {e.printStackTrace();}
			}
			lastSent+=game.getDeltaTime();
		}else{
			try {
				level.entities=(ArrayList<Entity>)entityInput.readObject();
			} catch (ClassNotFoundException e) {e.printStackTrace();
			} catch (IOException e) {e.printStackTrace();}
		}
		checkControls();
	}
	
	public void issueCommand(int id, int command){
		level.sendCommand(id,command);
		try {
			commandOutput.write(id);
			commandOutput.write(command);
			commandOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void checkControls(){
		if(level.selectedShip !=null){
			if(game.getControls().isPressed(KeyEvent.VK_G)){
				issueCommand(level.selectedShip,Ship.TURNPROGRADE);
			}
			if(game.getControls().isPressed(KeyEvent.VK_H)){
				issueCommand(level.selectedShip,Ship.TURNRETROGRADE);
			}
			if(game.getControls().isPressed(KeyEvent.VK_SPACE)){
				issueCommand(level.selectedShip,Ship.BURN);
			}
			if(game.getControls().isTyped(KeyEvent.VK_SPACE)){
				issueCommand(level.selectedShip,Ship.STOPBURN);
			}
		}
	}

}
