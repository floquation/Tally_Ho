package nl.kevinvanas.tally_ho.game.managers;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.List;

import nl.kevinvanas.tally_ho.game.game_objects.Board;
import nl.kevinvanas.tally_ho.game.game_objects.Tile;
import nl.kevinvanas.tally_ho.game.game_objects.TileEnum;
import nl.kevinvanas.tally_ho.game.game_objects.TileGestureListener;
import nl.kevinvanas.tally_ho.game.game_objects.TileMoveRules;
import nl.kevinvanas.tally_ho.game.game_objects.Tile_Null;
import nl.kevinvanas.tally_ho.utils.Constants;

/**
 * Created by Kevin on 07/01/2016.
 */
public class BoardGridManager {

    // TODO: Rework the InputBlocker system. Now input blocks if a tile is moved outside the board

    private GameManager mgr;

    private Board board;

    private Tile[][] grid = new Tile[Constants.BOARD_GRID_WIDTH][Constants.BOARD_GRID_HEIGHT];
    private Tile[] endPhaseTiles = new Tile[]{
            new Tile_Null(),new Tile_Null(),new Tile_Null(),new Tile_Null()
    }; // exit tiles during the endPhase. Indices represent facingAngles: 0 = east, 1 = north, 2 = west, 3 = south

    public class GridIndex{
        private int i;
        private int j;

        public GridIndex(int i, int j){
            this.i=i;
            this.j=j;
        }

        public int getI() {
            return i;
        }

        public int getJ() {
            return j;
        }

        @Override
        public String toString() {
            return "GridIndex{" +
                    "i=" + i +
                    ", j=" + j +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GridIndex gridIndex = (GridIndex) o;
            return i == gridIndex.i && j == gridIndex.j;
        }

    }

    public BoardGridManager(GameManager mgr, Board board){
        this.mgr=mgr;
        this.board=board;

        if(mgr.boardInputmgr == null){
            throw new RuntimeException("(BoardGridManager) There is a programming error:\nMust create a BoardInputManager before generating the board (= by instantiating the BoardGridManager)!");
        }
        // Generate the board (48 tiles, with an open center)
        generateRandomBoard();
        // Also generate the endPhase tiles
        this.correctNewlyAddedTile(endPhaseTiles[0], this.getIndicesForTile(endPhaseTiles[0]));
        this.correctNewlyAddedTile(endPhaseTiles[1], this.getIndicesForTile(endPhaseTiles[1]));
        this.correctNewlyAddedTile(endPhaseTiles[2], this.getIndicesForTile(endPhaseTiles[2]));
        this.correctNewlyAddedTile(endPhaseTiles[3], this.getIndicesForTile(endPhaseTiles[3]));
        // Undo random rotation (inconvenient and useless)
        endPhaseTiles[0].setRotation(0f);
        endPhaseTiles[1].setRotation(0f);
        endPhaseTiles[2].setRotation(0f);
        endPhaseTiles[3].setRotation(0f);
        // Make them half-size and shift them as required
        endPhaseTiles[0].setWidth(endPhaseTiles[0].getWidth() / 2);
        endPhaseTiles[1].setHeight(endPhaseTiles[1].getHeight() / 2);
        endPhaseTiles[2].setWidth(endPhaseTiles[2].getWidth() / 2);
        endPhaseTiles[2].moveBy(endPhaseTiles[2].getWidth() , 0);
        endPhaseTiles[3].setHeight(endPhaseTiles[3].getHeight() / 2);
        endPhaseTiles[3].moveBy(0, endPhaseTiles[3].getHeight() );

//        // TODO: TEMP. TO TEST ENDPHASE: flip-all tiles but one
//        for(int i = 0; i<grid.length; i++){
//            for(int j = 0; j<grid[i].length; j++){
//                if(!(i==3 && j==4)){
//                    grid[i][j].setFlipped_up();
//                    mgr.animmgr.flipUpTile(grid[i][j],false, null);
//                }
//            }
//        }
    }


