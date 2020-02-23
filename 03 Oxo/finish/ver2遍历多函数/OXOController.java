class OXOController
{
    private int numberOfPlayers;
    private int currentNumber;
    private OXOModel gameDisplay;
    private int playSteps;

    public OXOController(OXOModel model)
    {
        //Initialise the OXOController
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
        if (command.length() > 2 || command.length() <= 1){
            throw new InvalidCellIdentifierException(command, command);
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
        if (columnReachThreshold(rowIdentify,colIdentify,holdPlayer)) {
            return true;
        }
        else if (rowReachThreshold(rowIdentify,colIdentify,holdPlayer)) {
            return true;
        }
        else if (backslashReachThreshold(rowIdentify,colIdentify,holdPlayer)) {
            return true;
        }
        else if (slashReachThreshold(rowIdentify,colIdentify,holdPlayer)) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean columnReachThreshold(int rowIdentify, int colIdentify, OXOPlayer holdPlayer)
    {
        int target = 0;
        for (int col = 0;col < gameDisplay.getNumberOfColumns();col++){
            if (gameDisplay.getCellOwner(rowIdentify,col) == holdPlayer){
                target++;
            }
            else{
                target = 0;
            }
            if (target == gameDisplay.getWinThreshold()){
                return true;
            }
        }
        return false;
    }

    public boolean rowReachThreshold(int rowIdentify, int colIdentify,OXOPlayer holdPlayer)
    {
        int target = 0;
        for (int row = 0;row < gameDisplay.getNumberOfRows();row++){
            if (gameDisplay.getCellOwner(row,colIdentify) == holdPlayer){
                target++;
            }
            else{
                target = 0;
            }
            if (target == gameDisplay.getWinThreshold()){
                return true;
            }
        }
        return false;
    }

    public boolean backslashReachThreshold(int rowIdentify, int colIdentify,OXOPlayer holdPlayer)
    {
        int target = 0;
        int rowStart = rowIdentify, colStart=colIdentify;
        while (rowIdentify < gameDisplay.getNumberOfRows() &&
                colIdentify < gameDisplay.getNumberOfColumns()&&
                gameDisplay.getCellOwner(rowIdentify,colIdentify) == holdPlayer){
            target++;
            rowIdentify++;
            colIdentify++;
        }
        rowIdentify = rowStart - 1;
        colIdentify = colStart - 1;
        while (rowIdentify >= 0 && colIdentify >= 0 &&
            gameDisplay.getCellOwner(rowIdentify,colIdentify) == holdPlayer){
            target++;
            rowIdentify--;
            colIdentify--;
        }
        if (target == gameDisplay.getWinThreshold()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean slashReachThreshold(int rowIdentify, int colIdentify,OXOPlayer holdPlayer)
    {
        int target = 0;
        int rowStart = rowIdentify, colStart = colIdentify;
        while (rowIdentify >= 0 &&
                colIdentify < gameDisplay.getNumberOfColumns() &&
                gameDisplay.getCellOwner(rowIdentify,colIdentify) == holdPlayer){
            target++;
            rowIdentify--;
            colIdentify++;
        }
        rowIdentify=rowStart + 1;
        colIdentify=colStart - 1;
        while (colIdentify >= 0 &&
                rowIdentify < gameDisplay.getNumberOfRows() &&
                gameDisplay.getCellOwner(rowIdentify,colIdentify) == holdPlayer){
            target++;
            rowIdentify++;
            colIdentify--;
        }
        if (target == gameDisplay.getWinThreshold()){
            return true;
        }
        else{
            return false;
        }
    }

    // Identify if the input character is digit??library
    public boolean isDigit(char inputChar)
    {
        if (inputChar-'0' >= 0 && inputChar-'0' <= 9){
            return true;
        }
        else{
            return false;
        }
    }

    // Identify if the input character is letter
    public boolean isCharacter(char inputChar)
    {
        if (inputChar-'a' >= 0 && inputChar-'a' < 26){
            return true;
        }
        else{
            return false;
        }
    }

}
