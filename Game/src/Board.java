import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.codejava.sound.AudioPlayer;



public class Board {
	// Columns from top right to bottom right
	// 11 10 09 08 07 06 | 05 04 03 02 01 00 |BLACKBOX(25)
	// 5w          3b    | 5b             2w |
	// ------------------|-------------------|
	// 5b          3w    | 5w             2b |
	// 12 13 14 15 16 17 | 18 19 20 21 22 23 |WHITEBOX (24)
	Column[] columns;

	int whitePiecesInBox;
	int blackPiecesInBox;

	private int whitePiecesInMiddle;
	private int blackPiecesInMiddle;

	public int currentRoll1;
	public int currentRoll2;
	
	public boolean hasInCol;

	public int numberOfMovesLeft;

	public boolean currentDoubles;

	public boolean isWhiteTurn;

	public int sourceColumn;
	
	public boolean whiteHasMiddle;
	public boolean blackHasMiddle;

	public static JButton[] buttonArray;
	Board GameBoard;
	public static JButton blackBoxButton;
	public static JButton whiteBoxButton;
	public static JFrame frame;
	public static JPanel boardPanel;
	public static JPanel leftBoard;
	public static JPanel rightBoard;
	public static JPanel topButtonPanel;
	public static JPanel bottomButtonPanel;
	public static JPanel centerPanel;
	public static JPanel rollPanel;
	public static JPanel eastPanel;
	public static boolean whiteWins;
	public static boolean blackWins;
	
	public Board() {
		columns = new Column[25];
		for (int i = 0; i < 25; i++) {
			// columns[i]=new Column(ROWNUM,#WHITE,#BLACK);
			if (i == 0) {
				columns[i] = new Column(0, 2, 0);
			} else if (i == 5)
				columns[i] = new Column(5, 0, 5);
			else if (i == 7) {
				columns[i] = new Column(7, 0, 3);
			} else if (i == 11) {
				columns[i] = new Column(11, 5, 0);
			} else if (i == 12) {
				columns[i] = new Column(12, 0, 5);
			} else if (i == 16) {
				columns[i] = new Column(16, 3, 0);
			} else if (i == 18) {
				columns[i] = new Column(18, 5, 0);
			} else if (i == 23) {
				columns[i] = new Column(23, 0, 2);
			} else {
				columns[i] = new Column(i, 0, 0);
			}
		}
		//refreshBoard();
	}

	public void run(){
		frame=new JFrame();
		frame.setLayout(new BorderLayout());
		buttonArray=new JButton[24];
		
		whiteWins=false;
		blackWins=false;
		
		topButtonPanel=new JPanel();
		bottomButtonPanel=new JPanel();
		boardPanel=new JPanel();
		leftBoard=new JPanel();
		rightBoard=new JPanel();
		rollPanel=new JPanel();
		centerPanel=new JPanel();
		centerPanel.setPreferredSize(new Dimension(30,150));
		centerPanel.setBackground(new Color(0,0,0));
		
		leftBoard.setLayout(new GridLayout(2,6));
		rightBoard.setLayout(new GridLayout(2,6));
		
		for(int i=0; i<12; i++){
			JLabel labelL=new JLabel();
			JLabel labelR=new JLabel();
			try{
				labelL.setIcon(new ImageIcon(ImageIO.read(new File("upright0.gif"))));
				labelR.setIcon(new ImageIcon(ImageIO.read(new File("upright0.gif"))));
			}catch(Exception e){};
			leftBoard.add(labelL);
			rightBoard.add(labelR);
		}
		eastPanel=new JPanel();
		eastPanel.setPreferredSize(new Dimension(100, 300));
		frame.add(BorderLayout.EAST, eastPanel);
		boardPanel.setLayout(new BorderLayout());
		boardPanel.add(BorderLayout.WEST,leftBoard);
		boardPanel.add(BorderLayout.EAST, rightBoard);
		boardPanel.add(BorderLayout.CENTER, centerPanel);
		frame.add(BorderLayout.CENTER, boardPanel);
		frame.add(BorderLayout.WEST, rollPanel);
		
		
		blackBoxButton=new JButton();
		whiteBoxButton=new JButton();
		blackBoxButton.add(new JLabel("Black Box"));
		whiteBoxButton.add(new JLabel("White Box"));
		topButtonPanel.add(new JLabel("         "));
		topButtonPanel.add(new JLabel("         "));
		for(int i=11; i>=0; i--){
			buttonArray[i]=new JButton();
			buttonArray[i].setPreferredSize(new Dimension(69,25));
			topButtonPanel.add(buttonArray[i]);
			if(i==6){
				topButtonPanel.add(new JLabel("        "));
				topButtonPanel.add(new JLabel("         "));
			}
			buttonArray[i].add(new JLabel(""+i));
			//topButtonPanel.add(new JLabel(" "));
		}
		bottomButtonPanel.add(new JLabel("      "));
		for(int i=12;i<24; i++){
			buttonArray[i]=new JButton();
			buttonArray[i].setPreferredSize(new Dimension(69,25));
			bottomButtonPanel.add(buttonArray[i]);
			buttonArray[i].add(new JLabel(""+i));
			if(i==17){
				bottomButtonPanel.add(new JLabel("        "));
				
			}
		}
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BorderLayout());
		
