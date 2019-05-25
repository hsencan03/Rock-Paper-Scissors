package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.util.Utils;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {

    private static final int STATE_PLAY = 0;
    private static final int STATE_END = 1;

    private static final int NONE = -1;
    private static final int ROCK = 0;
    private static final int PAPER = 1;
    private static final int SCISSORS = 2;

    private static final int TIED = 0;
    private static final int WIN = 1;
    private static final int LOSE = 2;

    private int currentstate, status;

    private Bitmap[] images;
    private int imagecount, imagew, imageh;
    private int[] playerx, playery, enemyx, enemyy;
    private int playerselectedposx, playerselectedposy, playerindex, enemyselectedposx, enemyselectedposy, enemyindex;
    private Paint selectedfont;

    private double dx, dy, angle;
    private int speed;
    private boolean playerselected, enemyselected;

    private Random rand;
    private String[] strings;
    private Paint gamefont;
    private Rect textbounds;

    private int enemyanimno, enemyanimcounter;
    private long gametimer, gametime, enemytimer, enemytime;


    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {
        currentstate = STATE_PLAY;

        imagecount = 3;
        images = new Bitmap[imagecount];
        images[ROCK] = Utils.loadImage(root, "rock.png");
        images[PAPER] = Utils.loadImage(root, "paper.png");
        images[SCISSORS] = Utils.loadImage(root, "scissors.png");
        imagew = images[ROCK].getWidth();
        imageh = images[ROCK].getHeight();

        playerx = new int[imagecount];
        playery = new int[imagecount];
        enemyx = new int[imagecount];
        enemyy = new int[imagecount];
        int offset = ((getUnitWidth() / imagecount) - imagew);
        for(int i = 0; i < imagecount; i++) {
            playerx[i] = (imagew + offset) * i + (offset >> 1);
            playery[i] = getUnitHeight() - imageh - 100;
            enemyx[i] = playerx[i];
            enemyy[i] = 100;
        }
        playerselectedposx = (getUnitWidth() - imagew) >> 1;
        playerselectedposy = getUnitHeight() - imageh - 700;
        enemyselectedposx = playerselectedposx;
        enemyselectedposy = 600;

        playerindex = NONE;
        enemyindex = NONE;
        selectedfont = new Paint();
        selectedfont.setARGB(128, 255, 255, 255);

        dx = 0;
        dy = 0;
        angle = 0;
        speed = 30;

        rand = new Random();
        strings = new String[] {"TIED", "PLAYER WIN", "ENEMY WIN"};
        gamefont = new Paint();
        gamefont.setTextSize(156);
        gamefont.setColor(Color.WHITE);
        textbounds = new Rect();

        enemyanimno = -1;
        enemyanimcounter = 10;

        gametime = 2000;
        gametimer = 0;
        enemytime = 2500;
        enemytimer = 0;
    }

    public void update() {
        switch (currentstate) {
            case STATE_PLAY:
                playerUpdate();
                enemyUpdate();
                checkWinner();
                break;
            case STATE_END:
                restartGame();
                break;
        }
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.scale(getWidth() / 1080.0f, getHeight() / 1920.0f);
        switch (currentstate) {
            case STATE_PLAY:
                drawPlayers(canvas);
                break;
            case STATE_END:
                drawPlayers(canvas);
                drawGUI(canvas);
                break;
        }
        canvas.restore();
    }

    private void playerUpdate() {
        if(playerindex != NONE && playerselected) {
            calculateDirection(true);
            playerx[playerindex] += dx * speed;
            playery[playerindex] += dy * speed;
            if(Math.abs(playerx[playerindex] - playerselectedposx) < speed && Math.abs(playery[playerindex] - playerselectedposy) < speed) {
                dx = dy = 0;
                enemyindex = rand.nextInt(3);
                playerselected = false;
                enemyselected = true;
                enemytimer = enemytime + System.currentTimeMillis();
            }
        }
    }

    private void enemyUpdate() {
        if(System.currentTimeMillis() < enemytimer) {
            if(--enemyanimcounter <= 0) {
                if(++enemyanimno >= imagecount) enemyanimno = 0;
                enemyanimcounter = 10;
            }
        }
        else if(enemyindex != NONE && enemyselected) {
            enemyanimno = enemyindex;
            calculateDirection(false);
            enemyx[enemyindex] += dx * speed;
            enemyy[enemyindex] += dy * speed;
            if(Math.abs(enemyx[enemyindex] - enemyselectedposx) < speed && Math.abs(enemyy[enemyindex] - enemyselectedposy) < speed) {
                dx = dy = 0;
                enemyselected = false;
            }
        }
    }

    private void checkWinner() {
        if(playerindex != NONE && enemyindex != NONE && !enemyselected && !playerselected) {
            if (playerindex == enemyindex) status = TIED;
            else if (playerindex == ROCK) status = (enemyindex == PAPER ? LOSE : WIN);
            else if (playerindex == PAPER) status = (enemyindex == SCISSORS ? LOSE : WIN);
            else status = (enemyindex == PAPER ? LOSE : WIN);
            gametimer = gametime + System.currentTimeMillis();
            enemyanimno = -1;
            currentstate = STATE_END;
        }
    }

    private void restartGame() {
        if(System.currentTimeMillis() >= gametimer) {
            root.canvasManager.setCurrentCanvas(new GameCanvas(root));
        }
    }

    private void drawPlayers(Canvas canvas) {
        canvas.drawARGB(255, 0, 0, 0);
        for(int i = 0; i < imagecount; i++) {
            canvas.drawBitmap(images[i], playerx[i], playery[i], (playerindex == i && playerselected) ? selectedfont : null);
            canvas.drawBitmap(images[i], enemyx[i], enemyy[i], enemyanimno == i ? selectedfont : null);
        }
    }

    private void drawGUI(Canvas canvas) {
        gamefont.getTextBounds(strings[status], 0, strings[status].length(), textbounds);
        canvas.drawText(strings[status], (getUnitWidth() - textbounds.right) >> 1, getUnitHeight() >> 1, gamefont);
    }

    private void calculateDirection(boolean isPlayer) {
        double radyan;
        if(isPlayer) radyan =  Math.atan2(playery[playerindex] - playerselectedposy, playerx[playerindex] - playerselectedposx);
        else radyan = Math.atan2(enemyy[enemyindex] - enemyselectedposy, enemyx[enemyindex] - enemyselectedposx);
        angle = (450 + Math.toDegrees(radyan)) % 360;

        dx = -(Math.sin(Math.toRadians(angle)));
        dy = Math.cos(Math.toRadians(angle));
    }

    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return true;
    }

    public void surfaceChanged(int width, int height) {

    }

    public void surfaceCreated() {

    }

    public void surfaceDestroyed() {

    }

    public void touchDown(int x, int y, int id) {
        switch (currentstate) {
            case STATE_PLAY:
                if(playerselected || enemyselected) return;
                for(int i = 0; i < imagecount; i++) {
                    if (x >= playerx[i] && x < playerx[i] + imagew &&
                            y >= playery[i] && y < scaleNum(playery[i] + imageh) && images[i].getPixel(x - playerx[i], y - playery[i]) != Color.TRANSPARENT) {
                        playerindex = i;
                    }
                }
                break;
            case STATE_END:
                break;
        }
    }

    public void touchMove(int x, int y, int id) {
    }

    public void touchUp(int x, int y, int id) {
        switch (currentstate) {
            case STATE_PLAY:
                if(playerselected || enemyselected) return;
                if (playerindex != NONE) {
                    if (x >= scaleNum(playerx[playerindex]) && x < scaleNum(playerx[playerindex] + imagew) &&
                            y >= scaleNum(playery[playerindex]) && y < scaleNum(playery[playerindex] + imageh) && images[playerindex].getPixel(x - playerx[playerindex], y - playery[playerindex]) != Color.TRANSPARENT) {
                        playerselected = true;
                        return;
                    }
                }
                dx = dy = 0;
                playerindex = NONE;
                break;
            case STATE_END:
                break;
        }
    }


    public void pause() {

    }


    public void resume() {

    }


    public void reloadTextures() {

    }


    public void showNotify() {
    }

    public void hideNotify() {
    }

}