    private void generateRandomBoard(){
        // Prepare an array holding all possibilities
        int[] tileList = new int[Constants.BOARD_GRID_WIDTH*Constants.BOARD_GRID_HEIGHT-1];
        int counter=0;
        TileEnum[] types = TileEnum.values();
        for(int typeNr = 0; typeNr<types.length; typeNr++){
            int numPieces = types[typeNr].getNumPieces();
            for(int i = 0; i<numPieces; i++){
                tileList[counter] = typeNr;
                counter++;
            }
        }
//        System.out.println(Arrays.toString(tileList));

        // Randomise the board
        int[] rndTileList = new int[tileList.length];
        int numOptions = tileList.length;
        for(int i = 0; i<rndTileList.length; i++){
            int randNr = (int)(Math.random()*numOptions);
            rndTileList[i] = tileList[randNr];
            tileList[randNr] = tileList[numOptions-1];
            numOptions--;
        }
        tileList=null;
//        System.out.println(Arrays.toString(rndTileList));

        // Create the appropriate tiles
        int offset=0;
        for(int i = 0; i<grid.length; i++){
            for(int j = 0; j<grid[i].length; j++){
                int elem = i*grid[i].length+j;
                if(!(i==(Constants.BOARD_GRID_WIDTH-1)/2 && j==(Constants.BOARD_GRID_HEIGHT-1)/2)) {
//                    System.out.println("(i,j)=("+i+","+j+")");
//                    System.out.println("(" + elem + ")" + offset + " , " + rndTileList[elem-offset] + " --> " + types[rndTileList[elem-offset]]);
                    grid[i][j] = types[rndTileList[elem-offset]].createTile();
                }else{
                    offset++;
                    grid[i][j] = new Tile_Null();
                }
                this.correctNewlyAddedTile(grid[i][j],i,j);
            }
        }
        rndTileList=null;
    }



    private float computeTilePositionX(int ix, int iy){
        return Constants.BOARD_XOFFSET_ABSOLUTE+ix*Constants.BOARD_TILE_WIDTH;
    }
    private float computeTilePositionY(int ix, int iy){
        return Constants.BOARD_YOFFSET_ABSOLUTE+iy*Constants.BOARD_TILE_HEIGHT;
    }
    private float computeTileCenterPositionX(int ix, int iy){
        return Constants.BOARD_XOFFSET_ABSOLUTE+(ix+0.5f)*Constants.BOARD_TILE_WIDTH;
    }
    private float computeTileCenterPositionY(int ix, int iy){
        return Constants.BOARD_YOFFSET_ABSOLUTE+(iy+0.5f)*Constants.BOARD_TILE_HEIGHT;
    }
    private float computeTileSpritePositionX(int ix, int iy){
        return Constants.BOARD_XOFFSET_ABSOLUTE+(ix+Constants.BOARD_TILE_SPRITE_OFFSET_FACTOR)*Constants.BOARD_TILE_WIDTH;
    }
    private float computeTileSpritePositionY(int ix, int iy){
        return Constants.BOARD_YOFFSET_ABSOLUTE+(iy+Constants.BOARD_TILE_SPRITE_OFFSET_FACTOR)*Constants.BOARD_TILE_HEIGHT;
    }
    private float computeTileSpriteWidth(){
        return (1f-2f*Constants.BOARD_TILE_SPRITE_OFFSET_FACTOR)*Constants.BOARD_TILE_WIDTH;
    }
    private float computeTileSpriteHeight(){
        return (1f-2f*Constants.BOARD_TILE_SPRITE_OFFSET_FACTOR)*Constants.BOARD_TILE_HEIGHT;
    }

    /**
     * Determines whether there is a tile at the requested cell index.
     *
     * @param ix x-index of the requested cell
     * @param iy y-index of the requested cell
     * @return true if there is no tile at the requested cell. false if there is a tile. true if outside board.
     */
    public boolean isNoTileAt(int ix, int iy){
        boolean insideBoard = (ix>=0 && ix<grid.length && iy>=0 && iy<grid[ix].length);
        return !insideBoard || (grid[ix][iy].getType() == TileEnum.notile);
        /*
        This does the following, but then without the if-statement:
        if(ix>=0 && ix<grid.length && iy>=0 && iy<grid[ix].length){
            return (grid[ix][iy].getType() == TileEnum.notile);
        }else{ // Outside board, so certainly no tile there!
            return true;
        }
        */
    }

    /**
     * Determines whether there is a face-up tile at the requested cell.
     *
     * @param ix x-index of the requested cell
     * @param iy y-index of the requested cell
     * @return true if the tile at the requested cell is face-up. false if outside board.
     */
    public boolean isFlippedUpTileAt(int ix, int iy){
        boolean insideBoard = (ix>=0 && ix<grid.length && iy>=0 && iy<grid[ix].length);
        return insideBoard && (grid[ix][iy].isFlipped_up());
    }

