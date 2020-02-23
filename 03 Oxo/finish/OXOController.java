class OXOController
{
    private int numberOfPlayers;
    private int currentNumber;
    private OXOModel gameDisplay;
    private int playSteps;

    public OXOController(OXOModel model)
    {
        // Initialise the OXOController
        this.numberOfPlayers = model.getNumberOfPlayers();
        this.gameDisplay = model;
        this.currentNumber = 0;
        OXOPlayer firstPlayer = model.getPlayerByNumber(currentNumber);
        this.gameDisplay.setCurrentPlayer(firstPlayer);
        this.playSteps = 0;
    }

    public void handleIncomingCommand(String command) throws InvalidCellIdentifierException, CellAlreadyTakenException, CellDoesNotExistException
    {
        // Identify if the game has a winner or drawn
        if (gameDisplay.getWinner() != null || gameDisplay.isGameDrawn()){
            return;
        }

        // invalid command, e.g. a, a2bcd, ""
        if (command.length() != 2){
            throw new InvalidCellIdentifierException("Wrong length of " + command, command);
        }

        // Flexibility for upper or lower letters, e.g. a1, A1
        command = command.toLowerCase();
        int playRowNumber = command.charAt(0) - 'a';
        int playColNumber = command.charAt(1) - '1';

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
        if (gameDisplay.getCellOwner(playRowNumber,playColNumber) != null){
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
        if (currentNumber == numberOfPlayers) {
            currentNumber = 0;
        }
        OXOPlayer nextPlayers = gameDisplay.getPlayerByNumber(currentNumber);
        gameDisplay.setCurrentPlayer(nextPlayers);
    }

    // Identify if the player is winning
    public boolean isWinning(int rowIdentify, int colIdentify, OXOPlayer holdPlayer)
    {
        // Check if the row/column/slash meets the win threshold
        // including moveX,moveY = (0,1) and (0,-1) is row, (-1,0) and (1,0) is column
        // (-1,-1) and (1,1) is backslash, (-1,1) and  (1,-1) is slash
        int rowConsecutivePieces = getConsecutivePieces(rowIdentify,colIdentify,holdPlayer,0,1)
                + getConsecutivePieces(rowIdentify,colIdentify,holdPlayer,0,-1) - 1;
        int columnConsecutivePieces = getConsecutivePieces(rowIdentify,colIdentify,holdPlayer,1,0)
                + getConsecutivePieces(rowIdentify,colIdentify,holdPlayer,-1,0) - 1;
        int backslashConsecutivePieces = getConsecutivePieces(rowIdentify,colIdentify,holdPlayer,1,1)
                + getConsecutivePieces(rowIdentify,colIdentify,holdPlayer,-1,-1) - 1;
        int slashConsecutivePieces = getConsecutivePieces(rowIdentify,colIdentify,holdPlayer,-1,1)
                + getConsecutivePieces(rowIdentify,colIdentify,holdPlayer,1,-1) - 1;
        return rowConsecutivePieces == gameDisplay.getWinThreshold() ||
                columnConsecutivePieces == gameDisplay.getWinThreshold() ||
                backslashConsecutivePieces == gameDisplay.getWinThreshold() ||
                slashConsecutivePieces == gameDisplay.getWinThreshold();
    }

    public int getConsecutivePieces(int rowIdentify, int colIdentify, OXOPlayer holdPlayer,
                                    int moveX, int moveY) {
        int consecutivePieces = 0;
        while (rowIdentify < gameDisplay.getNumberOfRows() &&
                colIdentify < gameDisplay.getNumberOfColumns() &&
                rowIdentify >= 0 && colIdentify >= 0 &&
                gameDisplay.getCellOwner(rowIdentify, colIdentify) == holdPlayer) {
            consecutivePieces++;
            rowIdentify += moveX;
            colIdentify += moveY;
        }
        return consecutivePieces;
    }

    // Identify if the input character is digit
    public boolean isDigit(char inputChar)
    {
        return inputChar - '0' >= 0 && inputChar - '0' <= 9;
    }

    // Identify if the input character is letter
    public boolean isCharacter(char inputChar)
    {
        return inputChar - 'a' >= 0 && inputChar - 'a' < 26;
    }

}
