package com.rockbass2560.megacode.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.rockbass2560.megacode.entities.MegaCode;

public class ChaseCam {

    public static final String TAG = ChaseCam.class.getName();

    public OrthographicCamera camera;
    public MegaCode target;
    private Boolean following;
    public float zoom;

    public ChaseCam() {
        following = false;
    }

    public void resetCameraPosition(boolean following){
        camera.position.x = target.getPosition().x;
        camera.position.y = target.getPosition().y;
        this.following = following;
        camera.zoom = this.zoom;
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
