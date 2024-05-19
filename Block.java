public class Block {

    private int posX;
    private int posY;
    private int value;


    public Block(int posX, int posY, int value) {
        this.posX = posX;
        this.posY = posY;
        this.value = value;
    }

    // copy constructor for keeping a copy of the grid
    public Block(Block initial) {
        this.posX = initial.posX;
        this.posY = initial.posY;
        this.value = initial.value;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getValue() {
        return value;
    }


    public void setValue(int value) {
        this.value = value;
    }


}