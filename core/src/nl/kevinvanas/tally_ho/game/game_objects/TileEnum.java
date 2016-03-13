package nl.kevinvanas.tally_ho.game.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.kevinvanas.tally_ho.game.players.PlayerEnum;

/**
 * TODO: CANNOT REFER TO ENUMS BEFORE THEY ARE DEFINED AS BELOW. CONSEQUENTLY E.G. BEAR CANNOT ATTACK ANYONE!!!
 *
 * Created by Kevin on 02/01/2016.
 */
public enum TileEnum {
    bear(
            PlayerEnum.animals, // owner
            10, // points
            2, // quantity
            new Texture(Gdx.files.internal("BearTile.jpg")),
            new ArrayList<TileMoveRules>(){{ // moveList for moving
                add(new TileMoveRules(TileMoveEnum.straight,1));
            }}
        ),
    fox(
            PlayerEnum.animals, // owner
            5, // points
            6, // quantity
            new Texture(Gdx.files.internal("FoxTile.jpg")),
            new ArrayList<TileMoveRules>(){{ // moveList for moving
                add(new TileMoveRules(TileMoveEnum.straight,-1));
            }}
        ),
    hunter(
            PlayerEnum.hunters, // owner
            5, // points
            8, // quantity
            new Texture(Gdx.files.internal("HunterTile.jpg")),
            new ArrayList<TileMoveRules>(){{ // moveList for moving
                add(new TileMoveRules(TileMoveEnum.straight,-1,false,false)); // Move in any direction
                add(new TileMoveRules(TileMoveEnum.straight,-1,true,true)); // Attack only in facing angle
            }}
        ),
    woodsman(
            PlayerEnum.hunters, // owner
            5, // points
            2, // quantity
            new Texture(Gdx.files.internal("WoodsmanTile.jpg")),
            new ArrayList<TileMoveRules>(){{ // moveList for moving
                add(new TileMoveRules(TileMoveEnum.straight,1));
            }}
        ),
    pheasant(
            null, // owner
            3, // points
            8, // quantity
            new Texture(Gdx.files.internal("PheasantTile.jpg")),
            new ArrayList<TileMoveRules>(){{ // moveList for moving
                add(new TileMoveRules(TileMoveEnum.straight,-1));
            }}
        ),
    duck(
            null, // owner
            2, // points
            7, // quantity
            new Texture(Gdx.files.internal("DuckTile.jpg")),
            new ArrayList<TileMoveRules>(){{ // moveList for moving
                add(new TileMoveRules(TileMoveEnum.straight,-1));
            }}
        ),
    tree(
            null, // owner
            2, // points
            15, // quantity
            new Texture[]{
                new Texture(Gdx.files.internal("Tree1Tile.jpg")),
                new Texture(Gdx.files.internal("Tree2Tile.jpg"))
            },
            new ArrayList<TileMoveRules>(){{ // moveList for moving
            }}
        ),
    notile() // MUST BE LAST IN THIS LIST. It will trigger the "initTargetListForAll()" method upon initialisation.
    ;

    private static void initTargetListForAll(){
        try {
            bear.targetList = Collections.unmodifiableList(new ArrayList<TileEnum>() {{
                add(hunter);
                add(woodsman);
            }});
            fox.targetList = Collections.unmodifiableList(new ArrayList<TileEnum>() {{
                add(duck);
                add(pheasant);
            }});
            hunter.targetList = Collections.unmodifiableList(new ArrayList<TileEnum>() {{
                add(bear);
                add(fox);
                add(duck);
                add(pheasant);
            }});
            woodsman.targetList = Collections.unmodifiableList(new ArrayList<TileEnum>() {{
                add(tree);
            }});
            pheasant.targetList = Collections.unmodifiableList(new ArrayList<TileEnum>() {{
            }});
            duck.targetList = Collections.unmodifiableList(new ArrayList<TileEnum>() {{
            }});
            tree.targetList = Collections.unmodifiableList(new ArrayList<TileEnum>() {{
            }});
        }catch(Exception e){
            System.out.println("exception");
        }
    }

    private int numPoints; // The number of points this tile is worth
    private int numPieces; // The number of times this tile is in play
    private Texture[] textures; // Textures used for this tile (including variations)

    private PlayerEnum owner; // The player which can control this type. "null" represents neutral.

    private List<TileEnum> targetList = null;
    private List<TileMoveRules> moveList;

    private TileEnum(PlayerEnum owner, int numPoints, int numPieces, Texture texture, List<TileMoveRules> moveList){
        this.owner=owner;
        this.numPoints = numPoints;
        this.numPieces = numPieces;
        this.textures = new Texture[]{texture};
        this.moveList= Collections.unmodifiableList(moveList);
    }

    private TileEnum(PlayerEnum owner, int numPoints, int numPieces, Texture[] textures, List<TileMoveRules> moveList){
        this.owner=owner;
        this.numPoints = numPoints;
        this.numPieces = numPieces;
        this.textures=textures;
        this.moveList= Collections.unmodifiableList(moveList);
    }

    /**
     * Constructor which is called by the last in the list of TileEnum's.
     * The enum represents "no tile".
     * This constructor additionally calls the "initTargetListsForAll()"-method, which
     * initialises the targetList variable for ALL enum types.
     * To set this variable, all TileEnum's MUST already have been initialised,
     * and hence I sadly had to use this workaround to be able to set it to a non-null value.
     */
    private TileEnum(){
        this.numPoints=0;
        this.numPieces=0;
        this.textures=null;
        this.moveList=null;
        TileEnum.initTargetListForAll();
    }

    public Texture getTexture(){
        if(textures!=null) {
            int rand = (int) (Math.random() * textures.length);
            return textures[rand];
        }
        return null;
    }

    public int getNumPoints() {
        return numPoints;
    }
    public int getNumPieces() {
        return numPieces;
    }

    public List<TileEnum> getTargetList() { return targetList; }
    public List<TileMoveRules> getMovementList() { return moveList; }

    public Tile createTile(){
        switch(this){
            case bear: return new Tile_Bear();
            case fox: return new Tile_Fox();
            case hunter: return new Tile_Hunter();
            case woodsman: return new Tile_Woodsman();
            case pheasant: return new Tile_Pheasant();
            case duck: return new Tile_Duck();
            case tree: return new Tile_Tree();
            default: throw new RuntimeException("(TileEnum) If this exception occurs, there is a programming error.\n" +
                    "Default case in the switch was chosen, but that shouldn't be possible. Please make sure all cases of the enum have been programmed correctly.");
        }
    }

    public PlayerEnum getOwner(){return owner;}

}
