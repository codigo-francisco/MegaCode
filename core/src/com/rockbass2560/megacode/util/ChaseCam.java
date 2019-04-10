package com.rockbass2560.megacode.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.rockbass2560.megacode.entities.MegaCode;

public class ChaseCam {

    public static final String TAG = ChaseCam.class.getName();

    private OrthographicCamera camera;
    public MegaCode target;
    public Boolean following;
    public float zoom;

    public ChaseCam() {
        following = false;
    }

    public OrthographicCamera getCamera(){
        return camera;
    }

    public void setCamera(OrthographicCamera camera){
        InputControl inputControl = new InputControl(camera);
        GestureControl gestureControl = new GestureControl(camera);
        InputMultiplexer inputMultiplexer = new InputMultiplexer(gestureControl, inputControl);
        Gdx.input.setInputProcessor(inputMultiplexer);
        this.camera = camera;
    }

    public void resetCameraPosition(boolean following){
        camera.position.x = target.getPosition().x;
        camera.position.y = target.getPosition().y;
        this.following = following;
        camera.zoom = this.zoom;
    }

    private static float currentZoom;

    private static class GestureControl extends GestureDetector {
        public GestureControl(OrthographicCamera camera){
            super(new GestureDetectorControl(camera));
        }

        static class GestureDetectorControl extends GestureAdapter{
            private OrthographicCamera camera;
            //private float currentZoom;

            public GestureDetectorControl(OrthographicCamera camera){
                this.camera = camera;
                currentZoom = camera.zoom;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                //camera.translate(-deltaX * currentZoom,deltaY * currentZoom);
                //camera.update();
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                float zoom = (initialDistance / distance) * currentZoom;

                if (zoom < 1){
                    zoom = 1;
                }else if (zoom > 4){
                    zoom = 4;
                }

                camera.zoom = zoom;
                camera.update();

                return true;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                currentZoom = camera.zoom;
                return false;
            }
        }
    }

    private static class InputControl extends InputAdapter{
        private OrthographicCamera camera;
        private float factorMovimientoGesture = 5.5f;

        public InputControl(OrthographicCamera camera){
            this.camera = camera;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            float x = (Gdx.input.getDeltaX()/factorMovimientoGesture)*currentZoom;
            float y = (Gdx.input.getDeltaY()/factorMovimientoGesture)*currentZoom;

            camera.translate(-x,y);
            camera.update();

            return true;
        }
    }

    public void update(float delta) {

        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            following = !following;
        }

        if (following) {
            camera.position.x = target.getPosition().x;
            camera.position.y = target.getPosition().y;
        } else {
            if (Gdx.input.isKeyPressed(Keys.A)) {
                camera.position.x -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Keys.D)) {
                camera.position.x += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Keys.W)) {
                camera.position.y += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Keys.S)) {
                camera.position.y -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
        }
    }
}
