package com.megacode.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.megacode.entities.GigaGal;

public class ChaseCam extends GestureDetector.GestureAdapter {

    public static final String TAG = ChaseCam.class.getName();

    public OrthographicCamera camera;
    public GigaGal target;
    public Boolean following;
    private final static float MOVEMENT = .5f;
    private float currentZoom;

    public ChaseCam() {
        following = false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        camera.translate(-deltaX * currentZoom * MOVEMENT,deltaY * currentZoom * MOVEMENT);

        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {

        camera.zoom = (initialDistance / distance) * currentZoom;
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        currentZoom = camera.zoom;
        return true;
    }

    public float initialZoom;

    public void resetCameraPosition(boolean following){
        camera.position.x = target.getPosition().x;
        camera.position.y = target.getPosition().y;
        this.following = following;
        camera.zoom = initialZoom;
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
