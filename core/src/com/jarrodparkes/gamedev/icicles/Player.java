package com.jarrodparkes.gamedev.icicles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by jarrodparkes on 12/22/15.
 */
public class Player extends InputAdapter {

    public static final String TAG = Player.class.getName();

    Vector2 position;
    Viewport viewport;
    IciclesGame game;
    int deaths = 0;

    boolean moveLeft = false;
    boolean moveRight = false;

    public Player(IciclesGame game, Viewport viewport) {
        this.game = game;
        this.viewport = viewport;
        deaths = 0;
        init();
    }

    public void init() {
        position = new Vector2(viewport.getWorldWidth() / 2, Constants.PLAYER_HEAD_HEIGHT);
    }

    public void render(ShapeRenderer renderer) {

        renderer.setColor(Constants.PLAYER_COLOR);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.circle(position.x, position.y, Constants.PLAYER_HEAD_RADIUS, Constants.PLAYER_HEAD_SEGMENTS);

        Vector2 torsoTop = new Vector2(position.x, position.y - Constants.PLAYER_HEAD_RADIUS);
        Vector2 torsoBottom = new Vector2(torsoTop.x, torsoTop.y - 2 * Constants.PLAYER_HEAD_RADIUS);

        renderer.rectLine(torsoTop, torsoBottom, Constants.PLAYER_LIMB_WIDTH);
        renderer.rectLine(
                torsoTop.x, torsoTop.y,
                torsoTop.x + Constants.PLAYER_HEAD_RADIUS, torsoTop.y - Constants.PLAYER_HEAD_RADIUS, Constants.PLAYER_LIMB_WIDTH);
        renderer.rectLine(
                torsoTop.x, torsoTop.y,
                torsoTop.x - Constants.PLAYER_HEAD_RADIUS, torsoTop.y - Constants.PLAYER_HEAD_RADIUS, Constants.PLAYER_LIMB_WIDTH);
        renderer.rectLine(
                torsoBottom.x, torsoBottom.y,
                torsoBottom.x + Constants.PLAYER_HEAD_RADIUS, torsoBottom.y - Constants.PLAYER_HEAD_RADIUS, Constants.PLAYER_LIMB_WIDTH);
        renderer.rectLine(
                torsoBottom.x, torsoBottom.y,
                torsoBottom.x - Constants.PLAYER_HEAD_RADIUS, torsoBottom.y - Constants.PLAYER_HEAD_RADIUS, Constants.PLAYER_LIMB_WIDTH);
    }

    public void update(float delta) {
        // movement
        if (moveLeft) {
            position.x -= delta * Constants.PLAYER_MOVEMENT_SPEED;
        }
        if (moveRight) {
            position.x += delta * Constants.PLAYER_MOVEMENT_SPEED;
        }

        // accelerometer movement
        float yAxisInput = -Gdx.input.getAccelerometerY() / (Constants.ACCELEROMETER_SENSITIVITY * Constants.ACCELERATION_OF_GRAVITY);
        position.x += -yAxisInput * Constants.PLAYER_MOVEMENT_SPEED * delta;

        keepInBounds();
    }

    public boolean hitByIcicle(Icicles icicles) {
        boolean isHit = false;
        for (Icicle icicle : icicles.icicles) {
            if (icicle.position.dst(position) < Constants.PLAYER_HEAD_RADIUS) {
                isHit = true;
            }
        }
        if (isHit) {
            deaths++;
        }
        return isHit;
    }

    private void keepInBounds() {
        if (position.x - Constants.PLAYER_HEAD_RADIUS < 0) {
            position.x = Constants.PLAYER_HEAD_RADIUS;
        }
        if (position.x + Constants.PLAYER_HEAD_RADIUS > viewport.getWorldWidth()) {
            position.x = viewport.getWorldWidth() - Constants.PLAYER_HEAD_RADIUS;
        }
    }

    @Override
    public boolean keyDown (int keycode) {
        switch (keycode) {
            case Keys.LEFT:
                moveLeft = true;
                break;
            case Keys.RIGHT:
                moveRight = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp (int keycode) {
        switch (keycode) {
            case Keys.LEFT:
                moveLeft = false;
                break;
            case Keys.RIGHT:
                moveRight = false;
                break;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        game.showDifficultyScreen();
        return true;
    }
}
