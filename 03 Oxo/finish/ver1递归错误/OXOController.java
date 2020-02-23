class OXOController
{
    private int numberOfPlayers;
    private int currentNumber;
    private OXOModel gameDisplay;
    private int playSteps;

    public OXOController(OXOModel model)
    {
        //Initialise the OXOController
        this.numberOfPlayers=model.getNumberOfPlayers();
        this.gameDisplay=model;
        this.currentNumber=0;
        OXOPlayer firstPlayer = model.getPlayerByNumber(currentNumber);
        this.gameDisplay.setCurrentPlayer(firstPlayer);
        this.playSteps=0;
    }

    public void handleIncomingCommand(String command) throws InvalidCellIdentifierException, CellAlreadyTakenException, CellDoesNotExistException
    {
        // Identify if the game has a winner or drawn
        if (gameDisplay.getWinner()!=null || gameDisplay.isGameDrawn()){
            return;
        }

        // invalid command, e.g. a, a2bcd, ""
        if (command.length()>2||command.length()<=1){
            throw new InvalidCellIdentifierException(command, command);
        }

        // Flexibility for upper or lower letters, e.g. a1, A1
        command=command.toLowerCase();
        int playRowNumber=command.charAt(0)-'a';
        int playColNumber=command.charAt(1)-'1';

        // invalid for column-row, column-column,row-row, e.g. 1a, 11, aa, a!, ?2
        if (!isCharacter(command.charAt(0))) {
            throw new InvalidCellIdentifierException("column", command.charAt(0));
        }
        if (!isDigit(command.charAt(1))) {
            throw new InvalidCellIdentifierException("row", command.charAt(1));
        }

        // Cell not exist too big or too small (use 0 column), e.g. a9, z3 and a0
        if ((playRowNumber >= gameDisplay.getNumberOfRows()) || (playColNumber < 0) ||
                (playColNumber >= gameDisplay.getNumberOfColumns())){
            throw new CellDoesNotExistException(playRowNumber, playColNumber);
        }

        // Cell already taken
        if (gameDisplay.getCellOwner(playRowNumber,playColNumber)!=null){
            throw new CellAlreadyTakenException(playRowNumber, playColNumber);
        }

        // Set cell owner
        gameDisplay.setCellOwner(playRowNumber,playColNumber,gameDisplay.getCurrentPlayer());

        // Identify if the game is draw
        playSteps++;
        if (playSteps == gameDisplay.getNumberOfRows()*gameDisplay.getNumberOfColumns()){
            gameDisplay.setGameDrawn();
        }

        // Win Detection
        if (isWinning(playRowNumber,playColNumber,gameDisplay.getCurrentPlayer())) {
            gameDisplay.setWinner(gameDisplay.getCurrentPlayer());
        }

        // Switch the player
        switchPlayers();
    }

    // Switch and set the current player
    public void switchPlayers()
    {
        currentNumber++;
        if (currentNumber==numberOfPlayers) {currentNumber=0;}
        OXOPlayer nextPlayers=gameDisplay.getPlayerByNumber(currentNumber);
        gameDisplay.setCurrentPlayer(nextPlayers);
    }

    // Identify if the player is winning
    public boolean isWinning(int rowIdentify, int colIdentify, OXOPlayer holdPlayer)
    {
        /** Check if the row/column meets the win threshold,
         * including moveX,moveY = (-1,0)-up, (1,0)-down, (0,1)-right,(0,-1)-left
         * (-1,-1)-top left, (1,1)-bottom right, (-1,1)-top right, (1,-1)-bottom left */
        for (int moveX=-1;moveX<=1;moveX++){
            for (int moveY=-1;moveY<=1;moveY++){
                if (checkRow(rowIdentify,colIdentify,holdPlayer,moveX,moveY,1)){
                    return true;
                }
            }
        }
        return false;
    }

    // Check the row horizontal, vertical and diagonals
    public boolean checkRow(int rowIdentify, int colIdentify,OXOPlayer holdPlayer,
                           int moveX, int moveY,int recordRow)
    {
        if (moveX == 0 && moveY == 0){
            return false;
        }
        if (recordRow == gameDisplay.getWinThreshold()){
            return true;
        }
        rowIdentify=rowIdentify+moveX;
        colIdentify=colIdentify+moveY;
        if (rowIdentify < 0 || colIdentify < 0 ||
                rowIdentify >= gameDisplay.getNumberOfRows() ||
                colIdentify >= gameDisplay.getNumberOfColumns()){
            return false;
        }
        if (gameDisplay.getCellOwner(rowIdentify,colIdentify)==holdPlayer){
            recordRow++;
            if (checkRow(rowIdentify,colIdentify,holdPlayer,moveX,moveY,recordRow)){
                return true;
            }
        }
        return false;
    }

    // Identify if the input character is digit??library
    public boolean isDigit(char inputChar)
    {
        if (inputChar-'0'>=0 && inputChar-'0'<=9){
            return true;
        }
        else{
            return false;
        }
    }

    // Identify if the input character is letter
    public boolean isCharacter(char inputChar)
    {
        if (inputChar-'a'>=0 && inputChar-'a'<26){
            return true;
        }
        else{
            return false;
        }
    }

}