    /**
     * Determines whether there is a tile of the given type at the requested cell index.
     *
     * @param ix x-index of the requested cell
     * @param iy y-index of the requested cell
     * @param type which type of tile to check for (instanceof TileEnum)
     * @return true if the specified type is at the requested cell. false if outside board.
     */
    public boolean isTypeTileAt(int ix, int iy, TileEnum type){
        boolean insideBoard = (ix>=0 && ix<grid.length && iy>=0 && iy<grid[ix].length);
        return insideBoard && (grid[ix][iy].getType()==type);
        /*
        This does the following, but then without the if-statement:
        if(ix>=0 && ix<grid.length && iy>=0 && iy<grid[ix].length){
            return grid[ix][iy].getType()==type;
        }else{ // Outside board, so certainly no tile there!
            return false;
        }
        */
    }

    /**
     * Determines whether there is a tile of one of the given types is at the requested cell index.
     *
     * @param ix x-index of the requested cell
     * @param iy y-index of the requested cell
     * @param types which types (List<TileEnum>) of tile to check for
     * @return true if one of the specified types is at the requested cell. false if outside board.
     */
    public boolean isOneOfTypesTileAt(int ix, int iy, List<TileEnum> types){
        boolean insideBoard = (ix>=0 && ix<grid.length && iy>=0 && iy<grid[ix].length);
        return insideBoard && (types.contains(grid[ix][iy].getType()));
        /*
        This does the following, but then without the if-statement:
        if(ix>=0 && ix<grid.length && iy>=0 && iy<grid[ix].length){
            return types.contains(grid[ix][iy].getType());
        }else{ // Outside board, so certainly no tile there!
            return false;
        }
        */
    }



    /**
     * Takes care of a new tile, by correcting:
     * - its position
     * - its size
     * - give it a random facing-angle
     * - add it as an actor to this board
     * - add a listener to it for user input
     *
     * @param tile
     * @param i
     * @param j
     */
    private void correctNewlyAddedTile(Tile tile, int i, int j){
        tile.setPosition(this.computeTileSpritePositionX(i, j), this.computeTileSpritePositionY(i, j));
        tile.setSize(this.computeTileSpriteWidth(), this.computeTileSpriteHeight());
        tile.setTileRotation((int) (Math.random() * 4) * 90f);
        tile.setFacingAngle(tile.getFaceUpRotation());
        board.addActor(tile);
        tile.addListener(new TileGestureListener(mgr.boardInputmgr));
    }

    /**
     * Takes care of a new tile, by correcting:
     * - its position
     * - its size
     * - give it a random facing-angle
     * - add it as an actor to this board
     * - add a listener to it for user input
     *
     * @param tile
     * @param index
     */
    private void correctNewlyAddedTile(Tile tile, GridIndex index){
        this.correctNewlyAddedTile(tile,index.i,index.j);
    }



    public void findMarkTargetsForTile(Tile tile, List<Tile> targetTiles){
        if(tile!=null){
            // Cannot move a neutral tile if it was manipulated in the previous turn
            Tile lastTurnTile = mgr.historymgr.getLastTurnTile();
            if(lastTurnTile == tile && lastTurnTile.getType().getOwner() == null){
                System.out.println("(BoardGridManager) The current tile has no targets, because it is a neutral tile manipulated in the last turn.");
                return;
            }

            // The tile can be moved. Let's found out where it can go!
            List<TileMoveRules> moveRules = tile.getType().getMovementList();
            List<TileEnum> targetRules = tile.getType().getTargetList();
            GridIndex indices = getIndicesForTile(tile);
            if(indices==null){
                System.out.println("(BoardGridManager) Cannot display targets for tile. It is not part of the board!");
                return;
            }
            for(TileMoveRules moveRule : moveRules){
                switch(moveRule.getDirection()){
                    case straight:
                        if(moveRule.onlyInFacingAngle()){
                            float facingAngle = tile.getFacingAngle();
                            switch((int)((facingAngle/90)%4)){
                                case 0: getEastTargets(indices, moveRule.getMaxSteps(), targetRules, targetTiles, moveRule.canAttack()); break;
                                case 1: getNorthTargets(indices, moveRule.getMaxSteps(), targetRules, targetTiles, moveRule.canAttack()); break;
                                case 2: getWestTargets(indices, moveRule.getMaxSteps(), targetRules, targetTiles, moveRule.canAttack()); break;
                                case 3: getSouthTargets(indices, moveRule.getMaxSteps(), targetRules, targetTiles, moveRule.canAttack()); break;
                            }
                        }else {
                            getWestTargets(indices, moveRule.getMaxSteps(), targetRules, targetTiles, moveRule.canAttack());
                            getEastTargets(indices, moveRule.getMaxSteps(), targetRules, targetTiles, moveRule.canAttack());
                            getNorthTargets(indices, moveRule.getMaxSteps(), targetRules, targetTiles, moveRule.canAttack());
                            getSouthTargets(indices, moveRule.getMaxSteps(), targetRules, targetTiles, moveRule.canAttack());
                        }
                        break;
                    default: throw new RuntimeException("(Board) If this exception occurs, there is a programming error.\n" +
                            "Default case in the switch was chosen, but that shouldn't be possible. Please make sure all cases of the TileMoveEnum have been programmed correctly.");
                }
            }
        }
    }

