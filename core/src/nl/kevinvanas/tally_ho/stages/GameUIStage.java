package nl.kevinvanas.tally_ho.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import nl.kevinvanas.tally_ho.game.game_objects.Tile;
import nl.kevinvanas.tally_ho.ui.MyHorizontalGroup;
import nl.kevinvanas.tally_ho.ui.MyLabel;
import nl.kevinvanas.tally_ho.utils.Constants;
import nl.kevinvanas.tally_ho.ui.MyButton;

/**
 * Created by Kevin on 01/01/2016.
 */
public class GameUIStage extends Stage {

    private final float ASPECT_RATIO = Constants.APP_HEIGHT/Constants.APP_WIDTH;

    Skin skin;

    VerticalGroup ui;
    playerRow player1row;
    playerRow player2row;


    Table tileInfo;
    HorizontalGroup detailScores;

    buttonRow buttonRow;


    public GameUIStage(Viewport viewport) {
        super(viewport);

        ui = new VerticalGroup();
        ui.setFillParent(true);
        this.addActor(ui);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        this.createPlayerRows();
        this.createDetailScores();
        this.createTileDetails();
        buttonRow = new buttonRow();

        ui.addActor(player1row);
        ui.addActor(player2row);
        ui.addActor(detailScores);
//        ui.addActor(tileInfo);
        ui.addActor(buttonRow);

        ui.setDebug(true, true); // This is optional, but enables debug lines for tables.
    }

    private class playerRow extends MyHorizontalGroup {
        MyLabel score = new MyLabel("0",skin.get(Label.LabelStyle.class),30f);
        MyLabel nr = new MyLabel("0", skin.get(Label.LabelStyle.class),30f);
        MyLabel name = new MyLabel("name", skin.get(Label.LabelStyle.class),100f);
        Container iconHolder = new Container();

        private final float scoreMinWidth = 40f;
        private final float scoreMaxWidth = 50f;
        private final float nrMinWidth = 30f;
        private final float nrMaxWidth = 30f;
        private final float iconMinWidth = 30f;
        private final float iconMaxWidth = 100000f;
        private final float nameMinWidth = 100f;
        private final float nameMaxWidth = 100000f;

        private playerRow(){
            this.addActor(iconHolder);
            this.addActor(nr);
            this.addActor(name);
            this.addActor(score);

            score.setWrap(false);
            nr.setWrap(false);
        }

        protected void markActive(boolean active){
            if(active){
                System.out.println("(GameUIStage) Mark " + this.nr.getText() + " as active player.");
                this.name.setColor(Color.CYAN);
            }else{
                System.out.println("(GameUIStage) Mark " + this.nr.getText() + " as inactive player.");
                this.name.setColor(Color.WHITE);            }
        }

        @Override
        protected void sizeChanged() {
            float width = getWidth(); float height = getHeight();
            float nonNameIconWidth = scoreMinWidth + nrMinWidth;
            float nameWidth = width - nonNameIconWidth - height;
            float iconWidth = iconMinWidth;
            if(nameWidth<nameMinWidth){
                // Cannot take maximum size for the icon! Try to maximise it to the best of our ability.
                nameWidth += height-iconMinWidth;
                if(nameWidth<nameMinWidth){
                    // Cannot satisfy all Min Sizes
                    System.out.println("(GameUIStage) Bad resolution! Cannot satisfy all minimum sizes!");
                }else{
                    // Set an icon size between desired and minimum, prioritising the icon.
                    nameWidth = nameMinWidth;
                    iconWidth = width - nonNameIconWidth - nameWidth;
                }
            }else{
                // All minimum sizes are easily satisfied.
                iconWidth = height;
            }
            score.setWidth(scoreMinWidth);
            nr.setWidth(nrMinWidth);
            Drawable icon = ((Image)iconHolder.getActor()).getDrawable();
            icon.setMinWidth(iconWidth);
            icon.setMinHeight(iconWidth);
            name.setWidth(nameWidth);

            score.setHeight(iconWidth);
            name.setHeight(iconWidth);
            nr.setHeight(iconWidth);

            name.maximiseFontSize();
//            System.out.println("Same styles? " + (name.style==nr.style));
//            score.maximiseFontSize();

            super.sizeChanged();
        }
    }

    private void createPlayerRows(){
        player1row = new playerRow();
        player1row.nr.setText("1.");
        player1row.name.setText("player1player1player1");

        player2row = new playerRow();
        player2row.nr.setText("2.");
        player2row.name.setText("player2");
    }

    private void createDetailScores(){
        //TODO: LAST EDIT POINT
        detailScores = new HorizontalGroup();
    }

    private void createTileDetails(){
        tileInfo = new Table();
    }