		/*eastPanel.setLayout(new BorderLayout());
		JPanel eastMidPanel=new JPanel();
		eastMidPanel.setPreferredSize(new Dimension(200, 200));
		eastPanel.add(BorderLayout.CENTER, eastMidPanel);
		JPanel southPanel=new JPanel();
		southPanel.add(whiteBoxButton);*/
		
		
	eastPanel.add(blackBoxButton);
	eastPanel.add(whiteBoxButton);
		topPanel.add(topButtonPanel);
		
		
		frame.add(BorderLayout.NORTH,topButtonPanel);
		frame.add(BorderLayout.SOUTH, bottomButtonPanel);
		
		JButton noMoves=new JButton();
		noMoves.add(new JLabel("End Turn"));
	//	southPanel.add(noMoves);
		//eastPanel.add(BorderLayout.SOUTH, southPanel);
		
		
		
		class AddButtonXListener implements ActionListener
	    {
	       public void actionPerformed(ActionEvent e)
	       {
	          int rowNumber = Integer.parseInt(e.getActionCommand().substring(0,2));
	          if(isWhiteTurn&&whitePiecesInMiddle>0){
	        	  whiteMoveFromMiddle(rowNumber);
	          }
	          else if(!isWhiteTurn&&blackPiecesInMiddle>0){
	        	  blackMoveFromMiddle(rowNumber);
	          }
	          else if(hasInCol){
	        	  if(isWhiteTurn){
	        		  if(isLegalWhiteMove(sourceColumn, rowNumber)){
	        			  whiteMove(sourceColumn, rowNumber);
	        		  }
	        		  else{
	        			  sourceColumn=rowNumber;
	        		  }
	        	  }
	        	  else{
	        		  if(isLegalBlackMove(sourceColumn, rowNumber)){
	        		  	  blackMove(sourceColumn, rowNumber);
	        		  }
	        		  else{
	        			  sourceColumn=rowNumber;
	        		  }
	        	  }
	          }
	          else{
	        	  sourceColumn=rowNumber;
	        	  hasInCol=true;
	          }
	          
	       }
	    }
		class AddWhiteBoxListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				if(hasInCol){
					if(isWhiteTurn){
						
						whiteToBox(sourceColumn);
						if(whitePiecesInBox==15)
						{
							whiteWins=true;
							refreshBoard();
						}
						refreshBoard();
					}
				}
			}
		}
		class AddBlackBoxListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				if(hasInCol&&!isWhiteTurn){
					blackToBox(sourceColumn);
					if(blackPiecesInBox==15){
						blackWins=true;
						refreshBoard();
					}
					refreshBoard();
				}
			}
		}
		class AddNoMovesListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				newTurn();
			}
		}
		noMoves.addActionListener(new AddNoMovesListener());
		ActionListener listenerX = new AddButtonXListener();
	      for (int i = 0; i < 24; i++){
	         buttonArray[i].addActionListener(listenerX);
	         if(i<10)
	        	 buttonArray[i].setActionCommand("0"+i);
	         else
	        	 buttonArray[i].setActionCommand(""+i);
	      }
	      whiteBoxButton.addActionListener(new AddWhiteBoxListener());
	      blackBoxButton.addActionListener(new AddBlackBoxListener());
	      eastPanel.add(noMoves);
	      refreshBoard();
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      newTurn();
	      frame.setVisible(true);
	      
	      
	}

	public void whiteMoveFromMiddle(int outCol){
		if((outCol+1)==currentRoll1){
			if (columns[outCol].numBlackPieces < 2) {
				if(columns[outCol].numBlackPieces==1){
					columns[outCol].numBlackPieces--;
					blackPiecesInMiddle++;
				}
				System.out.println("whiteFromMiddle 1");
				columns[outCol].numWhitePieces++;
				whitePiecesInMiddle--;
				hasInCol = false;
				sourceColumn = 100;
				if (numberOfMovesLeft < 3) {
					currentRoll1 = 0;
				}
				numberOfMovesLeft--;
			}
		} else if ((outCol + 1) == currentRoll2) {
			if (columns[outCol].numBlackPieces < 2) {
				if (columns[outCol].numBlackPieces == 1) {
					columns[outCol].numBlackPieces--;
					blackPiecesInMiddle++;
				}
				System.out.println("whiteFromMiddle 1");
				columns[outCol].numWhitePieces++;
				whitePiecesInMiddle--;
				hasInCol = false;
				sourceColumn = 100;
				if (numberOfMovesLeft < 3) {
					currentRoll2 = 0;
				}
				numberOfMovesLeft--;
			}
		}
		refreshBoard();
	}

	public void blackMoveFromMiddle(int outCol){
		if((24-outCol)==currentRoll1){
			if (columns[outCol].numWhitePieces < 2) {
				
				if (columns[outCol].numWhitePieces == 1) {
					columns[outCol].numWhitePieces--;
					whitePiecesInMiddle++;
				}
				columns[outCol].numBlackPieces++;
				blackPiecesInMiddle--;
				hasInCol = false;
				sourceColumn = 100;
				if (numberOfMovesLeft < 3) {
					currentRoll1 = 0;
				}
				numberOfMovesLeft--;
			}
		}
		else if((24-outCol)==currentRoll2){
			if (columns[outCol].numWhitePieces < 2) {
				
				if(columns[outCol].numWhitePieces==1){
					columns[outCol].numWhitePieces--;
					whitePiecesInMiddle++;
				}
				columns[outCol].numBlackPieces++;
				blackPiecesInMiddle--;
				hasInCol = false;
				sourceColumn = 100;
				if (numberOfMovesLeft < 3) {
					currentRoll2 = 0;
				}
				numberOfMovesLeft--;
			}
		}
		refreshBoard();
	}
		
	public void whiteMove(int inCol, int outCol) {
		if (inCol == 200) {
			
			if (outCol - 1 == currentRoll1) {
				if (columns[outCol].numBlackPieces < 2) {
					columns[outCol].numWhitePieces++;
					whitePiecesInMiddle--;
					if (numberOfMovesLeft < 3) {
						currentRoll1 = 0;
					}
					numberOfMovesLeft--;
				}
			} else if (outCol - 1 == currentRoll2) {
				if (columns[outCol].numBlackPieces < 2) {
					columns[outCol].numWhitePieces++;
					whitePiecesInMiddle--;
					if (numberOfMovesLeft < 3) {
						currentRoll2 = 0;
					}
					numberOfMovesLeft--;
				}
			}
		} else {
			if (outCol - inCol == currentRoll1) {
				if (columns[outCol].numBlackPieces == 1) {
					columns[outCol].numBlackPieces--;
					columns[inCol].numWhitePieces--;
					blackPiecesInMiddle++;
					columns[outCol].numWhitePieces++;
				} else if (columns[outCol].numBlackPieces > 1) {
					
				} else {
					columns[inCol].numWhitePieces--;
					columns[outCol].numWhitePieces++;
				}
				if (numberOfMovesLeft < 3) {
					currentRoll1 = 0;
				}
				numberOfMovesLeft--;
			} else if (outCol - inCol == currentRoll2) {

				if (columns[outCol].numBlackPieces == 1) {
					columns[outCol].numBlackPieces--;
					columns[inCol].numWhitePieces--;
					blackPiecesInMiddle++;
					columns[outCol].numWhitePieces++;
				} else if (columns[outCol].numBlackPieces > 1) {
					
				} else {
					columns[inCol].numWhitePieces--;
					columns[outCol].numWhitePieces++;
				}
				if (numberOfMovesLeft < 3) {
					currentRoll2 = 0;
				}
				numberOfMovesLeft--;
			} else { // Attempted move does not equal roll
				
			}
		}
		sourceColumn=100;
		hasInCol=false;
		if(numberOfMovesLeft==0)
			newTurn();
		else
			refreshBoard();
	}
	
	public void blackMove(int inCol, int outCol) {
		if (inCol == 200) {
			if (24 - outCol == currentRoll1) {
				if (columns[outCol].numWhitePieces < 2) {
					columns[outCol].numBlackPieces++;
					blackPiecesInMiddle--;
					if (numberOfMovesLeft < 3) {
						currentRoll1 = 0;
					}
					numberOfMovesLeft--;
				}
				 if(columns[outCol].numWhitePieces==1){
					columns[outCol].numWhitePieces=0;
					whitePiecesInMiddle++;
					
				}
			} else if (24 - outCol == currentRoll2) {
				if (columns[outCol].numWhitePieces < 2) {
					columns[outCol].numBlackPieces++;
					blackPiecesInMiddle--;
					if (numberOfMovesLeft < 3) {
						currentRoll2 = 0;
					}
					numberOfMovesLeft--;
				}
				if(columns[outCol].numWhitePieces==1){
					columns[outCol].numWhitePieces=0;
					whitePiecesInMiddle++;
				}
			}

		}

		else {
			// establishing which roll is used
			if(outCol==-1){
				//black box index=-1
				if(inCol+1==currentRoll1){
					columns[inCol].numBlackPieces--;
					blackPiecesInBox++;
					if(numberOfMovesLeft<3){
						currentRoll1=0;
					}
					numberOfMovesLeft--;
					hasInCol=false;
					
					
				}
				else if(inCol+1==currentRoll2){
					columns[inCol].numBlackPieces--;
					blackPiecesInBox++;
					if(numberOfMovesLeft<3)
						currentRoll2=0;
					numberOfMovesLeft--;
					hasInCol=false;
				}
			}
			else if (inCol - outCol == currentRoll1) {
				if (columns[outCol].numWhitePieces == 1) {

					columns[outCol].numWhitePieces--;
					columns[inCol].numBlackPieces--;
					whitePiecesInMiddle++;
					columns[outCol].numBlackPieces++;
					if(numberOfMovesLeft<3){
						currentRoll1=0;
					}
					numberOfMovesLeft--;
					hasInCol=false;

				} else if (columns[outCol].numWhitePieces > 1) {
				} else {
					columns[inCol].numBlackPieces--;
					columns[outCol].numBlackPieces++;
					if(numberOfMovesLeft<3){
						currentRoll1=0;
					}
					numberOfMovesLeft--;
					hasInCol=false;
				}
				
				hasInCol=false;
				
			} else if (inCol - outCol == currentRoll2) {

				if (columns[outCol].numWhitePieces == 1) {
					columns[outCol].numWhitePieces--;
					columns[inCol].numBlackPieces--;
					whitePiecesInMiddle++;
					columns[outCol].numBlackPieces++;
					if(outCol==25){
						blackPiecesInBox++;
					}
					if (numberOfMovesLeft < 3) {
						currentRoll2 = 0;
					}
					numberOfMovesLeft--;
				} else if (columns[outCol].numWhitePieces > 1) {
					
				} else {
					columns[inCol].numBlackPieces--;
					columns[outCol].numBlackPieces++;
					if (numberOfMovesLeft < 3) {
						currentRoll2 = 0;
					}
					numberOfMovesLeft--;
				}
				hasInCol=false;
			} else { // Attempted move does not equal roll
			}
		}
		sourceColumn=100;
		hasInCol=false;
		if(numberOfMovesLeft==0)
			newTurn();
		else
			refreshBoard();
	}

	public void whiteToBox(int inCol){
		boolean canMoveToBox=true;
		int largerRoll=0;
		if(currentRoll1>currentRoll2){
			largerRoll=currentRoll1;
		}
		else{
			largerRoll=currentRoll2;
		}
		for(int i=0; i<18; i++){
			if(columns[i].numWhitePieces>0){
				canMoveToBox=false;
				
			}
		}
		
		if(whitePiecesInMiddle>0){
			canMoveToBox=false;
		}
		boolean firstFewEmpty=true;
		if(canMoveToBox){
			if(inCol+currentRoll1==24){
			
				columns[inCol].numWhitePieces--;
				whitePiecesInBox++;
				if(numberOfMovesLeft<3){
					currentRoll1=0;
				}
				numberOfMovesLeft--;
			}
			else if(inCol+currentRoll2==24){
			
				columns[inCol].numWhitePieces--;
				whitePiecesInBox++;
				if (numberOfMovesLeft < 3) {
					currentRoll2 = 0;
				}
				numberOfMovesLeft--;

			}

			else if (inCol + largerRoll > 24) {
				for (int i = 18; i < inCol; i++) {
					if (columns[i].numWhitePieces > 0) {
						firstFewEmpty = false;
					}
				}
				if (firstFewEmpty) {
					columns[inCol].numWhitePieces--;
					whitePiecesInBox++;
					if (numberOfMovesLeft < 3) {
						if (largerRoll == currentRoll1) {
							currentRoll1 = 0;
						} else if (largerRoll == currentRoll2) {
							currentRoll2 = 0;
						}
					}
					numberOfMovesLeft--;
				}
			}

		}
		hasInCol = false;
		sourceColumn = 100;
		refreshBoard();
		if (numberOfMovesLeft == 0) {
			newTurn();
		}
	}
	
	public void blackToBox(int inCol){
		boolean canMoveToBox=true;
		int largerRoll=0;
		if(currentRoll1>currentRoll2){
			largerRoll=currentRoll1;
		}
		else{
			largerRoll=currentRoll2;
		}
		for(int i=23; i>5; i--){
			if(columns[i].numBlackPieces>0){
				canMoveToBox=false;
			}
		}
		if(blackPiecesInMiddle>0){
			canMoveToBox=false;
		}
		boolean firstFewEmpty=true;
		if(canMoveToBox){
			if(inCol-currentRoll1==-1){
				columns[inCol].numBlackPieces--;
				blackPiecesInBox++;
				if(numberOfMovesLeft<3){
					currentRoll1=0;
				}
				numberOfMovesLeft--;
			}
			else if(inCol-currentRoll2==-1){
				columns[inCol].numBlackPieces--;
				blackPiecesInBox++;
				if(numberOfMovesLeft<3){
					currentRoll2=0;
				}
				numberOfMovesLeft--;
			}
			else if(inCol-largerRoll<(-1)){
				for(int i=5; i>inCol; i--){
					if(columns[i].numBlackPieces>0){
						firstFewEmpty=false;
					}
				}
				if(firstFewEmpty){
					columns[inCol].numBlackPieces--;
					blackPiecesInBox++;
					if(numberOfMovesLeft<3){
						if(largerRoll==currentRoll1){
							currentRoll1=0;
						}
						else if(largerRoll==currentRoll2){
							currentRoll2=0;
						}
					}
					numberOfMovesLeft--;
				}
			}
		}
		hasInCol=false;
		sourceColumn=100;
		refreshBoard();
		if(numberOfMovesLeft==0){
			newTurn();
			refreshBoard();
		}
	}

	public boolean isLegalWhiteMove(int inCol, int outCol){
		if(inCol==200){
			
			if(outCol-1==currentRoll1||outCol-1==currentRoll2){
				if(columns[outCol].numBlackPieces<2){
					return true;
				}
	
			}
			return false;
		}
		if(outCol<0||outCol>25){
			return false;
		}
		if(outCol==24){
			for(int i=0; i<18; i++){
				if(columns[i].numWhitePieces>0){
					return false;
				}
			}
		}
		if(inCol!=200&&columns[inCol].numWhitePieces<1){
			return false;
		}
		if(outCol-inCol!=currentRoll1&&outCol-inCol!=currentRoll2||outCol-inCol==0){
			//move is not legal
			return false;
		}
		else if(outCol-inCol==currentRoll1  &&  columns[outCol].numBlackPieces>2  ||  outCol-inCol==currentRoll2 && columns[outCol].numBlackPieces>2){
			return false;
		}
		else if(inCol!=200&&whitePiecesInMiddle>0){
			return false;
		}
		
		else{return true;}
	}

	public boolean isLegalBlackMove(int inCol, int outCol){
		if(inCol==200){
			if(24-outCol==currentRoll1||24-outCol==currentRoll2){
				return true;
			}
		}
		if(outCol==-1){
			for(int i=23; i>5; i--){
				if(columns[i].numBlackPieces>0){
					return false;
				}
			}
		}
		if(inCol-outCol!=currentRoll1&&inCol-outCol!=currentRoll2||inCol-outCol==0){
			//move is not legal
			return false;
		}
		else if(inCol-outCol==currentRoll1  &&  columns[outCol].numWhitePieces>2  ||  inCol-outCol==currentRoll2 && columns[outCol].numWhitePieces>2){
			return false;
		}
		else if(inCol!=200&&blackPiecesInMiddle>0){
			return false;
		}
		
		else{return true;}
	}
	
	public boolean areNoLegalWhiteMoves(){
		int largerRoll;
		if(whitePiecesInMiddle>0){
			if(columns[currentRoll1-1].numBlackPieces<2||columns[currentRoll2-1].numBlackPieces<2){
				return false;
			}
		}
		if(currentRoll1>currentRoll2){
			largerRoll=currentRoll1;
		}
		else{
			largerRoll=currentRoll2;;
		}
		for(int i=0; i<24-largerRoll; i++){
			if(isLegalWhiteMove(i,i+currentRoll1)||isLegalWhiteMove(i, i+currentRoll2)){
				return false;
			}
		}
		return true;
	}
	
	public boolean areNoLegalBlackMoves(){
		if(blackPiecesInMiddle>0){
			if(columns[24-currentRoll1].numWhitePieces<2||columns[24-currentRoll2].numWhitePieces<2){
				return false;
			}
			else{return true;}
		}
		int largerRoll;
		if(currentRoll1>currentRoll2){
			largerRoll=currentRoll1;
		}
		else{
			largerRoll=currentRoll2;;
		}
		for(int i=24; i>largerRoll; i--){
			if(isLegalBlackMove(i,i-currentRoll1)||isLegalBlackMove(i, i-currentRoll2)){
				return false;
			}
		}
		return true;
	}
	
	public void newTurn() {
		getRoll();
		refreshBoard();
		if (whitePiecesInBox >= 15) {
			whiteWins=true;
			refreshBoard();
		} else if (blackPiecesInBox >= 15) {
			blackWins=true;
		}
		
		if (isWhiteTurn) {
			isWhiteTurn = false;
			/*if(areNoLegalBlackMoves()){
				System.out.println("NO LEGAL BLACK MOVES");
				newTurn();
			}*/
			if(blackPiecesInMiddle>0){
				sourceColumn=200;
				
				hasInCol=true;
			}
			
		} else{
			isWhiteTurn = true;
			
			if(whitePiecesInMiddle>0){
				
				sourceColumn=200;
				hasInCol=true;
			}
			
		}
		refreshBoard();
		
		
	}

	public void getRoll() {
		// roll dice
		// wait for player to move
		//
		Random r=new Random();
		currentRoll1 = r.nextInt(6)+1;
	
		currentRoll2 = r.nextInt(6)+1;;
		
		
		//AudioPlayer player=new AudioPlayer();
		//player.play("CASHREG.wav");
		if (currentRoll1 == currentRoll2) {
			 currentDoubles = true;
			numberOfMovesLeft = 4;
		} else {
			numberOfMovesLeft = 2;
		}
		refreshBoard();

	}
	
	public void refreshBoard(){
		
		JPanel leftPanel=new JPanel();
		GridLayout left = new GridLayout(2, 6);
		leftPanel.setLayout(left);
		for(int i =11; i>=6; i--){
			if(columns[i].numWhitePieces>0){
				JLabel label=new JLabel();
				int num=columns[i].numWhitePieces;
				try{
				label.setIcon(new ImageIcon(ImageIO.read(new File("invert"+num+"w.gif"))));
				}catch(Exception e){}
				leftPanel.add(label);
				
			}
			else if(columns[i].numBlackPieces>0){
				JLabel label=new JLabel();
				int num=columns[i].numBlackPieces;
				try{
				label.setIcon(new ImageIcon(ImageIO.read(new File("invert"+num+"b.gif"))));
				}catch(Exception e){}
				leftPanel.add(label);
			}
			else{
				JLabel label =new JLabel();
				try{
					label.setIcon(new ImageIcon(ImageIO.read(new File("invert0.gif"))));
				}
				catch(Exception e){}
				leftPanel.add(label);
			}
		}
		for(int i =12; i<18; i++){
			if(columns[i].numWhitePieces>0){
				JLabel label=new JLabel();
				int num=columns[i].numWhitePieces;
				try{
				label.setIcon(new ImageIcon(ImageIO.read(new File("upright"+num+"w.gif"))));
				}catch(Exception e){}
				leftPanel.add(label);
			}
			else if(columns[i].numBlackPieces>0){
				JLabel label=new JLabel();
				int num=columns[i].numBlackPieces;
				try{
				label.setIcon(new ImageIcon(ImageIO.read(new File("upright"+num+"b.gif"))));
				}catch(Exception e){}
				leftPanel.add(label);
			}
			else{
				JLabel label =new JLabel();
				try{
					label.setIcon(new ImageIcon(ImageIO.read(new File("upright0.gif"))));
				}
				catch(Exception e){}
				leftPanel.add(label);
			}
		}
		
		JPanel rightPanel=new JPanel();
		GridLayout right=new GridLayout(2, 6);
		rightPanel.setLayout(right);
		//rightPanel.setBackGround
		
		
		for(int i =5; i>=0; i--){
			if(columns[i].numWhitePieces>0){
				JLabel label=new JLabel();
				int num=columns[i].numWhitePieces;
				try{
				label.setIcon(new ImageIcon(ImageIO.read(new File("invert"+num+"w.gif"))));
				}catch(Exception e){}
				rightPanel.add(label);
			}
			else if(columns[i].numBlackPieces>0){
				JLabel label=new JLabel();
				int num=columns[i].numBlackPieces;
				try{
				label.setIcon(new ImageIcon(ImageIO.read(new File("invert"+num+"b.gif"))));
				}catch(Exception e){}
				rightPanel.add(label);
			}
			else{
				JLabel label =new JLabel();
				try{
					label.setIcon(new ImageIcon(ImageIO.read(new File("invert0.gif"))));
				}
				catch(Exception e){}
				rightPanel.add(label);
			}
		}
		for(int i =18; i<24; i++){
				if(columns[i].numWhitePieces>0){
					JLabel label=new JLabel();
					int num=columns[i].numWhitePieces;
					try{
					label.setIcon(new ImageIcon(ImageIO.read(new File("upright"+num+"w.gif"))));
					}catch(Exception e){}
					rightPanel.add(label);
				}
				else if(columns[i].numBlackPieces>0){
					JLabel label=new JLabel();
					int num=columns[i].numBlackPieces;
					try{
					label.setIcon(new ImageIcon(ImageIO.read(new File("upright"+num+"b.gif"))));
					}catch(Exception e){}
					rightPanel.add(label);
				}
			else{
				JLabel label =new JLabel();
				try{
					label.setIcon(new ImageIcon(ImageIO.read(new File("upright0.gif"))));
				}
				catch(Exception e){}
				rightPanel.add(label);
			}
		}
		leftBoard=leftPanel;
		rightBoard=rightPanel;
		JPanel newRollPanel=new JPanel();
		newRollPanel.setLayout(new FlowLayout());
		newRollPanel.setPreferredSize(new Dimension(175, 250));
		//newRollPanel.add(new JLabel("CURRENT ROLLS:                 "));
		//newRollPanel.add(new JLabel("     "+currentRoll1));
	//	newRollPanel.add(new JLabel("     "+currentRoll2));
		JLabel die1=new JLabel();
		JLabel die2=new JLabel();
		try{
		if(numberOfMovesLeft>2){
			for(int i=0; i<numberOfMovesLeft; i++){
				JLabel a=new JLabel();
				a.setIcon(new ImageIcon(ImageIO.read(new File("die"+currentRoll1+".png"))));
				newRollPanel.add(a);
			}
		}
		else{
			if(currentRoll1!=0){
				die1.setIcon(new ImageIcon(ImageIO.read(new File("die"+currentRoll1+".png"))));
				newRollPanel.add(die1);
				}
			if(currentRoll2!=0){
				die2.setIcon(new ImageIcon(ImageIO.read(new File("die"+currentRoll2+".png"))));
				newRollPanel.add(die2);
			}
			
			
		}}catch(Exception e){}
		newRollPanel.add(new JLabel("Number of Moves Left: "+numberOfMovesLeft));
		newRollPanel.add(new JLabel("Current Turn:"));
		
		
		if(isWhiteTurn)
			newRollPanel.add(new JLabel("White"));
		else
			newRollPanel.add(new JLabel("Black"));
		newRollPanel.add(new JLabel("White in Middle:  "+whitePiecesInMiddle));
		newRollPanel.add(new JLabel("Black in Middle:  "+blackPiecesInMiddle));
		newRollPanel.add(new JLabel("White Box: "+whitePiecesInBox));
		newRollPanel.add(new JLabel("Black Box: "+ blackPiecesInBox));
		if(whiteWins){
			JLabel winLabel=new JLabel("WHITE WINS!");
			winLabel.setFont(new Font("Times New Roman", 10, 24));
			newRollPanel.add(winLabel);
		}
		if(blackWins){
			JLabel winLabel=new JLabel("BLACK WINS!");
			winLabel.setFont(new Font("Times New Roman", 10, 24));
			newRollPanel.add(winLabel);
		}
		frame.add(BorderLayout.WEST, newRollPanel);
		boardPanel.add(BorderLayout.WEST, leftPanel);
		boardPanel.add(BorderLayout.EAST, rightPanel);
		
		frame.pack();
	}

}	