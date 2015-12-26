package com.jarrodparkes.gamedev.icicles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jarrodparkes.gamedev.icicles.Constants.Difficulty;

/**
 * Created by jarrodparkes on 12/22/15.
 */
public class IciclesScreen extends InputAdapter implements Screen {

    IciclesGame game;
    Difficulty difficulty;
    Player player;

    Icicles icicles;
    int highScore = 0;

    // world viewport
    ShapeRenderer renderer;
    ExtendViewport viewport;

    // HUD viewport
    SpriteBatch batch;
    ScreenViewport textViewport;
    BitmapFont font;

    public IciclesScreen(IciclesGame game, Difficulty difficulty) {
        this.game = game;
        this.difficulty = difficulty;
    }

    @Override
    public void show () {
        // setup world
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        player = new Player(game, viewport);
        Gdx.input.setInputProcessor(player);
        icicles = new Icicles(viewport, difficulty);
        // setup HUD
        batch = new SpriteBatch();
        textViewport = new ScreenViewport();
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void resize (int width, int height) {
        viewport.update(width, height, true);
        textViewport.update(width, height, true);
        font.getData().setScale(Math.min(width, height)/Constants.HUD_FONT_REFERENCE_SCREEN_SIZE);
        player.init();
        icicles.init();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void render (float delta) {
        // update values related to drawing
        player.update(delta);
        icicles.update(delta);
        if (player.hitByIcicle(icicles)) {
            icicles.init();
        }
        highScore = Math.max(highScore, icicles.iciclesDodged);

        // clear screen
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // setup HUD drawing
        textViewport.apply();
        batch.setProjectionMatrix(textViewport.getCamera().combined);
        // draw HUD
        batch.begin();
        font.draw(batch, "Deaths: " + player.deaths + "\nDifficulty: " + difficulty.label,
                Constants.HUD_MARGIN, textViewport.getWorldHeight() - Constants.HUD_MARGIN);
        font.draw(batch, "Score: " + icicles.iciclesDodged + "\nTop Score: " + highScore,
                textViewport.getWorldWidth() - Constants.HUD_MARGIN, textViewport.getWorldHeight() - Constants.HUD_MARGIN,
                0, Align.right, false);
        batch.end();

        // setup world drawing
        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        // draw world
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        player.render(renderer);
        icicles.render(renderer);
        renderer.end();
    }

    @Override
    public void dispose () {
        renderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
