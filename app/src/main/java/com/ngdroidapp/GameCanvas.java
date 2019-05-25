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

    private static int STATE_PLAY = 0;
    private static int STATE_END = 0;

    private static final int NONE = -1;
    private static final int ROCK = 0;
    private static final int PAPER = 1;
    private static final int SCISSORS = 2;

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
    private Rect fontbounds;

    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {
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
        int offset = ((getWidth() / imagecount) - imagew);
        for(int i = 0; i < imagecount; i++) {
            playerx[i] = (imagew + offset) * i + (offset >> 1);
            playery[i] = getHeight() - imageh - 100;
            enemyx[i] = playerx[i];
            enemyy[i] = 100;
        }
        playerselectedposx = (getWidth() - imagew) >> 1;
        playerselectedposy = getHeight() - imageh - 700;
        enemyselectedposx = playerselectedposx;
        enemyselectedposy = 700;

        playerindex = NONE;
        selectedfont = new Paint();
        selectedfont.setARGB(128, 255, 255, 255);

        dx = 0;
        dy = 0;
        angle = 0;
        speed = 20;

        rand = new Random();
        strings = new String[] {"PLAYER WIN", "TIED", "ENEMYWIN"};
        gamefont = new Paint();
        gamefont.setTextSize(60);
    }

    public void update() {
        if(playerindex != NONE && playerselected) {
            calculateDirection(true);
            playerx[playerindex] += dx * speed;
            playery[playerindex] += dy * speed;
            if(Math.abs(playerx[playerindex] - playerselectedposx) < speed && Math.abs(playery[playerindex] - playerselectedposy) < speed) {
                dx = dy = 0;
                playerindex = NONE;
                enemyindex = rand.nextInt(3);
                enemyselected = true;
            }
        } else if(enemyindex != NONE && enemyselected) {
            calculateDirection(false);
            enemyx[enemyindex] += dx * speed;
            enemyy[enemyindex] += dy * speed;
            if(Math.abs(enemyx[enemyindex] - enemyselectedposx) < speed && Math.abs(enemyy[enemyindex] - enemyselectedposy) < speed) {
                dx = dy = 0;
                enemyindex = NONE;
            }
        } else if(playerselected && enemyselected) {
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawARGB(255, 0, 0, 0);
        for(int i = 0; i < imagecount; i++) {
            canvas.drawBitmap(images[i], playerx[i], playery[i], playerindex == i ? selectedfont : null);
            canvas.drawBitmap(images[i], enemyx[i], enemyy[i], null);
        }
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
        if(playerselected) return;
        for(int i = 0; i < imagecount; i++) {
            if(x >= playerx[i] && x < playerx[i] + imagew && y >= playery[i] && y < playery[i] + imageh && images[i].getPixel(x - playerx[i], y - playery[i]) != Color.TRANSPARENT) {
                playerindex = i;
            }
        }
    }

    public void touchMove(int x, int y, int id) {
    }

    public void touchUp(int x, int y, int id) {
        if(playerselected) return;
        if (playerindex != NONE) {
            if (x >= playerx[playerindex] && x < playerx[playerindex] + imagew &&
                    y >= playery[playerindex] && y < playery[playerindex] + imageh && images[playerindex].getPixel(x - playerx[playerindex], y - playery[playerindex]) != Color.TRANSPARENT) {
                playerselected = true;
                return;
            }
        }
        dx = dy = 0;
        playerindex = NONE;
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
