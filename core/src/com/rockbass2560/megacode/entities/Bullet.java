package com.rockbass2560.megacode.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.rockbass2560.megacode.Level;
import com.rockbass2560.megacode.util.Assets;
import com.rockbass2560.megacode.util.Constants;
import com.rockbass2560.megacode.util.Enums.Direction;
import com.rockbass2560.megacode.util.Utils;

public class Bullet {

    private final Direction direction;
    private final Level level;
    public boolean active;
    private Vector2 position;

    public Bullet(Level level, Vector2 position, Direction direction) {
        this.level = level;
        this.position = position;
        this.direction = direction;
        active = true;
    }

    public void update(float delta) {
        switch (direction) {
            case LEFT:
                position.x -= delta * Constants.BULLET_MOVE_SPEED;
                break;
            case RIGHT:
                position.x += delta * Constants.BULLET_MOVE_SPEED;
                break;
        }

        for (Enemy enemy : level.getEnemies()) {
            if (position.dst(enemy.position) < Constants.ENEMY_SHOT_RADIUS) {
                level.spawnExplosion(position);
                active = false;
                enemy.health -= 1;
                level.score += Constants.ENEMY_HIT_SCORE;
            }
        }

        final float worldWidth = level.getViewport().getWorldWidth();
        final float cameraX = level.getViewport().getCamera().position.x;

        if (position.x < cameraX - worldWidth / 2 || position.x > cameraX + worldWidth / 2) {
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion region = Assets.instance.bulletAssets.bullet;
        Utils.dr