package nl.kevinvanas.tally_ho.game.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.badlogic.gdx.utils.Align;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Kevin on 01/01/2016.
 */
public abstract class Tile extends Actor {

    private TileEnum type;

    private boolean flipped_up = false; // Is the current tile face-up?
    private boolean is_selected = false; // Is the current tile selected by the user?
    /**
     * Can this tile be clicked as a target (e.g. for movement) when another tile is selected?
     * ONLY used for drawing, and hence it does not have a getter. "Board" holds a list of targetTiles for other purposes.
     * This is safer, because then "Board" is responsible, whereas if "Tile" is responsible,
     * then any corrupted class can change this Tile to being untargetable.
     * This is an issue, since Tile-instances are passed all over the place.
     */
    private boolean is_possible_target = false;

    private float facingAngle = this.getRotation();
    private float faceUpRotation = this.getRotation();

    protected Texture texture_up = null;
    private static Texture texture_down = new Texture(Gdx.files.internal("BacksideTile.png"));

    private Sprite sprite = new Sprite(texture_down);

    public Tile(float x, float y, float width, float height, TileEnum type){
        this.setPosition(x, y);
        this.setWidth(width);
        this.setHeight(height);
        this.type=type;

        this.positionChanged();
        this.sizeChanged();
        this.init();
    }

    public Tile(TileEnum type){
        this.type=type;
        this.init();
    }

    private void init(){
        if(type!=TileEnum.notile) {
            texture_up = type.getTexture();
        }else{
            flipped_up=true;
            sprite.setTexture(null);
        }
        setTouchable(Touchable.enabled);
    }

    /**
     * Set the rotation of the tile for drawing.
     *
     * @param rot
     */
    public void setTileRotation(float rot){
        if(this.flipped_up){
            this.faceUpRotation=rot;
            this.setRotation(rot);
        }else{
            this.faceUpRotation=rot;
        }
    }

    /**
     * Set the facing angle of the tile for attack-processing.
     * A facingAngle of 0 degrees is to the right. 90 degrees is up. 180 degrees west. 270 degrees south.
     *
     * @param facingAngle
     */
    public void setFacingAngle(float facingAngle){
        this.facingAngle=facingAngle;
    }
    public float getFacingAngle(){
        return facingAngle;
    }

    public float getFaceUpRotation(){
        return faceUpRotation;
    }

    @Override
    protected void positionChanged() {
//        System.out.println("positionChanged: " + this.getX() + ", " + this.getY());
        sprite.setPosition(this.getX(), this.getY());
    }

    @Override
    protected void sizeChanged(){
//        System.out.println("sizeChanged: " + this.getWidth() + ", " + this.getHeight());
        sprite.setSize(this.getWidth(), this.getHeight());
        sprite.setOriginCenter();
        this.setOrigin(0); //setOriginCenter
    }

    @Override
    protected void rotationChanged(){
        sprite.setRotation(this.getRotation());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(!(sprite.getTexture() == null)) { // The "null tile" is not drawn, but still has an instantiated class for movement reasons.
            if (is_selected) {
                sprite.draw(batch, parentAlpha*this.getColor().a*0.5f);
            } else {
                sprite.draw(batch, parentAlpha*this.getColor().a);
            }
        }
        if (is_possible_target) {
            // This tile is a possible target. Draw a hitbox on top of it
            //TODO: DRAW SOME BOX. THIS IS A VERY BAD TEMPORARY SOLUTION
            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
            shapeRenderer.translate(sprite.getX(), sprite.getY(), 0);
            shapeRenderer.translate(sprite.getOriginX(), sprite.getOriginY(), 0);
            shapeRenderer.rotate(0, 0, 1, sprite.getRotation());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1f,0f,0f,0.5f);
            shapeRenderer.rect(-this.getWidth()/2, -this.getHeight()/2, this.getWidth(), this.getHeight());
            shapeRenderer.end();
            batch.begin();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void setFlipped_up(){
        this.flipped_up = true;
    }
    public void setFaceUpTexture(){
        sprite.setTexture(texture_up);
    }

    protected abstract int[][] whereCanIGo();

    /**
     * @return true if the tile is faced-up. false if faced-down.
     */
    public boolean isFlipped_up(){ return flipped_up; }

    /**
     * Mark this tile as `selected'. The drawing changes.
     */
    public void select() {
        is_selected=true;
    }

    /**
     * Unmark this tile as `selected'. The drawing returns to normal.
     */
    public void deselect() {
        is_selected=false;
    }

    public TileEnum getType(){return type;}

    public void setIs_possible_target(boolean is_possible_target) {
        this.is_possible_target = is_possible_target;
    }

}