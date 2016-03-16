package nl.kevinvanas.tally_ho.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Kevin on 12/03/2016.
 */
public class MyLabel extends Actor {

    static private final Color tempColor = new Color();
    private BitmapFontCache cache;

    public Label.LabelStyle style;
    private String text0;

    private int labelAlign = Align.left; //TODO: Align.Center doesn't behave properly (text may go outside of the bounds on the right side)
    private int lineAlign = Align.left;
    private float fontScaleX = 1, fontScaleY = 1;

    private boolean wrap = true;
    private String wrapText = "..";

    GlyphLayout layout = new GlyphLayout();

    private boolean constructorRunning = true;

    private boolean invalid = true;

    public MyLabel (String text, Label.LabelStyle style, float width, float height) {
        setSize(width, height);
        setStyle(style);
        if (text != null && text.length() > 0){
            setText(text);
        }

        constructorRunning = false;
    }

    public MyLabel (String text, Label.LabelStyle style, float width) {
        this(text, style, width, 30);
    }

    public void setText(String newText){
        if (newText == null) newText = "";
        if (newText.equals(text0)) return;
        this.text0 = newText;
        invalid=true;
    }

    public String getText(){
        return text0;
    }

    public void setStyle (Label.LabelStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        if (style.font == null) throw new IllegalArgumentException("Missing LabelStyle font.");
        this.style = style;
        cache = style.font.newFontCache();
        cache.setText(layout, 0, 0);
    }

    public void setFontScale(float scaleXY){
        this.fontScaleX=scaleXY;
        this.fontScaleY=scaleXY;
//        style.font.getData().setScale(scaleXY); // If you do this, you change the font of ALL LABELS, instead of only this label.
        invalid=true;
    }

    public void maximiseFontSize(){
        float height=this.getHeight(); float width=this.getWidth();
        System.out.println("Label \"" + this.getText() + "\"");
        System.out.println("label height = " + height);
        System.out.println("lineheight = " + style.font.getData().lineHeight);
        System.out.println("lineheight2 = " + style.font.getLineHeight());
        System.out.println("layout width = " + layout.width);
        System.out.println("xHeight = " + style.font.getData().xHeight);
        float scale=height/style.font.getLineHeight();
        if(!wrap && scale*layout.width > width){
            // No wrap, and new font size is too big.
            // Maximise the width instead
            scale=width/layout.width;
        }
        System.out.println("Chosen scale = " + scale);
        setFontScale(scale);
        invalid=true;
    }

    private void computeText(){
        if(constructorRunning){ // Do not do this during the constructor call.
            return;
        }
        System.out.println("(MyLabel) \"" + getText() +"\" computeText()");
        BitmapFont font = cache.getFont();
        float oldScaleX = font.getScaleX();
        float oldScaleY = font.getScaleY();
        if (fontScaleX != 1 || fontScaleY != 1) font.getData().setScale(fontScaleX, fontScaleY);

        boolean wrap = this.wrap && wrapText == null;
//        if (wrap) {
//            float prefHeight = getPrefHeight();
//            if (prefHeight != lastPrefHeight) {
//                lastPrefHeight = prefHeight;
//                invalidateHierarchy();
//            }
//        }

        float width = getWidth(), height = getHeight();
        Drawable background = style.background;
        float x = 0, y = 0;
        if (background != null) {
            x = background.getLeftWidth();
            y = background.getBottomHeight();
            width -= background.getLeftWidth() + background.getRightWidth();
            height -= background.getBottomHeight() + background.getTopHeight();
        }

        float textWidth = layout.width, textHeight = layout.height;

        if ((labelAlign & Align.left) == 0) {
            if ((labelAlign & Align.right) != 0)
                x += width - textWidth;
            else
                x += (width - textWidth) / 2;
        }

        if ((labelAlign & Align.top) != 0) {
            y += cache.getFont().isFlipped() ? 0 : height - textHeight;
            y += style.font.getDescent();
        } else if ((labelAlign & Align.bottom) != 0) {
            y += cache.getFont().isFlipped() ? height - textHeight : 0;
            y -= style.font.getDescent();
        } else {
            y += (height - textHeight) / 2;
        }
        if (!cache.getFont().isFlipped()) y += textHeight;

//        System.out.println("textwidth = " + textWidth + "; width = " + width);

        layout.setText(font, text0, 0, text0.length(), Color.WHITE, width, lineAlign, wrap, wrapText);
        cache.setText(layout, x, y);

        if (fontScaleX != 1 || fontScaleY != 1) font.getData().setScale(oldScaleX, oldScaleY);
    }

    public void draw (Batch batch, float parentAlpha) {
        if(invalid) {invalid=false; computeText(); computeText();} //TODO: WHY DO I NEED TO CALL IT TWICE TO PROPERLY POSITION MY TEXT???

        Color color = tempColor.set(getColor());
        color.a *= parentAlpha;
        if (style.background != null) {
            batch.setColor(color.r, color.g, color.b, color.a);
            style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
        if (style.fontColor != null) color.mul(style.fontColor);
//        System.out.println("Draw label with cache: " + cache.getLayouts().get(0).toString());
        cache.tint(color);
        cache.setPosition(getX(), getY());
        cache.draw(batch);
    }

    public void setWrap(boolean b){
        wrap=b;
        if(wrap){
            wrapText="..";
        }else{
            wrapText = null;
        }
        invalid=true;
    }
    public void setWrap(String wrapText){
        this.wrapText=wrapText;
        wrap = (wrapText != null);
        invalid=true;
    }
    public void setWrapText(String s){
        wrapText=s;
        invalid=true;
    }

    @Override
    protected void sizeChanged() {
        invalid=true;
    }
}
