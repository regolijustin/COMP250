import java.util.Stack;

public class Game {
    Board b;
    Stack<String> moveHistory = new Stack<String>();
    Side currentTurn;

    public Game(){
        // todo: write a constructor that initializes the game with a new board
        // hint: you are also responsible for tracking whose turn it is
        //reset(); // not sure if right
        b = new Board();
        //currentTurn.negate(currentTurn); // not sure if this is correct: intent is to change the side at every turn,
        currentTurn = Side.WHITE;
    }

    public static String getName() {
        return "Queen's Game-bit";
    }
    public boolean canMove(int x, int y, int destX, int destY, Side s){
       /* TODO write a method that checks if a piece at coordinates x,y can move to coordinates destX,destY
       Conditions for false:
       - Origin or destination coordinates are outside the board
       - Piece at origin is null
       - If source and destination coordinates are the same
       - Piece at origin is not of the same side as s
            - You can check this using piece.getSide()
       - Piece cannot move to the destination by piece movement rules
            - You should check this using Piece.canMove(destX, destY)
       - Destination has a piece of the same Side as the player
       - piece must move "through" a piece to get from (x,y) to (destX,destY) (use isVisible())
            - The knight is the exception to this rule. The knight can hop "over" pieces, so be sure to check for this.
          */


        if (  x > 7 || x < 0 || y > 7 || y < 0 || destX > 7 || destX < 0 || destY > 7 || destY < 0 ){
            return false;
        }

        Piece currentPiece = b.get(x,y);
        Piece destinationPiece = b.get(destX,destY);

        if (currentPiece == null || (x == destX && y == destY)){
            return false;
        } else if(currentPiece.getSide() != s ){ // piece that we want to move is not the same one as the input of the method
            return false;
        } else if (!currentPiece.canMove(destX, destY)){
            return false;
        } else if (destinationPiece != null && destinationPiece.getSide() == s) {
            return false;
        } else if (!(currentPiece.getSymbol() == "♞" || currentPiece.getSymbol() == "♘")){
            if (!isVisible(x,y,destX, destY)){
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * This method is provided to you in order to help with canMove().
     *
     * In chess, no piece except the knight can "move through" or "hop over" a piece that's in its way
     *
     * This method checks that there are no pieces along the path from (x,y) to (destX,destY).
     * Note that a "path" is only defined if (x,y) and (destX,destY) are on the same row, column, or diagonal.
     * If the requested path is undefined, the method throws an exception.
     *
     * If the path is defined and no piece is found along the path, the method returns true.
     *
     * Don't worry about how this method works or tests and edge cases for it, we will
     * grade you assuming you keep it exactly as provided and use it as a part of your
     * canMove() method.
     */
    private boolean isVisible(int x, int y, int destX, int destY) {
        int diffX = destX - x;
        int diffY = destY - y;

        boolean validCheck = (diffX == 0 || diffY == 0) || (Math.abs(diffX) == Math.abs(diffY));
        if (!validCheck) {
            try {
                throw new Exception("The 'path' between the squares (" + x + ", " + y + ") and (" + destX + ", " + destY +") is undefined");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Diagonal
        if (Math.abs(diffX) == Math.abs(diffY) && Math.abs(diffX) > 1) {
            //Determine direction of move
            int dirX = diffX > 0 ? 1 : -1;
            int dirY = diffY > 0 ? 1 : -1;
            for (int i = x + dirX, j = y + dirY; i != destX && j != destY; i += dirX, j += dirY) {
                if (b.get(i, j) != null) {
                    return false;
                }
            }
        }

        //Linear
        if ((diffX == 0 && Math.abs(diffY) > 1) || (diffY == 0 && Math.abs(diffX) > 1)) {
            if (diffX == 0) {
                int dirY = diffY > 0 ? 1 : -1;
                for (int j = y + dirY; j != destY; j += dirY) {
                    if (b.get(x, j) != null) {
                        return false;
                    }
                }
            } else {
                int dirX = diffX > 0 ? 1 : -1;
                for (int i = x + dirX; i != destX; i += dirX) {
                    if (b.get(i, y) != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void appendCheckToHistory(Side side){
        moveHistory.push(side.toString()+ " is in check");
    }

    private void appendWinToHistory(Side side){
        moveHistory.push(side.toString()+ " has won");
    }

    private void appendMoveToHistory(int x, int y, int destX, int destY, Side side){
        Piece toMove = b.get(x,y);
        Piece toCapture = b.get(destX, destY);
        if(toCapture == null){
            moveHistory.push(side.toString() + toMove.getSymbol() + " at "+ x + ", " + y + " to " + destX + ", " + destY);
        }else{
            moveHistory.push(side.toString() + toMove.getSymbol() + " at " + x + ", " + y + " captures " + toCapture.getSymbol() + " at " + destX + ", " + destY);
        }
    }


    /**
     * This method takes as input the coordinates of the piece to move and of the destination to move to.
     *
     * It returns true if the move is valid and false otherwise.
     *
     * Conditions for a valid move are determined in canMove()
     *
     * Upon a successful move, this method also:
     * - updates the board to reflect the move
     * - tracks the move in game history
     *   - also tracks in history if the move was:
     *     - a capture
     *     - result in check EDIT: for either side
     *     - EDIT: also if it results in a win / king capture
     * - updates the current player's turn
     *
     *  x The x coordinate of the piece to move
     *  y The y coordinate of the piece to move
     *  destX The x coordinate of the destination to move to
     *  destY The y coordinate of the destination to move to
     */

    //missing: isInCheck for both sides
    public boolean move(int x, int y, int destX, int destY){
        // TODO write this method
        if (!canMove(x,y,destX,destY, currentTurn)){
            return false;
        }
        //if (!isVisible(x,y,destX,destY)) return false;

        Piece pieceToMove =  b.get(x,y);
        Piece potentialPiece = b.get(destX,destY);
        boolean kingCaptured = false;

        boolean opposingPieceAtDestination = potentialPiece != null && (potentialPiece.getSide() != pieceToMove.getSide());
        boolean teampieceAtDestination = potentialPiece != null && (potentialPiece.getSide() == pieceToMove.getSide());
        currentTurn = Side.negate(pieceToMove.getSide());

        if (pieceToMove.canMove(destX,destY) && !teampieceAtDestination){
            if (opposingPieceAtDestination){
                //potentialPiece.capture();
                if (potentialPiece == b.getKing(potentialPiece.getSide())){
                    appendMoveToHistory(x,y,destX,destY,pieceToMove.getSide());
                    potentialPiece.capture();
                    pieceToMove.move(destX,destY);
                    appendWinToHistory(pieceToMove.getSide());
                    kingCaptured = true;
                } else {
                    potentialPiece.capture();
                    appendMoveToHistory(x,y,destX,destY,pieceToMove.getSide());
                    pieceToMove.move(destX,destY);
                }
            } else{
                appendMoveToHistory(x,y,destX,destY,pieceToMove.getSide());
                pieceToMove.move(destX,destY);
            }


            if (!kingCaptured && isInCheck(Side.WHITE)) appendCheckToHistory(Side.WHITE); // checks if white is in check
            if (!kingCaptured && isInCheck(Side.BLACK)) appendCheckToHistory(Side.BLACK); // checks if black is in check


            return true;
        }
        return false;
    }

    /**
     * Return true if the King of Side side can be captured by any of
     * the opponent's pieces.
     *
     */

    public boolean isInCheck(Side side) {
        // TODO write this method
        Piece sideColorKing = b.getKing(side);


        for (Piece currentPiece : b.getPieces(Side.negate(side))){
            if (canMove(currentPiece.x,currentPiece.y,sideColorKing.x, sideColorKing.y,Side.negate(side))){
                return true;
            }
        }

        return false;
    }



    /**
     * Ensures that the game is in the exact same state as a new game
     */
    public void reset(){
        while(!moveHistory.empty()){
            System.out.println(moveHistory.pop());
        }
        b.fillBoard();
        currentTurn = Side.WHITE;
    }


    public static void main(String[] args){
        Board b = new Board();
        System.out.println(b);
    }

    /**
     * Return an array of Strings containing every successful move made during the game,
     * and every time a move resulted in check.
     */
    public String[] moveHistory(){
        String[] out = new String[moveHistory.size()];
        for(int i = 0; i < moveHistory.size(); i++){
            out[i] = moveHistory.get(i);
        }
        return out;
    }

    public Side getCurrentTurn(){
        return currentTurn;
    }
}


//!( currentPiece.getSymbol() == "♞" || currentPiece.getSymbol() == "♘"