    private void getEastTargets(GridIndex index, int maxMoves, List<TileEnum> targetRules, List<Tile> list, boolean canAttack){
        int ix = index.getI();
        int iy = index.getJ();
//        System.out.println("(BoardGridManager) getEastTargets: (" + ix + "," + iy + ") with max: " + maxMoves);
        if(maxMoves==-1){
            maxMoves=Math.max(Constants.BOARD_GRID_WIDTH,Constants.BOARD_GRID_HEIGHT)+2; // +2 is required for the "endPhase" check below
        }

        // Scan from index towards east
        int imin =ix+1;
        int imax = Math.min(grid.length-1, ix + maxMoves);
        int i; // required for the "endPhase" check below
        int isTarget = MOVE_TARGET; // init to MOVE_TARGET for the "endPhase" check below
        forloop: for(i = imin; i<=imax; i++) {
            isTarget = isPositionAValidTarget(i, iy, targetRules, canAttack);
            switch (isTarget) {
                case NO_TARGET:
                    break forloop;
                case MOVE_TARGET:
                    if(!mgr.historymgr.isReverseMoveOf(grid[ix][iy],/*from*/ix,iy,/*to*/i,iy,/*last turn of current player*/1)){ // Not allowed to revert your last move.
                        list.add(grid[i][iy]);
                    }
                    break;
                case ATTACK_TARGET:
                    list.add(grid[i][iy]);
                    break forloop;
                default:
                    throw new RuntimeException("(BoardGridManager) There was a programming error.\nFound " + isTarget + " as target-identifier, but this value is not allowed.");
            }
        }

        // Check the endPhase east tile
        if(mgr.turnmgr.isFinalPhase() && i==grid.length && ( (grid.length-1)-ix < maxMoves ) && isTarget==MOVE_TARGET && iy==(Constants.BOARD_GRID_HEIGHT-1)/2 && grid[ix][iy].getType().getOwner()!=null){
            list.add(endPhaseTiles[0]);
        }
    }
    private void getWestTargets(GridIndex index, int maxMoves, List<TileEnum> targetRules, List<Tile> list, boolean canAttack){
        int ix = index.getI();
        int iy = index.getJ();
//        System.out.println("(BoardGridManager) getWestTargets: (" + ix + "," + iy + ") with max: " + maxMoves);
        if(maxMoves==-1){
            maxMoves=Math.max(Constants.BOARD_GRID_WIDTH,Constants.BOARD_GRID_HEIGHT)+2;
        }

        int imin = Math.max(0, ix - maxMoves);
        int imax = ix-1;
        int i; // required for the "endPhase" check below
        int isTarget = MOVE_TARGET; // init to MOVE_TARGET for the "endPhase" check below
        forloop: for(i = imax; i>=imin; i--) {
            isTarget = isPositionAValidTarget(i, iy, targetRules, canAttack);
            switch (isTarget) {
                case NO_TARGET:
                    break forloop;
                case MOVE_TARGET:
                    if(!mgr.historymgr.isReverseMoveOf(grid[ix][iy],/*from*/ix,iy,/*to*/i,iy,/*last turn of current player*/1)){ // Not allowed to revert your last move.
                        list.add(grid[i][iy]);
                    }
                    break;
                case ATTACK_TARGET:
                    list.add(grid[i][iy]);
                    break forloop;
                default:
                    throw new RuntimeException("(BoardGridManager) There was a programming error.\nFound " + isTarget + " as target-identifier, but this value is not allowed.");
            }
        }

        // Check the endPhase west tile
        if(mgr.turnmgr.isFinalPhase() && i==-1 && ( ix < maxMoves ) && isTarget==MOVE_TARGET && iy==(Constants.BOARD_GRID_HEIGHT-1)/2 && grid[ix][iy].getType().getOwner()!=null){
            list.add(endPhaseTiles[2]);
        }
    }
    private void getNorthTargets(GridIndex index, int maxMoves, List<TileEnum> targetRules, List<Tile> list, boolean canAttack){
        int ix = index.getI();
        int iy = index.getJ();
//        System.out.println("(BoardGridManager) getNorthTargets: (" + ix + "," + iy + ") with max: " + maxMoves);
        if(maxMoves==-1){
            maxMoves=Math.max(Constants.BOARD_GRID_WIDTH,Constants.BOARD_GRID_HEIGHT)+2;
        }

        int jmin = iy + 1;
        int jmax = Math.min(grid[ix].length-1, iy + maxMoves);
        int j; // required for the "endPhase" check below
        int isTarget = MOVE_TARGET; // init to MOVE_TARGET for the "endPhase" check below
        forloop: for (j = jmin; j <= jmax; j++) {
            isTarget = isPositionAValidTarget(ix,j,targetRules, canAttack);
            switch(isTarget){
                case NO_TARGET: break forloop;
                case MOVE_TARGET:
                    if(!mgr.historymgr.isReverseMoveOf(grid[ix][iy],/*from*/ix,iy,/*to*/ix,j,/*last turn of current player*/1)){ // Not allowed to revert your last move.
                        list.add(grid[ix][j]);
                    }
                    break;
                case ATTACK_TARGET: list.add(grid[ix][j]); break forloop;
                default: throw new RuntimeException("(BoardGridManager) There was a programming error.\nFound " + isTarget + " as target-identifier, but this value is not allowed.");
            }
        }

        // Check the endPhase north tile
        if(mgr.turnmgr.isFinalPhase() && j==grid[ix].length && ( (grid[ix].length-1)-iy < maxMoves ) && isTarget==MOVE_TARGET && ix==(Constants.BOARD_GRID_WIDTH-1)/2 && grid[ix][iy].getType().getOwner()!=null){
            list.add(endPhaseTiles[1]);
        }
    }
    private void getSouthTargets(GridIndex index, int maxMoves, List<TileEnum> targetRules, List<Tile> list, boolean canAttack){
        int ix = index.getI();
        int iy = index.getJ();
//        System.out.println("(BoardGridManager) getSouthTargets: (" + ix + "," + iy + ") with max: " + maxMoves);
        if(maxMoves==-1){
            maxMoves=Math.max(Constants.BOARD_GRID_WIDTH,Constants.BOARD_GRID_HEIGHT)+2;
        }

        int jmin = Math.max(0, iy - maxMoves);
        int jmax = iy-1;
        int j; // required for the "endPhase" check below
        int isTarget = MOVE_TARGET; // init to MOVE_TARGET for the "endPhase" check below
        forloop: for (j = jmax; j >= jmin; j--) {
            isTarget = isPositionAValidTarget(ix,j,targetRules, canAttack);
            switch(isTarget){
                case NO_TARGET: break forloop;
                case MOVE_TARGET:
                    if(!mgr.historymgr.isReverseMoveOf(grid[ix][iy],/*from*/ix,iy,/*to*/ix,j,/*last turn of current player*/1)){ // Not allowed to revert your last move.
                        list.add(grid[ix][j]);
                    }
                    break;
                case ATTACK_TARGET: list.add(grid[ix][j]); break forloop;
                default: throw new RuntimeException("(BoardGridManager) There was a programming error.\nFound " + isTarget + " as target-identifier, but this value is not allowed.");
            }
        }

        // Check the endPhase south tile
        if(mgr.turnmgr.isFinalPhase() && j==-1 && ( iy < maxMoves ) && isTarget==MOVE_TARGET && ix==(Constants.BOARD_GRID_WIDTH-1)/2 && grid[ix][iy].getType().getOwner()!=null){
            list.add(endPhaseTiles[3]);
        }
    }

