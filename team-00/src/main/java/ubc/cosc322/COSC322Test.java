
package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca)
 * Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer{
	private boolean humanPlay = true;

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;
 
	private ArrayList<Integer> gameState = null;
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {				 
    	COSC322Test player = new COSC322Test("WeebTrain", "COSC322");
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                	player.Go();
                }
            });
    	}
    }
	
    /**
     * Any name and passwd 
     * @param userName
      * @param passwd
     */
    public COSC322Test(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	this.gamegui = new BaseGameGUI(this);
    }
 


    @Override
    public void onLogin() {
		int index = 14;
		List<Room> rooms = gameClient.getRoomList();
		//gameClient.joinRoom(rooms.get(index).getName());
		gameClient.joinRoom(rooms.get(index).getName());

    	userName = gameClient.getUserName();
		if(this.gamegui != null){
			gamegui.setRoomInformation(rooms);
		}
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document. 
		if (messageType.equals(AmazonsGameMessage.GAME_STATE_BOARD)) {
			gamegui.setGameState((ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.GAME_STATE));
			System.out.println(msgDetails.get(AmazonsGameMessage.GAME_STATE).toString());
			gameState = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.GAME_STATE);
			Thread H = new Thread(new HumanPlayer(gameClient));
			H.start();
		}
		else if (messageType.equals(AmazonsGameMessage.GAME_ACTION_MOVE)) {
			gamegui.updateGameState(msgDetails);
		}
    	return true;   	
    }
    
    
    @Override
    public String userName() {
    	return userName;
    }

	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		return  this.gamegui;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
    	gameClient = new GameClient(userName, passwd, this);			
	}

	void updateGameState (Map<String, Object> msgDetails) {

	}
 
}//end of class
