
import javax.swing.JButton;
import javax.swing.JFrame;

import javax.swing.JPanel;


public class GameMain {
	public static JButton[] buttonArray;
	static Board GameBoard;
	public static JButton blackBoxButton;
	public static JButton whiteBoxButton;
	public static JFrame frame;
	public static JPanel boardPanel;
	public static JPanel leftBoard;
	public static JPanel rightBoard;
	public static JPanel topButtonPanel;
	public static JPanel bottomButtonPanel;
	public static JPanel centerPanel;

	public static void main(String[] args){
		
		GameBoard=new Board();
		GameBoard.run();
		GameBoard.refreshBoard();
		
		/*GameBoard.blackPiecesInBox=12;
		GameBoard.whitePiecesInBox=12;
		for(int i=0; i<24; i++){
			GameBoard.columns[i]=new Column(0,0,0);
		}
		for(int i=23; i>19; i--){
			GameBoard.columns[i].numWhitePieces=3;
		}
		for(int i=0; i<4; i++){
			GameBoard.columns[i].numBlackPieces=3;
		}*/
		GameBoard.refreshBoard();
		
		
		
	}
}
