package com.rockbass2560.megacode;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rockbass2560.megacode.entities.Bullet;
import com.rockbass2560.megacode.entities.Enemy;
import com.rockbass2560.megacode.entities.ExitPortal;
import com.rockbass2560.megacode.entities.Explosion;
import com.rockbass2560.megacode.entities.MegaCode;
import com.rockbass2560.megacode.entities.Platform;
import com.rockbass2560.megacode.entities.Powerup;
import com.rockbass2560.megacode.util.ChaseCam;
import com.rockbass2560.megacode.util.Constants;
import com.rockbass2560.megacode.util.Enums.Direction;

import java.util.ArrayDeque;
import java.util.Queue;

public class Level {

    public static final String TAG = Level.class.getName();
    public boolean gameOver;
    public boolean victory;
    public boolean enemyInFront = false;
    public boolean recibirComandos = true;
    public Viewport viewport;
    public int score;
    public MegaCode megaCode;
    public ChaseCam cam;
    private ExitPortal exitPortal;
    private Array<Platform> platforms;
    private DelayedRemovalArray<Enemy> enemies;
    public Array<Enemy> originalEnemies;
    private DelayedRemovalArray<Bullet> bullets;
    private DelayedRemovalArray<Explosion> explosions;
    private DelayedRemovalArray<Powerup> powerups;

    public Level() {
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE, new OrthographicCamera());

        megaCode = new MegaCode(new Vector2(50, 50), this);
        platforms = new Array<Platform>();
        enemies = new DelayedRemovalArray<Enemy>();
        originalEnemies = new Array<>();
        bullets = new DelayedRemovalArray<Bullet>();
        explosions = new DelayedRemovalArray<Explosion>();
        powerups = new DelayedRemovalArray<Powerup>();
        exitPortal = new ExitPortal(new Vector2(200, 200));

