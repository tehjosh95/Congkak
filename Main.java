package congkak;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class Main extends Application{
	Stage stage;
	Scene gameScene, startPageScene;
	int windowWidth = 450, windowHeight = 350;
    int[][] currentArray = {{4,4,4,4,4,4,4},{4,4,4,4,4,4,4}};
    boolean isPlayer1Turn = true;
    int[] player1Score = {0};
    int[] player2Score = {0};
    int lastRow = 0;
    int lastCol = 0;
    public int enteredColumn = 0;
    public int enteredSeed = 0;
    public static void main(String[] args){   	
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
    	stage = primaryStage;
    	
    	//Start page
    	primaryStage.setTitle("Congkak Master 1.0!");
    	Label startPageLbl = new Label("SELECT DIFFICULTY");
        startPageLbl.setFont(new Font("Impact", 35));
        startPageLbl.setTextFill(Color.web("#17D8B2"));
        
        Button startPageBasicBtn = new Button("Basic Game");
        startPageBasicBtn.setStyle("-fx-pref-width:150px; -fx-font-weight:bold");
        Button startPageCustomBtn = new Button("Custom Game");
        startPageCustomBtn.setStyle("-fx-pref-width:150px; -fx-font-weight:bold");
        Button startPageComputerBtn = new Button("Player vs Node");
        startPageComputerBtn.setStyle("-fx-pref-width:150px; -fx-font-weight:bold");
        Button startPageExitBtn = new Button("Exit :(");
        startPageExitBtn.setStyle("-fx-pref-width:150px; -fx-font-weight:bold");
    	
    	VBox startPageVb = new VBox(startPageLbl, startPageBasicBtn, startPageCustomBtn, startPageComputerBtn, startPageExitBtn);
    	startPageVb.setSpacing(10);
        startPageVb.setPadding(new Insets(20));
        startPageVb.setAlignment(Pos.CENTER);
    	
    	GridPane startPageGp = new GridPane();
    	startPageGp.getChildren().add(startPageVb);
        startPageGp.setStyle("-fx-background:#313335");
        startPageGp.setAlignment(Pos.CENTER);
    	
    	Scene startPageScene = new Scene(startPageGp, windowWidth, windowHeight);
    	
    	//Exit button
    	startPageExitBtn.setOnAction( __ -> {
    		System.out.println("Exit");
       	  	Platform.exit();
       	});
    	
    	//Button linking to Basic Game
    	startPageBasicBtn.setOnAction(__ ->{
            primaryStage.setTitle("Congkak Master 1.0! - Basic Game");
            
            //Menu
            Menu optionMenu = new Menu("Option");

            MenuItem restartMenuItem = new MenuItem("Restart");
            restartMenuItem.setOnAction(e -> {
                System.out.println("Restart!");
                primaryStage.close();
                Platform.runLater( () -> new Main().start( new Stage() ) );
            });

            MenuItem exitMenuItem = new MenuItem("Exit");
            exitMenuItem.setOnAction(e -> {
                System.out.println("Exit");
                Platform.exit();
            });

            optionMenu.getItems().addAll(restartMenuItem, exitMenuItem);
            MenuBar menuBar = new MenuBar();
            menuBar.getMenus().add(optionMenu);
            
            //Game status
            Button statusBtn = new Button();
            statusBtn.setText("PLAYER 1 START");
            statusBtn.setStyle("-fx-pref-width:400px; -fx-font-weight:bold");
            
            //Players' score
            Button player1ScoreBtn = new Button();
            player1ScoreBtn.setStyle("-fx-font-size:16; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height: 150px");
            player1ScoreBtn.setText(" ");
            Button player2ScoreBtn = new Button();
            player2ScoreBtn.setStyle("-fx-font-size:16; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height: 150px");
            player2ScoreBtn.setText(" ");
            
            //Game
            GridPane seedLayoutGp = new GridPane();
            BorderPane gameLayoutBp = new BorderPane();
            
            gameLayoutBp.setTop(menuBar);
            gameLayoutBp.setBottom(statusBtn);
            gameLayoutBp.setLeft(player1ScoreBtn);
            gameLayoutBp.setRight(player2ScoreBtn);
            gameLayoutBp.setCenter(seedLayoutGp);
            
            gameLayoutBp.setPadding(new Insets(0, 0, 30, 0));
            gameLayoutBp.setAlignment(statusBtn, Pos.BOTTOM_CENTER);
            gameLayoutBp.setStyle("-fx-background-color:#313335");
            
            gameScene = new Scene(gameLayoutBp);
            
            //Print seeds
            int count = 0;
            for(int a = 0; a < 2; a++){
                for(int b = 0; b < 7; b++){
                    int row = a;
                    int col = b;
                    count = count + 1;
                    Button seed = new Button();
                    seed.setText(Integer.toString(4));
                    seed.setId("button" + count);
                    seed.setStyle("-fx-font:18 impact; -fx-background-radius:100%; -fx-pref-width:60px; -fx-pref-height:60px");
                    
                    if(row == 1){
                        seed.setDisable(true);
                    }
                    
                    seed.setOnAction(new EventHandler<ActionEvent>(){
                        @Override public void handle(ActionEvent e){
                            if(isPlayer1Turn){
                                player1Move(row, col, currentArray, player1Score, player2Score);
                            }
                            else{
                                player2Move(row, col, currentArray, player1Score, player2Score);
                            }
                            
                            refreshSeedCount(seedLayoutGp, gameLayoutBp);
                            System.out.println("Player 1 score " + player1Score);
                            System.out.println("Player 2 score " + player2Score);
                            
                            for(int i = 0; i < 2; i++){
                                for(int j = 0; j < 7; j++){
                                    System.out.print(currentArray[i][j] + " ");
                                }
                                System.out.println();
                            }
                        }
                    });
                    seedLayoutGp.setPadding(new Insets(20, 30, 60, 30));
                    seedLayoutGp.add(seed, b ,a);
                }
            }
	        primaryStage.setScene(gameScene);
	        primaryStage.show();
    	});
    		
    	//Button linking to Custom Game Menu
    	startPageCustomBtn.setOnAction( __ -> {
    		primaryStage.setTitle("Congkak Master 1.0! - Custom Game");
    		
    		Label customMenuLbl = new Label("CUSTOM GAME");
            customMenuLbl.setFont(new Font("Impact", 30));
            customMenuLbl.setTextFill(Color.web("#17D8B2"));
            
            TextField customMenuColumnNoTf = new TextField ();
            customMenuColumnNoTf.setPromptText("Enter column number");
            TextField customMenuSeedNoTf = new TextField ();
            customMenuSeedNoTf.setPromptText("Enter seed number");
    		
            Button customMenuStartBtn = new Button("START");
            customMenuStartBtn.setStyle("-fx-pref-width:90px; -fx-font-weight:bold");
            Button customMenuExitBtn = new Button("EXIT");
            customMenuExitBtn.setStyle("-fx-pref-width:90px; -fx-font-weight:bold");
    		
            HBox customMenuHb = new HBox(customMenuStartBtn,customMenuExitBtn);
        	customMenuHb.setSpacing(20);
        	customMenuHb.setPadding(new Insets(20));
        	
        	VBox customMenuVb = new VBox(customMenuLbl,customMenuColumnNoTf,customMenuSeedNoTf,customMenuHb);
        	customMenuVb.setSpacing(10);
        	customMenuVb.setPadding(new Insets(20));
        	customMenuVb.setAlignment(Pos.CENTER);
        	
        	GridPane customMenuGp = new GridPane();
            customMenuGp.getChildren().add(customMenuVb);
            customMenuGp.setStyle("-fx-background:#313335");
            customMenuGp.setAlignment(Pos.CENTER);
        	
        	//Exit button
        	customMenuExitBtn.setOnAction( e -> {
                System.out.println("Exit");
                Platform.exit();
            });
        	
        	//Button linking to Custom Game
        	customMenuStartBtn.setOnAction((ActionEvent event) -> {
        	int i;
                int j;
                int x, y;
        		try{
                    i = Integer.parseInt(customMenuColumnNoTf.getText());
                    j = Integer.parseInt(customMenuSeedNoTf.getText());                 
                    enteredColumn = i;
                    enteredSeed = j;
                    currentArray = new int[2][enteredColumn];
                    System.out.println(enteredColumn);
                    for(x = 0; x < 2; x++){
                    	for(y = 0;y < enteredColumn; y++){
                    		currentArray[x][y] = enteredSeed;
                    		System.out.print(currentArray[x][y]);
                    	}
                    	System.out.println();
                    }
                    
                   
                    primaryStage.setTitle("Congkak Master 1.0! - Custom Game");
                    
                    //Menu
                    Menu optionMenu = new Menu("Option");

                    MenuItem restartMenuItem = new MenuItem("Restart");
                    restartMenuItem.setOnAction(e -> {
                        System.out.println("Restart!");
                        primaryStage.close();
                        Platform.runLater( () -> new Main().start( new Stage() ) );
                    });

                    MenuItem exitMenuItem = new MenuItem("Exit");
                    exitMenuItem.setOnAction(e -> {
                        System.out.println("Exit");
                        Platform.exit();
                    });

                    optionMenu.getItems().addAll(restartMenuItem, exitMenuItem);
                    MenuBar menuBar = new MenuBar();
                    menuBar.getMenus().add(optionMenu);
                    
                    //Game status
                    Button statusBtn = new Button();
                    statusBtn.setText("PLAYER 1 START");
                    statusBtn.setStyle("-fx-pref-width:400px; -fx-font-weight:bold");
                    
                    //Players' score
                    Button player1ScoreBtn = new Button();
                    player1ScoreBtn.setStyle("-fx-font-size:16; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height: 150px");
                    player1ScoreBtn.setText(" ");
                    Button player2ScoreBtn = new Button();
                    player2ScoreBtn.setStyle("-fx-font-size:16; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height: 150px");
                    player2ScoreBtn.setText(" ");
                    
                    //Game
                    GridPane seedLayoutGp = new GridPane();
                    BorderPane gameLayoutBp = new BorderPane();
                    
                    gameLayoutBp.setTop(menuBar);
                    gameLayoutBp.setBottom(statusBtn);
                    gameLayoutBp.setLeft(player1ScoreBtn);
                    gameLayoutBp.setRight(player2ScoreBtn);
                    gameLayoutBp.setCenter(seedLayoutGp);
                    
                    gameLayoutBp.setPadding(new Insets(0, 0, 30, 0));
                    gameLayoutBp.setAlignment(statusBtn, Pos.BOTTOM_CENTER);
                    gameLayoutBp.setStyle("-fx-background-color:#313335");
                    
                    gameScene = new Scene(gameLayoutBp);
                      
                    int count = 0;
                    for(int a = 0; a < 2; a++){
                        for(int b = 0;b < enteredColumn; b++) {
                            int row = a;
                            int col = b;
                            count = count + 1;
                            Button seed = new Button();
                            seed.setText(Integer.toString(enteredSeed));
                            seed.setId("button" + count);
                            seed.setStyle("-fx-font:18 impact; -fx-background-radius:100%; -fx-pref-width:60px; -fx-pref-height:60px");
                             
                            if(row == 1){
                                seed.setDisable(true);
                            }
                             
                            seed.setOnAction(new EventHandler<ActionEvent>(){
                                @Override public void handle(ActionEvent e){
                                    if(isPlayer1Turn){
                                        player1Move(row, col, currentArray, player1Score, player2Score);
                                    }
                                    else{
                                        player2Move(row, col, currentArray, player1Score, player2Score);
                                    }
                                     
                                    refreshSeedCount(seedLayoutGp, gameLayoutBp);
                                    System.out.println("Player 1 score " + player1Score);
                                    System.out.println("Player 2 score " + player2Score);
                                    for (int i = 0; i < 2; i++){
                                        for (int j = 0; j < enteredColumn; j++){
                                            System.out.print(currentArray[i][j] + " ");
                                        }
                                        System.out.println();
                                    }
                                }
                            });
                            seedLayoutGp.setPadding(new Insets(20, 30, 60, 30));
                            seedLayoutGp.add(seed, b ,a);
                        }
                    }
                }catch (NumberFormatException e){
                    System.out.println("GG");
                }
                primaryStage.setScene(gameScene);
                primaryStage.show();
        	});
        	Scene customMenuScene = new Scene(customMenuGp, windowWidth, windowHeight);
	        primaryStage.setScene(customMenuScene);
	        primaryStage.show();
	    });
    	
    	//Button linking to Player vs Computer Game
    	startPageComputerBtn.setOnAction( __ -> {
    		primaryStage.setTitle("Congkak Master 1.0! - Basic Game");
    		
    		//Menu
            Menu optionMenu = new Menu("Option");

            MenuItem restartMenuItem = new MenuItem("Restart");
            restartMenuItem.setOnAction(e -> {
                System.out.println("Restart!");
                primaryStage.close();
                Platform.runLater(() -> new Main().start(new Stage()));
            });

            MenuItem exitMenuItem = new MenuItem("Exit");
            exitMenuItem.setOnAction(e -> {
                System.out.println("Exit");
                Platform.exit();
            });

            optionMenu.getItems().addAll(restartMenuItem,exitMenuItem);
            MenuBar menuBar = new MenuBar();
            menuBar.getMenus().add(optionMenu);
            
            //Game status
            Button startStatusBtn = new Button();
            startStatusBtn.setText("PLAYER 1 START");
            startStatusBtn.setStyle("-fx-pref-width:400px; -fx-font-weight:bold");
            
            //Players' score
            Button player1ScoreBtn = new Button();
            player1ScoreBtn.setStyle("-fx-font-size:16; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height: 150px");
            player1ScoreBtn.setText(" ");
            Button player2ScoreBtn = new Button();
            player2ScoreBtn.setStyle("-fx-font-size:16; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height: 150px");
            player2ScoreBtn.setText(" ");
            
            //Game
            GridPane seedLayoutGp = new GridPane();
            BorderPane gameLayoutBp = new BorderPane();
            gameLayoutBp.setTop(menuBar);
            gameLayoutBp.setBottom(startStatusBtn);
            gameLayoutBp.setLeft(player1ScoreBtn);
            gameLayoutBp.setRight(player2ScoreBtn);
            gameLayoutBp.setCenter(seedLayoutGp);

            gameLayoutBp.setStyle("-fx-background-color:#313335");
            gameLayoutBp.setPadding(new Insets(0, 0, 30, 0));
            gameLayoutBp.setAlignment(startStatusBtn, Pos.BOTTOM_CENTER); 
            
            int count = 0;
            for(int i = 0; i < 2; i++){
                for(int j = 0;j < 7; j++) {
                    int row = i;
                    int col = j;
                    count = count + 1;
                    Button seed = new Button();
                    seed.setText(Integer.toString(4));
                    seed.setId("button" + count);
                    seed.setStyle("-fx-font:18 impact; -fx-background-radius:100%; -fx-pref-width:60px; -fx-pref-height:60px");
                    
                    AILogic ai = new AILogic();
                    if(row == 1){
                        seed.setDisable(true);
                    }
                    
                    seed.setOnAction(new EventHandler<ActionEvent>(){
                        @Override public void handle(ActionEvent e){
                            if(isPlayer1Turn){
                                player1Move(row, col, currentArray, player1Score, player2Score);

                                for (int i = 0; i < 2; i++){
                                    for (int j = 0; j < 7; j++){
                                        System.out.print(currentArray[i][j] + " ");
                                    }
                                    System.out.println();
                                }

                                if(!checkWinner(currentArray, player1Score ,player2Score)){
                                    int[] moves = ai.AiMove(currentArray, player1Score, player2Score);
                                    System.out.println("CURRENT MOVES " + moves[0] + " " + moves[1]);
                                    player2Move(moves[0], moves[1], currentArray, player1Score, player2Score);
                                }
                            }
                            
                            refreshSeedCount(seedLayoutGp, gameLayoutBp);
                            
                            System.out.println("Player 1 score " + player1Score[0]);
                            System.out.println("Player 2 score " + player2Score[0]);
                            
                            for (int i = 0; i < 2; i++){
                                for (int j = 0; j < 7; j++){
                                    System.out.print(currentArray[i][j] + " ");
                                }
                                System.out.println();
                            }
                        }
                    });
                    seedLayoutGp.setPadding(new Insets(20, 30, 60, 30));
                    seedLayoutGp.add(seed, j ,i);
                }
            }
            System.out.println(count);
            
            primaryStage.setScene(new Scene(gameLayoutBp));
            primaryStage.show();
    	});
    	primaryStage.setScene(startPageScene);
        primaryStage.show();
    }
    

    //METHODS
    public int[] chooseMostSeedMove(int[][] currentArray){
        int mostSeed = currentArray[1][0];
        int mostSeedPost = 0;
        for(int i = 0;i<currentArray[0].length; i++){
            if(currentArray[1][i] != 0){
                if(currentArray[1][i] > mostSeed){
                    mostSeed = currentArray[1][i];
                    mostSeedPost = i;
                }
            }
        }
        int[] most = {1, mostSeedPost};
        return most;
    }

    
    public int[][] clone2DArray(int[][] currentArray){
        int[][] nextState=new int[currentArray.length][currentArray[0].length];

        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 7; j++){
                nextState[i][j] = currentArray[i][j];
            }

        }
        return nextState;
    }
    
    
    public int[] chooseBestMove(int[][] currentArray, int[] player1Score, int[] player2Score){
        int[] possibleMove = new int[2];
        boolean firstMove = true;
        int maxDifference = 0;
        int diffInScores;
        int nextState[][];
        int nextMovePlayer1Score[];
        int nextMovePlayer2Score[];

        for(int i = 0; i < currentArray[0].length;i++){
            if(currentArray[1][i]!=0){
                if(firstMove){
                    maxDifference = currentArray[1][i];
                    possibleMove[0] = 1;
                    possibleMove[1] = i;
                    firstMove = false;
                }
                nextState = clone2DArray(currentArray);
                nextMovePlayer1Score = player1Score.clone();
                nextMovePlayer2Score = player2Score.clone();

                System.out.println("PLAYER 1: " + nextMovePlayer1Score[0] + " PLAYER 2: " + nextMovePlayer2Score[0]);
                playerMove(1, i, nextState, nextMovePlayer1Score, nextMovePlayer2Score);
                checkValidMove(nextState, nextMovePlayer1Score, nextMovePlayer2Score);

                System.out.println("PLAYER 1: "+ nextMovePlayer1Score[0] + " PLAYER 2 SCORE: " + nextMovePlayer2Score[0]);
                diffInScores = nextMovePlayer2Score[0] - nextMovePlayer1Score[0];

                System.out.println("diff in scores " + diffInScores);

                if(diffInScores > maxDifference){
                    maxDifference = diffInScores;
                    possibleMove[0] = 1;
                    possibleMove[1] = i;
                }
            }
        }
        System.out.println("best moves " + possibleMove[0] + " " + possibleMove[1]);
        return possibleMove;
    }


    public void player1Move(int row, int col, int[][] currentArray, int[] player1Score, int[] player2Score){
        playerMove(row, col, currentArray, player1Score, player2Score);
        checkValidMove(currentArray, player1Score, player2Score);
        isPlayer1Turn = false;
    }

    public void player2Move(int row, int col, int[][] currentArray, int[] player1Score, int[] player2Score){
        playerMove(row, col, currentArray, player1Score, player2Score);
        checkValidMove(currentArray, player1Score, player2Score);
        isPlayer1Turn = true;
    }


    public void checkValidMove(int[][] array, int[] player1Score, int[] player2Score){
        int count_first = 0;
        int count_second = 0;

        if(isPlayer1Turn){
            for(int i = 0;i < array[0].length; i++){
                if(array[0][i] == 0){
                    count_first += 1;
                }

                if(array[1][i] == 0){
                    count_second += 1;
                }


                if(count_first == array[0].length){
                    addAllSeedToBase(array, player1Score, player2Score, true);
                }

                if(count_second == array[0].length){
                    addAllSeedToBase(array, player1Score, player2Score, false);
                }
            }
        }

        else{
            for(int i = 0;i < array[0].length; i++){
                if(array[1][i] == 0){
                    count_first += 1;;
                }

                if(array[0][i] == 0){
                    count_second += 1;
                }

                if(count_first == array[0].length){
                    addAllSeedToBase(array, player1Score, player2Score, false);
                }

                if(count_second == array[0].length){
                    addAllSeedToBase(array, player1Score, player2Score, true);
                }
            }
        }
    }
    

    public void addAllSeedToBase(int[][] array, int player1Score[], int[] player2Score, boolean isPlayer1Turn){
        int currentRowToBeAdded;
        int sum = 0;

        if(isPlayer1Turn){
            currentRowToBeAdded = 1;
            for(int i = 0;i < array[0].length; i++){
                sum = sum + array[currentRowToBeAdded][i];
                array[currentRowToBeAdded][i] = 0;
            }
            player2Score[0] = player2Score[0] + sum;
        }

        else{
            currentRowToBeAdded = 0;
            for(int i = 0; i < array[0].length; i++){
                sum = sum + array[currentRowToBeAdded][i];
                array[currentRowToBeAdded][i] = 0;
            }
            player1Score[0] = player1Score[0] + sum;
        }
    }

    
    public void playerMove(int row, int col, int[][] array, int[] player1Score, int[] player2Score){
        int seedCountRemaining = array[row][col];

        array[row][col] = 0;
        int[] nextMoveCoor = new int[2];

        while(seedCountRemaining != 0){
            if(row == 0){
                if(col != 0){
                    nextMoveCoor[0] = row;
                    nextMoveCoor[1] = col - 1;
                    array[nextMoveCoor[0]][nextMoveCoor[1]] = array[nextMoveCoor[0]][nextMoveCoor[1]] + 1;
                    col = col - 1;
                }
                else{
                    row = 1;
                    array[row][nextMoveCoor[1]] = array[row][nextMoveCoor[1]] + 1;
                }
            }

            else{
                if(col != (array[0].length-1)){
                    nextMoveCoor[0] = row;
                    nextMoveCoor[1] = col + 1;
                    array[nextMoveCoor[0]][nextMoveCoor[1]] = array[nextMoveCoor[0]][nextMoveCoor[1]] + 1;
                    col = col + 1;
                }
                else{
                    row = 0;
                    array[row][array[0].length-1] = array[row][array[0].length-1] + 1;
                }
            }
            seedCountRemaining -= 1;
        }

        //check next hole
        if(row== 0 && col==0 && array[row+1][col]!=0){
            //first row first col
            playerMove(row+1, col, array, player1Score, player2Score);
        }
        else if(row==1 && col==array[0].length-1 && array[row-1][col]!=0){
            //second row last rol check
            playerMove(row-1, col, array, player1Score, player2Score);
        }
        else if(row==0 && col!=0 && array[row][col-1]!=0){
            //in first row but not first col
            playerMove(row, col-1, array, player1Score, player2Score);
        }
        else if(row==1 && col!=array[0].length-1 && array[row][col+1]!=0){
            //second row but not last col
            playerMove(row, col+1, array, player1Score, player2Score);
        }
        else{
            addScoreToBase(row, col, array, player1Score, player2Score);
        }
    }
    

    public void addScoreToBase(int row, int col, int[][] array, int[] player1Score, int[] player2Score){
        if(row==0 && col==0 && array[row+1][col]==0 && array[row+1][col+1]!=0){
            if(isPlayer1Turn){
                player1Score[0] = player1Score[0] + array[row+1][col+1];
            }
            else{
                player2Score[0] = player2Score[0] + array[row+1][col+1];
            }
            array[row+1][col+1] = 0;
        }

        else if(row==1 && col==array[0].length-1 && array[row-1][col]==0 && array[row-1][col-1]!=0){
            if(isPlayer1Turn){
                player1Score[0] = player1Score[0] + array[row-1][col-1];
            }
            else{
                player2Score[0] = player2Score[0] + array[row-1][col-1];
            }
            array[row-1][col-1] = 0;
        }

        else if(row==0 && col==1 && array[row][col-1]==0 && array[row+1][col-1]!=0){
            if(isPlayer1Turn){
                player1Score[0] = player1Score[0] + array[row+1][col-1];
            }
            else{
                player2Score[0] = player2Score[0] + array[row+1][col-1];
            }
            array[row+1][col-1] = 0;
        }

        else if(row==1 && col==array[0].length-2 && array[row][col+1]==0 && array[row-1][col+1]!=0){
            if(isPlayer1Turn){
                player1Score[0] = player1Score[0] + array[row-1][col+1];
            }
            else{
                player2Score[0] = player2Score[0] + array[row-1][col+1];
            }
            array[row-1][col+1] = 0;
        }

        else if(row==0 && col !=0 && col !=1 && array[row][col-1]==0 && array[row][col-2]!=0){
            if(isPlayer1Turn){
                player1Score[0] = player1Score[0] + array[row][col-2];
            }
            else{
                player2Score[0] = player2Score[0] + array[row][col-2];
            }
            array[row][col-2] = 0;
        }

        else if(row==1 && col!=array[0].length-1 && col!=array[0].length-2 && array[row][col+1]==0 && array[row][col+2]!=0) {
            if(isPlayer1Turn){
                player1Score[0] = player1Score[0] + array[row][col+2];
            }
            else{
                player2Score[0] = player2Score[0] + array[row][col+2];
            }
            array[row][col+2] = 0;
        }
    }
    

    public boolean checkWinner(int[][]currentArray, int[] player1Score, int[] player2Score){
        int count = 0;
        boolean gameFinished = false;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < currentArray[0].length; j++) {
                if(currentArray[i][j] == 0){
                    count += 1;
                }
            }
        }

        if(count == currentArray.length * currentArray[0].length){
            if(player1Score[0] > player2Score[0]){
                System.out.println("PLAYER 1 WINS!");

            }

            else if(player1Score[0] < player2Score[0]){
                System.out.println("PLAYER 2 WINS!");
            }

            else{
                System.out.println("DRAW GAME");
            }
            gameFinished = true;
        }
        return gameFinished;
    }


    public void refreshSeedCount(GridPane layout, BorderPane root){
        ObservableList<Node> childrens = layout.getChildren();
        Button leftButton = (Button)root.getLeft();
        Button rightButton = (Button)root.getRight();
        Button statusButton = (Button)root.getBottom();
        leftButton.setText(Integer.toString(player1Score[0]));
        rightButton.setText(Integer.toString(player2Score[0]));
        int rowIndex = 0;
        int colIndex = 0;

        for(Node node:childrens){
            if(node instanceof Button){
                Button btn = (Button)node;
                rowIndex = layout.getRowIndex(node);
                colIndex = layout.getColumnIndex(node);
                btn.setText(Integer.toString(currentArray[rowIndex][colIndex]));
                btn.setDisable(false);

                if(isPlayer1Turn){
                    leftButton.setStyle("-fx-text-fill:green; -fx-font-size:20; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height:150px");
                    rightButton.setStyle("-fx-font-size:16; -fx-font:30 impact; -fx-background-radius:50%; -fx-pref-width: 150px; -fx-pref-height:150px");
                    statusButton.setText("PLAYER 1 TURN");
                }
                else if(!isPlayer1Turn){
                    rightButton.setStyle("-fx-text-fill:green; -fx-font-size:20; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height:150px");
                    leftButton.setStyle("-fx-font-size:16; -fx-font:30 impact; -fx-background-radius:50%; -fx-pref-width: 150px; -fx-pref-height:150px");
                    statusButton.setText("PLAYER 2 TURN");
                }

                if(isPlayer1Turn && rowIndex == 1){
                    btn.setDisable(true);
                    System.out.println("row index = " + rowIndex);
                }
                else if(!isPlayer1Turn && rowIndex == 0){
                    btn.setDisable(true);
                    System.out.println("row index = " + rowIndex);
                }

                if(currentArray[rowIndex][colIndex] == 0){
                    btn.setDisable(true);
                }

                int sum = 0;

                if(enteredColumn == 0 ){
                    enteredColumn = 7;
                }

                for(int i = 0; i < 2; i++){
                    for(int j = 0; j < (enteredColumn); j++) {
                        if (currentArray[i][j] == 0){
                            sum += 1;
                        }
                    }
                }

                if(sum == (2 * enteredColumn)){
                    if(player1Score[0] > player2Score[0]){
                        statusButton.setText("PLAYER 1 WINS!");
                        btn.setStyle("-fx-background-color:#313335");
                        rightButton.setStyle("-fx-text-fill:red; -fx-font-size:20; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height:150px");
                        leftButton.setStyle("-fx-font-size:20; -fx-font:20 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height:150px");
                    }
                    else if(player1Score[0] < player2Score[0]){
                        statusButton.setText("PLAYER 2 WINS!");
                        btn.setStyle("-fx-background-color:#313335");
                        leftButton.setStyle("-fx-font-size:20; -fx-font:20 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height:150px");
                        rightButton.setStyle("-fx-text-fill:red; -fx-font-size:20; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height:150px");
                    }
                    else{
                        statusButton.setText("IT'S A DRAW!");
                        btn.setStyle("-fx-background-color:#313335");
                        leftButton.setStyle("-fx-text-fill:red; -fx-font-size:20; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height:150px");
                        rightButton.setStyle("-fx-text-fill:red; -fx-font-size:20; -fx-font:60 impact; -fx-background-radius:50%; -fx-pref-width:150px; -fx-pref-height:150px");
                    }
                }
            }
        }
    }
    
    
    class AILogic{
        private int[] AiMove(int[][] currentState, int[] player1score, int[] AIscore){
            congkak.Node node = generateGameTree(currentState, 3, player1score, AIscore);
            while (node.getParent().getCoordinates() != null){
                node = node.getParent();
            }
            return (node.getCoordinates());
        }

        private congkak.Node generateGameTree(int[][] state, int depth, int[] player1score, int[] AIscore){
            congkak.Node rootNode = new congkak.Node(null, null, state, null);
            generateChildNodes(rootNode, state, depth, 0, player1score, AIscore);
            congkak.Node node = minimax(rootNode, true);
            return node;
        }

        private void generateChildNodes(congkak.Node parentNode, int[][] state, int finalDepth, int currentDepth, int[] player1score, int[] AIscore){
            if (currentDepth != finalDepth){
                for(int j = 0; j < state[0].length; j++){
                    if(state[1][j] != 0){
                        int[] coor = {1, j};
                        int[][] nextState = clone2DArray(state);
                        int[] nextPlayer1Score = player1score.clone();
                        int[] nextAIScore = AIscore.clone();
                        playerMove(1, j, nextState, nextPlayer1Score, nextAIScore);
                        checkValidMove(nextState, nextPlayer1Score, nextAIScore);
                        congkak.Node childNode = new congkak.Node(coor, parentNode, nextState, null);
                        childNode.setPlayer1Score(nextPlayer1Score);
                        childNode.setAiScore(nextAIScore);
                        childNode.reEvaluateScore();
                        generateChildNodes(childNode, childNode.getCurrentState(), finalDepth, currentDepth + 1, nextPlayer1Score, nextAIScore);
                        parentNode.addChildNode(childNode);
                    }
                }
            }
        }

        private congkak.Node minimax(congkak.Node n, boolean isMaxPlayer){
            if(n.isLeaf()){
                return n;
            }

            if(isMaxPlayer){
                congkak.Node bestValueNode = new congkak.Node(null, null, null, Integer.MIN_VALUE);
                ArrayList<congkak.Node> nodes = n.getChildNodes();
                for (int x = 0; x < nodes.size(); x++) {
                    congkak.Node node = minimax(nodes.get(x), false);
                    bestValueNode = max(bestValueNode, node);
                }
                return bestValueNode;
            }

            else{
                congkak.Node bestValueNode = new congkak.Node(null, null, null, Integer.MAX_VALUE);
                ArrayList<congkak.Node> nodes = n.getChildNodes();
                for (int x = 0; x < nodes.size(); x++) {
                    congkak.Node node = minimax(nodes.get(x), true);
                    bestValueNode = min(bestValueNode, node);
                }
                return bestValueNode;
            }
        }

        private congkak.Node max(congkak.Node n, congkak.Node m){
            if (n.getScore() > m.getScore()){
                return n;
            }
            return m;
        }

        private congkak.Node min(congkak.Node n, congkak.Node m){
            if (n.getScore() < m.getScore()){
                return n;
            }
            return m;
        }
    }
}
