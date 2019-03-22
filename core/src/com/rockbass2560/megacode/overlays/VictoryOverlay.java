package com.megacode.overlays;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.megacode.entities.Explosion;
import com.megacode.util.Constants;

public class VictoryOverlay {

    public final static String TAG = VictoryOverlay.class.getName();
    public final Viewport viewport;
    final BitmapFont font;

    public VictoryOverlay() {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Firebug.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;

        font = generator.generateFont(parameter); // new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(1);

        generator.dispose();
    }

    public void render(SpriteBatch batch) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        font.draw(batch, "Nivel Superado", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 1.1f, 0, Align.center, false);

        batch.end();

    }
}