    private final int NO_TARGET = 0;
    private final int MOVE_TARGET = 1;
    private final int ATTACK_TARGET = 2;
    private int isPositionAValidTarget(int i, int j, List<TileEnum> targetRules, boolean canAttack){
//        System.out.println("checking ("+i+","+j+")");
        if (isNoTileAt(i, j)) {
            // There is no tile here.
//            System.out.println("no tile");
            return MOVE_TARGET;
        }else{
            // There is a tile here. Check whether it is face-up and if so, a target
            if(!canAttack) return NO_TARGET; // If you cannot attack, it doesn't matter.
//            System.out.println("tile");
            if(isFlippedUpTileAt(i, j) && isOneOfTypesTileAt(i, j, targetRules)){
//                System.out.println("flipped-up target");
                //Attack target!
                return ATTACK_TARGET;
            }
            // Then it is no target.
            return NO_TARGET;
        }
    }

    public void moveTileTo(final Tile source, final Tile target){
        System.out.println("(BoardGridManager) Move tile to target!");
        GridIndex target_index = this.getIndicesForTile(target);
        GridIndex source_index = this.getIndicesForTile(source);

        mgr.historymgr.newMoveTurn(source, source_index.i, source_index.j, target, target_index.i, target_index.j);

        // Change the grid
        if(target.getType() == TileEnum.notile){
            if(mgr.turnmgr.isFinalPhase() && (target_index.i >= grid.length || target_index.i < 0 || target_index.j >= grid[target_index.i].length || target_index.j<0)){
                // Final-Phase Suicide Move (move to outside of the board): remove the source tile, and create a new null_tile at the source position
                mgr.animmgr.moveTo_FadeTile(source, computeTileCenterPositionX(target_index.i, target_index.j), computeTileCenterPositionY(target_index.i, target_index.j), true, new Runnable() {
                    @Override
                    public void run() { // Remove the tile from the Group after fading
                        source.getColor().a = 1f;
                        Group parent = source.getParent();
                        if (parent != null) {
                            parent.removeActor(source);
                        }
                    }
                });
                grid[source_index.i][source_index.j] = new Tile_Null();
                mgr.scoremgr.addTile(source);
                this.correctNewlyAddedTile(grid[source_index.i][source_index.j], source_index.i, source_index.j);
                for(EventListener lsner : source.getListeners()){
                    source.removeListener(lsner); // this tile can no longer receive input
                }
            }else{
                // Simple Move: switch the target(=null type) to the source position
                mgr.animmgr.moveTileTo(source, computeTileCenterPositionX(target_index.i, target_index.j), computeTileCenterPositionY(target_index.i, target_index.j), true, null);
                grid[target_index.i][target_index.j] = source;
                grid[source_index.i][source_index.j] = target;
                target.setPosition(this.computeTileSpritePositionX(source_index.i,source_index.j),this.computeTileSpritePositionY(source_index.i, source_index.j));
            }
        }else{
            // Move & Attack: create a new null tile and add the target to the points of the active player
            mgr.animmgr.moveTileTo(source, computeTileCenterPositionX(target_index.i, target_index.j), computeTileCenterPositionY(target_index.i, target_index.j), true, null);
            grid[target_index.i][target_index.j] = source;
            grid[source_index.i][source_index.j] = new Tile_Null();
            mgr.scoremgr.addTile(target);
            this.correctNewlyAddedTile(grid[source_index.i][source_index.j], source_index.i, source_index.j);
            mgr.animmgr.fadeOutTile(target, true, new Runnable() {
                @Override
                public void run() { // Remove the tile from the Group after fading
                    target.getColor().a = 1f;
                    Group parent = target.getParent();
                    if (parent != null) {
                        parent.removeActor(target);
                    }
                }
            });
            target.setZIndex(0); // move target to back such that the attacker will be on top of the killed tile
            for(EventListener lsner : target.getListeners()){
                target.removeListener(lsner); // this tile can no longer receive input
            }
        }
    }


    public GridIndex getIndicesForTile(Tile tile){
        if(tile==null) return null;
        if(tile==endPhaseTiles[0]) return new GridIndex(Constants.BOARD_GRID_WIDTH, (Constants.BOARD_GRID_HEIGHT-1)/2);
        if(tile==endPhaseTiles[1]) return new GridIndex((Constants.BOARD_GRID_WIDTH-1)/2, Constants.BOARD_GRID_HEIGHT);
        if(tile==endPhaseTiles[2]) return new GridIndex(-1, (Constants.BOARD_GRID_HEIGHT-1)/2);
        if(tile==endPhaseTiles[3]) return new GridIndex((Constants.BOARD_GRID_WIDTH-1)/2, -1);
        for(int i = 0; i<grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if(grid[i][j] == tile){
                    return new GridIndex(i,j);
                }
            }
        }
        return null;
    }

    public int countFaceDownTiles(){
        int counter = 0;
        for(int i = 0; i<grid.length; i++){
            for(int j = 0; j<grid[i].length; j++){
                counter = counter + (grid[i][j].isFlipped_up()? 0 : 1);
            }
        }
        return counter;
    }



}