    private class buttonRow extends MyHorizontalGroup{
        private int numButtons = 4; // The number of visible buttons, used for layout.
        MyButton replay = new MyButton(
                new Texture(Gdx.files.internal("ui/replayUp.jpg")),
                new Texture(Gdx.files.internal("ui/replayDown.jpg"))
        );
        MyButton options = new MyButton(
                new Texture(Gdx.files.internal("ui/optionsUp.jpg")),
                new Texture(Gdx.files.internal("ui/optionsDown.jpg"))
        );
        MyButton help = new MyButton(
                new Texture(Gdx.files.internal("ui/helpUp.jpg")),
                new Texture(Gdx.files.internal("ui/helpDown.jpg"))
        );
        MyButton quit = new MyButton(
                new Texture(Gdx.files.internal("ui/quitUp.jpg")),
                new Texture(Gdx.files.internal("ui/quitDown.jpg"))
        );

        private float maxHeight = 100f;

        private buttonRow(){
            replay.setSize(50f, 50f);
            replay.registerOnButtonPress(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Replay!");
                }
            });

            options.setSize(50f, 50f);
            options.registerOnButtonPress(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Options!");
                }
            });

            help.setSize(50f, 50f);
            help.registerOnButtonPress(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Help!");
                }
            });

            quit.setSize(50f, 50f);
            quit.registerOnButtonPress(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Quit!");
                }
            });

            this.addActor(replay);
            this.addActor(options);
            this.addActor(help);
            this.addActor(quit);
        }

        @Override
        protected void sizeChanged() {
            float width = getWidth(); float height = getHeight();
            System.out.println("(GameUIStage) ButtonRow Size Changed: ("+width+","+height+ ");");

            // Give all an equal size, inserting padding in between as required.
            float buttonHeight = maxHeight;
            float buttonWidth = maxHeight;
            if(maxHeight > buttonHeight){
                // Height-limited
                buttonHeight = height;
            }else{
                // Allow maximum height as height
                if(numButtons*buttonWidth > width){
                    // Width-limited
                    buttonWidth = width/numButtons;
                    buttonHeight = buttonWidth;
                }else{
                    // Allow maximum width (= maximum height).
                    buttonHeight = maxHeight;
                    buttonWidth = maxHeight;
                }
                // Find how much padding is required.
                float extraSpace = width - numButtons*buttonWidth;
                // spacing*numButtons = extraSpace --> pad/2 BUT pad BUT pad BUT pad BUT pad/2
                float spacing = extraSpace/numButtons;
                this.space(spacing);
                this.padLeft(spacing/2);
                this.padRight(spacing/2);
                for(Actor child : this.getChildren()){
                    MyButton button = (MyButton)child;
                    button.setSize(buttonWidth,buttonHeight);
                }
            }

            super.sizeChanged();
        }
    }

    /**
     * Called when the screen is resized. The parameters refer to the size of the UI stage, not the total screen size.
     * @param width
     * @param height
     */
    public void onResize(int width, int height){
        System.out.println("On resize.");

//        System.out.println("Same styles? player1==player2 " + (player1row.name.style==player2row.name.style)); //true
//        System.out.println("Same font? player1==player2 " + (player1row.name.style.font == player2row.name.style.font)); //true
//        System.out.println("Same scaleY? player1==player2 " + (player1row.name.style.font.getScaleY() == player2row.name.style.font.getScaleY())); //true

        player1row.setSize(width, height / 10f);
        player2row.setSize(width, height / 10f);

        buttonRow.setSize(width,height / 5f);
//        buttonRow.setPosition(0,4*height/5f);

//        System.out.println("scale = " + (width/(float)Constants.APP_WIDTH));
//        player2row.name.setFontScale(width/(float)Constants.APP_WIDTH);
    }

    public void setScores(int score1, int score2){
        player1row.score.setText("" + score1);
        player2row.score.setText("" + score2);
    }
    public void addScoreTile(Tile tile){
        System.out.println("(GameUIStage) NOT YET IMPLEMENTED: addScoreTile");
    }
    public void setActivePlayer(int player){
        if(player==0){
            player1row.markActive(true);
            player2row.markActive(false);
        }else{
            player2row.markActive(true);
            player1row.markActive(false);
        }
    }

    public void setPlayer1Icon(Texture texture){
        Image player1Icon = new Image(texture);
        player1Icon.getDrawable().setMinWidth(30);
        player1Icon.getDrawable().setMinHeight(30);
        player1row.iconHolder.setActor(player1Icon);
    }
    public void setPlayer2Icon(Texture texture){
        Image player2Icon = new Image(texture);
        player2Icon.getDrawable().setMinWidth(30);
        player2Icon.getDrawable().setMinHeight(30);
        player2row.iconHolder.setActor(player2Icon);
    }



}
