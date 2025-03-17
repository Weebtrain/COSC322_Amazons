
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
	final private boolean humanPlay = true;	//sets human player or ai to run

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;
 
	private int queenIdentity;
	private int[][] gameState = null;	//10x10 array holding the game board. 2 is black queen, 1 is white queen, 3 is arrow
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {				 
    	COSC322Test player = new COSC322Test("WeebTrain2", "COSC322");	//Username display in server, password into server
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
				@Override
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
		int index = 14;	//Room index
		List<Room> rooms = gameClient.getRoomList();
		//gameClient.joinRoom(rooms.get(index).getName());
		
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

		//Message on entering the room that gives board state
		if (messageType.equals(AmazonsGameMessage.GAME_STATE_BOARD)) {
			//Sets up gui and our board matrix
			this.initializeGameBoard((ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.GAME_STATE));
		}
		//Message recieved when game starts
		else if (messageType.equals(AmazonsGameMessage.GAME_ACTION_START)) {
			gameStart(msgDetails);
		}
		//Message recieved when opponent makes a move
		else if (messageType.equals(AmazonsGameMessage.GAME_ACTION_MOVE)) {
			updateGameState(msgDetails, 3-queenIdentity);
			RunPlayer();
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

	//Initializes board state to 10x10 integer matrix for use, also initializes gamegui
	void initializeGameBoard (ArrayList<Integer> board) {
		gamegui.setGameState(board);
		gameState = new int[10][10];
		for (int i = 1; i<11;i++) {
			for (int j = 1; j<11; j++) {
				gameState[i-1][j-1] = board.get((i*11)+j);
			}
		}
	}

	//Updates board matrix
	private void updateGameState (Map<String, Object> msgDetails, int queenNum) {
		gamegui.updateGameState(msgDetails);

		ArrayList<Integer> move = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
		gameState[move.get(0)-1][move.get(1)-1] = 0;
		move = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
		gameState[move.get(0)-1][move.get(1)-1] = queenNum;
		move = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.ARROW_POS);
		gameState[move.get(0)-1][move.get(1)-1] = 3;
		//displayGameStateArray();
	}
	public void updateGameState (int[][] moveMade, int queenNum) {
		gameState[moveMade[0][0] - 1][moveMade[0][1] - 1] = 0;
		gameState[moveMade[1][0] - 1][moveMade[1][1] - 1] = queenNum;
		gameState[moveMade[2][0] - 1][moveMade[2][1] - 1] = 3;
		gamegui.updateGameState(Extras.arrayToArrayList(moveMade[0]), Extras.arrayToArrayList(moveMade[1]), Extras.arrayToArrayList(moveMade[2]));
		//displayGameStateArray();
	}

	void displayGameStateArray() {	//For debugging purposes only, commented out in updateGameState functions
		for (int i = 0; i<10; i++) {
			for (int j = 0; j<10; j++) {
				System.out.print(gameState[i][j]);
			}
			System.out.println();
		}
	}

	void gameStart (Map<String, Object> msgDetails) {
		System.out.println("Black Player: " + (msgDetails.get(AmazonsGameMessage.PLAYER_BLACK)));
		System.out.println("White Player: " + (msgDetails.get(AmazonsGameMessage.PLAYER_WHITE)));
		if (((String)msgDetails.get(AmazonsGameMessage.PLAYER_BLACK)).equals(userName)) {
			queenIdentity = 1;
			//Starts a human player or the ai depending on indication
			RunPlayer();
		} else queenIdentity = 2;
	}

	void RunPlayer () {
		//Starts a human player or the ai depending on indication
		if (humanPlay == true) {
			startHumanPlay();
		} else {
			//run ai
		}
	}

	void startHumanPlay () {
		Thread H = new Thread(new HumanPlayer(this,gameClient,gameState,queenIdentity));
		H.start();
	}
 
}//end of class
