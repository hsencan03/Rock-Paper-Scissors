package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.util.Log;
import istanbul.gamelab.ngdroid.util.Utils;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {

    private static final int ROCK = 0;
    private static final int PAPER = 1;
    private static final int SCISSORS = 2;

    private Bitmap[] images;
    private int imagecount, imagew, imageh;
    private int playerPosx, playerPosy;

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
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        canvas.drawARGB(255, 0, 0, 0);
        canvas.drawBitmap(images[ROCK], (getWidth() - imagew) >> 1, (getHeight() - imageh) >> 1, null);
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
    }

    public void touchMove(int x, int y, int id) {
    }

    public void touchUp(int x, int y, int id) {
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
