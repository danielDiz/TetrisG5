package com.tetris.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Board {

    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;

    private List<Block> blocks = new ArrayList<Block>();

    private Shape fallingShape;

    private Shape nextShape;

    private static Board instance = null;

    public static final int COLOR_FOR_ALL_FOR_NOW = 0xffffff00;


    //Board instance for use by other classes
    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    //Construct next shape randomly
    public void spawnNextShape() {
        Random r = new Random();
        int index = r.nextInt(7);

        nextShape = new Shape(index);
    }

    //Next shape falls
    public void makeNextShapeFalling() {
        if (fallingShape != null) { //If there is a shape falling add to the array
            for (Block block : fallingShape.getBlocks())
                blocks.add(block);
        }

        fallingShape = nextShape;
        spawnNextShape();
    }

    //Updates the falling shape
    public void update() {
        if (fallingShape.collide()) { //Checks if the falling shape collided
            spawnNextShape();
            makeNextShapeFalling();
        } else {
            fallingShape.update();
            if (!fallingShape.isFalling()) { //If it has collided with something
                Shape layingShape = fallingShape;
                makeNextShapeFalling();
                deleteLinesOf(layingShape);
            }
        }
    }

    //Deletes the lines that the shape is touching
    void deleteLinesOf(Shape shape) {
        List<Integer> deletedLines = new ArrayList<>();

        //For each line the shape touches checks if its completed
        for (Block shapeBlock : shape.getBlocks()) {
            if (lineComplete(shapeBlock.getY())) {
                deletedLines.add(shapeBlock.getY());
                // Remove from blocks all the block belonging to the same line.
                for (Iterator<Block> itr = blocks.iterator(); itr.hasNext(); ) {
                    Block block = itr.next();
                    if (block.getY() == shapeBlock.getY()) //Remove block from Board if its in the line
                        itr.remove();
                }
            }
        }
        for (Block block : blocks) {
            int count = 0;
            for (int y : deletedLines) {
                //Checks if the block its in one of the deleted lines
                if (y > block.getY())
                    ++count;
            }
            //Moves block down per deleted line
            block.setY(block.getY() + count);
        }
    }

    //Checks if a line is complete
    private boolean lineComplete(int y) {
        int count = 0;
        for (Block block : blocks) {
            if (block.getY() == y)
                ++count;
        }
        if (count == BOARD_WIDTH) {
            return true;
        }
        return false;
    }


    public List<Block> getBlocks() {
        return blocks;
    }

    public Shape getFallingShape() {
        return fallingShape;
    }

    public Shape getNextShape() {
        return nextShape;
    }

    public static int getColorForAllForNow() {
        return COLOR_FOR_ALL_FOR_NOW;
    }
}
