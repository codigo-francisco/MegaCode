package com.rockbass2560.megacode.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.rockbass2560.megacode.GameplayScreen;
import com.rockbass2560.megacode.Level;
import com.rockbass2560.megacode.entities.Enemy;
import com.rockbass2560.megacode.entities.ExitPortal;
import com.rockbass2560.megacode.entities.MegaCode;
import com.rockbass2560.megacode.entities.Platform;
import com.rockbass2560.megacode.entities.Powerup;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Comparator;


public class LevelLoader {

    public static final String TAG = LevelLoader.class.toString();


    public static Level load(String path) {
        Level level = new Level();


        FileHandle file = Gdx.files.internal(path);

        JSONParser parser = new JSONParser();
        JSONObject rootJsonObject;

        try {
            rootJsonObject = (JSONObject) parser.parse(file.reader());

            JSONObject composite = (JSONObject) rootJsonObject.get(Constants.LEVEL_COMPOSITE);

            JSONArray platforms = (JSONArray) composite.get(Constants.LEVEL_9PATCHES);
            loadPlatforms(platforms, level);

            JSONArray nonPlatformObjects = (JSONArray) composite.get(Constants.LEVEL_IMAGES);
            loadNonPlatformEntities(level, nonPlatformObjects);

        } catch (Exception ex) {
            Gdx.app.log(TAG, ex.getMessage());
            Gdx.app.log(TAG, Constants.LEVEL_ERROR_MESSAGE);
        }

        return level;
    }

    private static Vector2 extractXY(JSONObject object) {

        Number x = (Number) object.get(Constants.LEVEL_X_KEY);
        Number y = (Number) object.get(Constants.LEVEL_Y_KEY);

        return new Vector2(
                (x == null) ? 0 : x.floatValue(),
                (y == null) ? 0 : y.floatValue()
        );
    }

    private static void loadNonPlatformEntities(Level level, JSONArray nonPlatformObjects) {
        for (Object o : nonPlatformObjects) {
            JSONObject item = (JSONObject) o;

            final Vector2 imagePosition = extractXY(item);

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a powerup at " + powerupPosition);
                level.getPowerups().add(new Powerup(powerupPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.STANDING_RIGHT)) {
                final Vector2 gigaGalPosition = imagePosition.add(Constants.GIGAGAL_EYE_POSITION);
                Gdx.app.log(TAG, "Loaded MegaCode at " + gigaGalPosition);
                MegaCode megaCode = new MegaCode(gigaGalPosition, level);
                level.setMegaCode(megaCode);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.EXIT_PORTAL_SPRITE_1)) {
                final Vector2 exitPortalPosition = imagePosition.add(Constants.EXIT_PORTAL_CENTER);
                Gdx.app.log(TAG, "Loaded the exit portal at " + exitPortalPosition);
                level.setExitPortal(new ExitPortal(exitPortalPosition));
            }

        }
    }


    private static void loadPlatforms(JSONArray array, Level level) {

        Array<Platform> platformArray = new Array<Platform>();

        for (Object object : array) {
            final JSONObject platformObject = (JSONObject) object;

            Vector2 bottomLeft = extractXY(platformObject);

            final float width = ((Number) platformObject.get(Constants.LEVEL_WIDTH_KEY)).floatValue();
            final float height = ((Number) platformObject.get(Constants.LEVEL_HEIGHT_KEY)).floatValue();


            final Platform platform = new Platform(bottomLeft.x, bottomLeft.y + height, width, height);

            platformArray.add(platform);


            final String identifier = (String) platformObject.get(Constants.LEVEL_IDENTIFIER_KEY);


            if (identifier != null && identifier.equals(Constants.LEVEL_ENEMY_TAG)) {
                final Enemy enemy = new Enemy(platform);
                final Enemy originalEnemy = new Enemy(platform);
                level.getEnemies().add(enemy);
                level.originalEnemies.add(originalEnemy);
            }
        }


        platformArray.sort(new Comparator<Platform>() {
            @Override
            public int compare(Platform o1, Platform o2) {
                if (o1.top < o2.top) {
                    return 1;
                } else if (o1.top > o2.top) {
                    return -1;
                }
                return 0;
            }
        });

        level.getPlatforms().addAll(platformArray);

    }

}
