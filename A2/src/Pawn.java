import static java.lang.Math.sqrt;

public class Pawn extends Piece{

    public Pawn(int x, int y, Side side, Board board) {
        super(x, y , side, board);
        // TODO: Call super constructor
    }

    @Override
    public boolean canMove(int destX, int destY) {
        Piece currentPawn = this.board.get(this.x,this.y);
        Piece possiblePiece = this.board.get(destX,destY);

        boolean attemptFirstMove = false;
        boolean movingOneSquare = (destX == this.x) && ((currentPawn.getSide() == Side.BLACK && (destY - this.y == 1)) || (currentPawn.getSide() == Side.WHITE && (this.y - destY == 1)));
        boolean movingTwoSquares = (Math.abs(this.y - destY) == 2) && (this.x == destX);

        int movingDirection = currentPawn.getSide() == Side.WHITE? -1 : 1; // used for the diagonal movement


        if (currentPawn.getSide() == Side.WHITE && currentPawn.y == 6){
            attemptFirstMove = true;
        } else if (currentPawn.getSide() == Side.BLACK && currentPawn.y == 1) {
            attemptFirstMove = true;
        }


        //checks if can move diagonal, i.e. if there is a piece placed diagonally from the pawn
        if (possiblePiece != null && (movingDirection + this.y == destY ) && (Math.abs(this.x - destX) == 1)){ // if pawn is white, moving diagonally would cause a loss of 1 in y, if black, gain of one
            return true;
        }

        //checks if it is is attempting to make a double move with pawn, knowing that the pawn is in its original location
        if (attemptFirstMove && (movingTwoSquares|| movingOneSquare)){
            return true;
        } else if ( !attemptFirstMove && movingOneSquare){
            return true;
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "♟" : "♙";
    }
}

/*
condition for pawn: checks whether or not it is the first move,
which in that case, it would be able to move two squares instead of one
originally implemented in the pawn class -- not sure where it goes


Pawn currentPawn = null;
currentPawn.board.get(this.x, this.y);
if ((currentPawn.getSide() == Side.WHITE && currentPawn.x == 6) || (currentPawn.getSide() == Side.BLACK && currentPawn.x == 1) ){ // first move?
        return true;
        }


 */