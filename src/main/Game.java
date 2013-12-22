package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import java.util.Random;

public class Game implements Entity {
    private Texture
        background = new Texture("gfx/background.png"),
        green = new Texture("gfx/green.png"),
        red = new Texture("gfx/red.png"),
        down = new Texture("gfx/down.png"),
        downLeft = new Texture("gfx/down_left.png"),
        downRight = new Texture("gfx/down_right.png"),
        left = new Texture("gfx/left.png"),
        leftRight = new Texture("gfx/left_right.png"),
        leftDown = new Texture("gfx/left_down.png"),
        right = new Texture("gfx/right.png"),
        rightLeft = new Texture("gfx/right_left.png"),
        rightDown = new Texture("gfx/right_down.png");
//    private Music
//            music = Gdx.audio.newMusic(Gdx.files.internal("sfx/gbu.mp3")),
//            victory = Gdx.audio.newMusic(Gdx.files.internal("sfx/victory.mp3")),
//            go = Gdx.audio.newMusic(Gdx.files.internal("sfx/go.mp3"));
    private Sound
        fail = Gdx.audio.newSound(Gdx.files.internal("sfx/fail.mp3")),
        shoot = Gdx.audio.newSound(Gdx.files.internal("sfx/shoot.mp3")),
        richo = Gdx.audio.newSound(Gdx.files.internal("sfx/richo.mp3"));
    private boolean
        showIntro = true,
        end = false;
    private float[]
        x = { 100f, 310f, 630f },
        y = { 100f, 0f, 100f };
    private int[] direction = {0, 0, 0};
    private boolean[] alive = {true, true, true};
    private long[]
        wait = {0l, 0l, 0l},
        keyDelay = {0l, 0l, 0l};
    private Texture[][] playerTexture = {
        {left, leftDown, leftRight},
        {downLeft, down, downRight},
        {rightLeft, rightDown, right}
    };
    private long
        shootTime,
        endTime = 0l;
    private Random rand = new Random();
    private Intro intro = new Intro();

    private void roundStart() {
        shootTime = System.currentTimeMillis() + 5000 + rand.nextInt(25000);
//        victory.stop();
        end = false;
        for (int i = 0; i < 3; i++) {
            alive[i] = true;
            direction[i] = 0;
            wait[i] = 0l;
        }
//        music.play();
    }

    public boolean keyDown(int player) {
        boolean result = false;
        Array<Controller> controllers = Controllers.getControllers();

        if (controllers.size > player) {
            Controller controller = controllers.get(player);
            return controller.getButton(11) || controller.getButton(12) || controller.getButton(13);
        }

        switch (player) {
            case 0:
                result = Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.A);
                break;
            case 1:
                result = Gdx.input.isKeyPressed(Input.Keys.G) || Gdx.input.isKeyPressed(Input.Keys.H);
                break;
            case 2:
                result = Gdx.input.isKeyPressed(Input.Keys.L) || Gdx.input.isKeyPressed(Input.Keys.P);
                break;
        }

        return result;
    }

    public int direction(int player) {
        int result = -1;

        Array<Controller> controllers = Controllers.getControllers();

        if (controllers.size > player) {
            Controller controller = controllers.get(player);

            switch (player) {
                case 0:
                    result = controller.getButton(12) ? 2 : 1;
                    break;
                case 1:
                    result = controller.getButton(13) ? 0 : 2;
                    break;
                case 2:
                    result = controller.getButton(13) ? 0 : 1;
                    break;
            }
        } else {
            switch (player) {
                case 0:
                    result = (Gdx.input.isKeyPressed(Input.Keys.Q)) ? 2 : 1;
                    break;
                case 1:
                    result = (Gdx.input.isKeyPressed(Input.Keys.G)) ? 0 : 2;
                    break;
                case 2:
                    result = (Gdx.input.isKeyPressed(Input.Keys.P)) ? 0 : 1;
                    break;
            }
        }

        return result;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (showIntro) {
            intro.render(spriteBatch);

            if (keyDown(0) || keyDown(1) || keyDown(2)) {
                intro.stop();

                showIntro = false;
                for (int i = 0; i < 3; i++) {
                    keyDelay[i] = System.currentTimeMillis() + 500;
                }
                roundStart();
            }
        } else {
            spriteBatch.draw(background, 0, -190);

            if (shootTime > System.currentTimeMillis()) {
                spriteBatch.draw(red, 480f, 300f, 70f, 104f);
            } else {
                spriteBatch.draw(green, 480f, 300f, 70f, 104f);
//                if (music.isPlaying()) {
//                    music.stop();
//                        go.play();
//                }
            }

            for (int i = 0; i < 3; i++) {
                if (alive[i]) {
                    if (wait[i] < System.currentTimeMillis()) {
                        direction[i] = i;
                    }

                    if (keyDelay[i] < System.currentTimeMillis()) {
                        if (keyDown(i) && !end) {
                            direction[i] = direction(i);

                            if (System.currentTimeMillis() < shootTime) {
                                keyDelay[i] = System.currentTimeMillis() + 800;
                                richo.play();
                                wait[i] = System.currentTimeMillis() + 2000;
                            } else if (wait[i] < System.currentTimeMillis()) {
                                if (alive[direction[i]]) {
                                    alive[direction[i]] = false;
                                    wait[i] = System.currentTimeMillis() + 400;
                                    shoot.play();
                                } else {
                                    richo.play();
                                    wait[i] = System.currentTimeMillis() + 2000;
                                }

                                keyDelay[i] = System.currentTimeMillis() + 250;
                            } else {
                                fail.play();
                                keyDelay[i] = System.currentTimeMillis() + 250;
                                wait[i] = System.currentTimeMillis() + 2000;
                            }
                        }
                    }

                    spriteBatch.draw(playerTexture[i][direction[i]], x[i], y[i]);
                }
            }

            if ((alive[0] == false && alive[1] == false) || (alive[0] == false && alive[2] == false) || (alive[1] == false && alive[2] == false)) {
                if (!end) {
                    end = true;
                    endTime = System.currentTimeMillis() + 3000;
//                    go.stop();
//                    victory.play();
                }

                if (endTime < System.currentTimeMillis() && (keyDown(0) || keyDown(1) || keyDown(2))) {
                    for (int i = 0; i < 3; i++) {
                        keyDelay[i] = System.currentTimeMillis() + 500;
                    }

                    roundStart();
                }
            }
        }
    }
}