        gameOver = false;
        victory = false;
        score = 0;
    }

    public static Level debugLevel() {
        Level level = new Level();
        level.initializeDebugLevel();
        return level;
    }

    //public Queue<Comando> comandos = new LinkedList<>();

    private final static float secondsMovements = 1.2f;
    private final static float jumpTime = .1f;
    private final static float jumpMovements = 1f;
    private final static float shootTime = .1f;
    private Queue<Timer.Task> tasks = new ArrayDeque<>();
    private Comando lastMovement = Comando.NADA;

    private void eliminarTasksPendientes(){
        while(tasks.size() > 0){
            Timer.Task task = tasks.poll();
            task.cancel();
        }

        recibirComandos = true;
    }

    public void prepararNivel(){
        eliminarTasksPendientes();
        //Reposicionar al personaje principal
        megaCode.respawn();
        megaCode.respawn = false;

        //Eliminar enemigos restantes y posicionar los originales de nuevo
        enemies.begin();
        enemies.clear();
        for (Enemy enemy : originalEnemies){
            //Hay que crear objetos nuevos, elimina por referencia y no vuelve a agregar
            //En este caso clono el objeto original
            enemies.add((Enemy)enemy.clone());
        }
        enemies.end();

        cam.resetCameraPosition(true);
    }

    public void procesarComando(Comando comando){
        if (recibirComandos) {
            if (comando == Comando.CAMINAR_DERECHA) {
                recibirComandos = false;
                megaCode.rightButtonPressed = true;

                tasks.add(Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if (megaCode.justDied) {
                            megaCode.justDied = false;
                            eliminarTasksPendientes();
                            //comandos.clear();
                        }
                        megaCode.rightButtonPressed = false;
                        recibirComandos = true;
                        lastMovement = Comando.CAMINAR_DERECHA;
                    }
                }, secondsMovements));

            } else if (comando == Comando.CAMINAR_IZQUIERDA) {
                megaCode.leftButtonPressed = true;
                recibirComandos = false;

                tasks.add(Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if (megaCode.justDied) {
                            megaCode.justDied = false;
                            eliminarTasksPendientes();
                            //comandos.clear();
                        }
                        megaCode.leftButtonPressed = false;
                        recibirComandos = true;
                        lastMovement = Comando.CAMINAR_IZQUIERDA;
                    }
                }, secondsMovements));

            } else if (comando == Comando.DISPARAR) {
                recibirComandos = false;
                megaCode.shoot();
                tasks.add(Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if (megaCode.justDied) {
                            megaCode.justDied = false;
                            eliminarTasksPendientes();
                            //comandos.clear();
                        }
                        recibirComandos = true;
                    }
                }, shootTime));

            } else if (comando == Comando.SALTAR) {
                megaCode.jumpButtonPressed = true;
                recibirComandos = false;

                if (lastMovement == Comando.CAMINAR_DERECHA) {
                    megaCode.rightButtonPressed = true;
                } else if (lastMovement == Comando.CAMINAR_IZQUIERDA) {
                    megaCode.leftButtonPressed = true;
                }

                tasks.add(Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        megaCode.jumpButtonPressed = false;
                    }
                }, jumpTime));

                tasks.add(Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if (megaCode.justDied) {
                            megaCode.justDied = false;
                            eliminarTasksPendientes();
                            //comandos.clear();
                        }
                        megaCode.rightButtonPressed = false;
                        megaCode.leftButtonPressed = false;
                        recibirComandos = true;
                    }
                }, jumpMovements));
            }
        }
    }

    public void update(float delta) {

        if (megaCode.getPosition().dst(exitPortal.position) < Constants.EXIT_PORTAL_RADIUS) {
            victory = true;
        }

        enemyInFront = false;
        for(Enemy enemy : enemies){
            if (megaCode.getPosition().dst(enemy.position) < Constants.ENEMY_INFRONT_RADIUS){
                enemyInFront = true;
            }
        }

        if (!gameOver && !victory) {

            megaCode.update(delta, platforms);

            // Update Bullets
            bullets.begin();
            for (Bullet bullet : bullets) {
                bullet.update(delta);
                if (!bullet.active) {
                    bullets.removeValue(bullet, false);
                }
            }
            bullets.end();

            // Update Enemies
            enemies.begin();
            for (int i = 0; i < enemies.size; i++) {
                Enemy enemy = enemies.get(i);
                enemy.update(delta);
                if (enemy.health < 1) {
                    spawnExplosion(enemy.position);
                    enemies.removeIndex(i);
                    score += Constants.ENEMY_KILL_SCORE;
                }
            }
            enemies.end();

            // Update Explosions
            explosions.begin();
            for (int i = 0; i < explosions.size; i++) {
                if (explosions.get(i).isFinished()) {
                    explosions.removeIndex(i);
                }
            }
            explosions.end();
        }

    }

    public void render(SpriteBatch batch) {

        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        for (Platform platform : platforms) {
            platform.render(batch);
        }

        exitPortal.render(batch);

        for (Powerup powerup : powerups) {
            powerup.render(batch);
        }

        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }
        megaCode.render(batch);

        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }

        for (Explosion explosion : explosions) {
            explosion.render(batch);
        }

        batch.end();
    }

    private void initializeDebugLevel() {

        megaCode = new MegaCode(new Vector2(15, 40), this);

        exitPortal = new ExitPortal(new Vector2(150, 150));

        platforms = new Array<Platform>();
        bullets = new DelayedRemovalArray<Bullet>();
        enemies = new DelayedRemovalArray<Enemy>();
        explosions = new DelayedRemovalArray<Explosion>();
        powerups = new DelayedRemovalArray<Powerup>();


        platforms.add(new Platform(15, 100, 30, 20));

        Platform enemyPlatform = new Platform(75, 90, 100, 65);

        enemies.add(new Enemy(enemyPlatform));

        platforms.add(enemyPlatform);
        platforms.add(new Platform(35, 55, 50, 20));
        platforms.add(new Platform(10, 20, 20, 9));

        powerups.add(new Powerup(new Vector2(20, 110)));
    }


    public Array<Platform> getPlatforms() {
        return platforms;
    }

    public DelayedRemovalArray<Enemy> getEnemies() {
        return enemies;
    }

    public DelayedRemovalArray<Powerup> getPowerups() {
        return powerups;
    }

    public ExitPortal getExitPortal() {
        return exitPortal;
    }

    public void setExitPortal(ExitPortal exitPortal) {
        this.exitPortal = exitPortal;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public MegaCode getMegaCode() {
        return megaCode;
    }

    public void setMegaCode(MegaCode megaCode) {
        this.megaCode = megaCode;
    }

    public void spawnBullet(Vector2 position, Direction direction) {
        bullets.add(new Bullet(this, position, direction));
    }

    public void spawnExplosion(Vector2 position) {
        explosions.add(new Explosion(position));
    }
}
