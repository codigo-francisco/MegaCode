package com.megacode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.TimeUtils;
import com.megacode.overlays.GameOverOverlay;
import com.megacode.overlays.GigaGalHud;
import com.megacode.overlays.OnscreenControls;
import com.megacode.overlays.VictoryOverlay;
import com.megacode.util.Assets;
import com.megacode.util.ChaseCam;
import com.megacode.util.Constants;
import com.megacode.util.LevelLoader;
import com.megacode.util.Utils;


public class GameplayScreen extends ScreenAdapter {

    public static final String TAG = GameplayScreen.class.getName();

    //private OnscreenControls onscreenControls;
    private SpriteBatch batch;
    private long levelEndOverlayStartTime;
    public Level level;
    public ChaseCam chaseCam;
    //private GigaGalHud hud;
    private VictoryOverlay victoryOverlay;
    private GameOverOverlay gameOverOverlay;
    private String rutaNivel;

    public GameplayScreen(String rutaNivel){
        this.rutaNivel = rutaNivel;
    }

    @Override
    public void show() {
        AssetManager am = new AssetManager();
        Assets.instance.init(am);

        batch = new SpriteBatch();
        chaseCam = new ChaseCam();
        //hud = new GigaGalHud();
        victoryOverlay = new VictoryOverlay();
        gameOverOverlay = new GameOverOverlay();

        //onscreenControls = new OnscreenControls();

        // TODO: Use Gdx.input.setInputProcessor() to send touch events to onscreenControls
        // TODO: When you're done testing, use onMobile() turn off the controls when not on a mobile device
        /*if (onMobile()) {
            Gdx.input.setInputProcessor(onscreenControls);
        }*/

        startNewLevel();
    }

    private boolean onMobile() {
        return false;//Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        //hud.viewport.update(width, height, true);
        victoryOverlay.viewport.update(width, height, true);
        gameOverOverlay.viewport.update(width, height, true);
        level.viewport.update(width, height, true);
        chaseCam.camera = (OrthographicCamera)level.viewport.getCamera();
        /*onscreenControls.viewport.update(width, height, true);
        onscreenControls.recalculateButtonPositions();*/
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }

    @Override
    public void render(float delta) {

        level.update(delta);
        chaseCam.update(delta);


        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        level.render(batch);

        // TODO: When you're done testing, use onMobile() turn off the controls when not on a mobile device
        /*if (onMobile()) {
            onscreenControls.render(batch);
        }*/

        //hud.render(batch, level.getGigaGal().getLives(), level.getGigaGal().getAmmo(), level.score);
        renderLevelEndOverlays(batch);
    }

    private void renderLevelEndOverlays(SpriteBatch batch) {
        if (level.gameOver) {

            if (levelEndOverlayStartTime == 0) {
                levelEndOverlayStartTime = TimeUtils.nanoTime();
                gameOverOverlay.init();
            }

            gameOverOverlay.render(batch);
            if (Utils.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                levelFailed();
            }
        } else if (level.victory) {
            if (levelEndOverlayStartTime == 0) {
                levelEndOverlayStartTime = TimeUtils.nanoTime();
                victoryOverlay.init();
            }

            victoryOverlay.render(batch);
            if (Utils.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                levelComplete();
            }

        }
    }

    private void startNewLevel() {

//        level = Level.debugLevel();

        //String levelName = "levels/Tutorial.dt"; //Constants.LEVELS[MathUtils.random(Constants.LEVELS.length - 1)];
        level = LevelLoader.load(rutaNivel);

        chaseCam.camera = (OrthographicCamera)level.viewport.getCamera();
        chaseCam.initialZoom = chaseCam.camera.zoom;
        chaseCam.target = level.getGigaGal();

        level.cam = chaseCam;

        Gdx.input.setInputProcessor(new GestureDetector(chaseCam));

        //onscreenControls.gigaGal = level.getGigaGal();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void levelComplete() {
        //startNewLevel();
    }

    public void levelFailed() {
        //startNewLevel();
    }
}
