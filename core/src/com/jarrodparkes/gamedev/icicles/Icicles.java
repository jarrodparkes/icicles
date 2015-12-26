package com.jarrodparkes.gamedev.icicles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jarrodparkes.gamedev.icicles.Constants.Difficulty;

/**
 * Created by jarrodparkes on 12/22/15.
 */
public class Icicles {

    public static final String TAG = Icicles.class.getName();

    Difficulty difficulty;

    DelayedRemovalArray<Icicle> icicles;
    Viewport viewport;
    int iciclesDodged = 0;

    public Icicles(Viewport viewport, Difficulty difficulty) {
        this.viewport = viewport;
        this.difficulty = difficulty;
        init();
    }

    public void init() {
        icicles = new DelayedRemovalArray<Icicle>(false, 100);
        iciclesDodged = 0;
    }

    public void update(float delta) {
        if (MathUtils.random() < delta * difficulty.spawnRate) {
            Vector2 newIciclePosition = new Vector2(
                    MathUtils.random() * viewport.getWorldWidth(),
                    viewport.getWorldHeight()
            );
            Icicle newIcicle = new Icicle(newIciclePosition);
            icicles.add(newIcicle);
        }

        for (Icicle icicle : icicles) {
            icicle.update(delta);
        }

        icicles.begin();
        for (int i = 0; i < icicles.size; i++) {
            if (icicles.get(i).position.y < -Constants.ICICLES_HEIGHT) {
                icicles.removeIndex(i);
                iciclesDodged++;
            }
        }
        icicles.end();
    }

    public void render(ShapeRenderer renderer) {
        renderer.setColor(Constants.ICICLE_COLOR);
        for (Icicle icicle : icicles) {
            icicle.render(renderer);
        }
    }
}
