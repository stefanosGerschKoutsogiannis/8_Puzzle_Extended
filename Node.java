import java.util.ArrayList;
import java.util.List;

public class Node {

    private final Block[][] grid;
    private double cost;
    private final Node parent;
    private double heuristicCost;


    public Node(Block[][] grid, double cost, Node parent) {
        this.grid = grid;
        this.cost = cost;
        this.parent = parent;
        this.heuristicCost = 0;
    }

    // swap two values inside a grid
    public void swapBlockValues(Block blockOneCopy, Block blockTwoCopy) {
        int temp = blockOneCopy.getValue();
        blockOneCopy.setValue(blockTwoCopy.getValue());
        blockTwoCopy.setValue(temp);
    }

    // method that returns a deep copy of the grid
    public Block[][] deepCopy(Block[][] initial) {
        int i, j;
        Block[][] copy = new Block[3][3];

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                copy[i][j] = new Block(initial[i][j]);
            }
        }

        return copy;
    }


    // takes a node and checks all of its blocks
    // it finds and produces all the possible moves through other util functions
    // ,so it produces all of its children
    // should pass a deep copy of the state
    public List<Node> checkIfAbleToMove(Node node) {
        int i,j;
        List<Node> children = new ArrayList<>();
        Node retChild;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {

                // will be called for the blocks in the corners of the grid
                if ((i == 0 && j == 0) || (i == 2 && j == 2) || (i == 0 && j == 2) || (i == 2 && j == 0)) {
                    if (node.grid[i][j].getValue() != 0) {
                        int x = node.grid[i][j].getPosX();
                        int y = node.grid[i][j].getPosY();
                        retChild = checkNeighborsCorner(node, x, y);
                        if (retChild != null) {
                            children.add(retChild);
                        }
                    }
                }

                // will be called for the block in the middle of the grid
                else if (i == 1 && j == 1) {
                    if (node.grid[i][j].getValue() != 0) {
                        int x = node.grid[i][j].getPosX();
                        int y = node.grid[i][j].getPosY();
                        retChild = checkNeighborsMiddle(node, x, y);
                        // when in middle, always an available position to go
                        // no need for null checking, checkNeighborsMiddle never returns null
                        children.add(retChild);
                    }
                }

                // called for the other blocks
                else {
                    if (node.grid[i][j].getValue() != 0) {
                        int x = node.grid[i][j].getPosX();
                        int y = node.grid[i][j].getPosY();
                        retChild = checkNeighborsOther(node, x, y);
                        if (retChild != null) {
                            children.add(retChild);
                        }
                    }
                }
            }
        }

        return children;
    }


    public Node checkNeighborsCorner(Node node, int posX, int posY) {

        // what will be returned
        Node childProduced;

        if (posX == 0 && posY == 0) {
            // check up, upright, left
            childProduced = checkUp(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }
            childProduced = checkUpRight(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkRight(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            // if block in (0,0) cant go anywhere, return null
            return null;
        }

        else if (posX == 0 && posY == 2) {
            // check right, downright, down
            childProduced = checkRight(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkDownRight(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkDown(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            return null;
        }

        else if (posX == 2 && posY == 0) {
            // check up, up-left, left
            childProduced = checkUp(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkUpLeft(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkLeft(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            return null;
        }

        else {
            // check left, down-left, down
            childProduced = checkLeft(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkDownLeft(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkDown(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            return null;
        }

    }

    public Node checkNeighborsMiddle(Node node, int posX, int posY) {

        // what will be returned
        Node childProduced;

        childProduced = checkUp(node, posX, posY);
        if (childProduced != null) {
            return childProduced;
        }

        childProduced = checkDown(node, posX, posY);
        if (childProduced != null) {
            return childProduced;
        }

        childProduced = checkLeft(node, posX, posY);
        if (childProduced != null) {
            return childProduced;
        }

        childProduced = checkRight(node, posX, posY);
        if (childProduced != null) {
            return childProduced;
        }

        childProduced = checkUpRight(node, posX, posY);
        if (childProduced != null) {
            return childProduced;
        }
        childProduced = checkUpLeft(node, posX, posY);
        if (childProduced != null) {
            return childProduced;
        }

        childProduced = checkDownRight(node, posX, posY);
        if (childProduced != null) {
            return childProduced;
        }

        childProduced = checkDownLeft(node, posX, posY);


        // if in middle, always an available position
        return childProduced;
    }

    public Node checkNeighborsOther(Node node, int posX, int posY) {

        // what will be returned
        Node childProduced;

        // check if in left middle
        if (posX == 0 && posY == 1) {

            childProduced = checkUp(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkDown(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkRight(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkUpRight(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkDownRight(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            // if no available moves, return null
            return null;
        }

        // check if in down middle
        else if (posX == 1 && posY == 0) {

            childProduced = checkLeft(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkRight(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkUp(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkUpLeft(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkUpRight(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            return null;
        }

        // check if in up middle
        else if (posX == 1 && posY == 2) {

            childProduced = checkLeft(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkRight(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkDown(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkDownLeft(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkDownRight(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            return null;
        }

        // check if in right middle
        else if (posX == 2 && posY == 1) {
            childProduced = checkUp(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkDown(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkLeft(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkUpLeft(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            childProduced = checkDownLeft(node, posX, posY);
            if (childProduced != null) {
                return childProduced;
            }

            return null;
        }

        // idk, compiler issues
        return null;
    }


    public Node checkUp(Node node, int posX, int posY) {
        // able to move, do the move and create child
        if (node.grid[posX][posY+1].getValue() == 0) {
            Block[][] gridCopy = node.deepCopy(grid);

            swapBlockValues(gridCopy[posX][posY], gridCopy[posX][posY+1]);

            return new Node(gridCopy, node.cost+1, node);
        }
        return null;
    }

    public Node checkDown(Node node, int posX, int posY) {
        // able to move, do the move and create child
        if (node.grid[posX][posY-1].getValue() == 0) {
            Block[][] gridCopy = node.deepCopy(grid);
            swapBlockValues(gridCopy[posX][posY], gridCopy[posX][posY-1]);

            return new Node(gridCopy, node.cost+1, node);
        }
        return null;
    }

    public Node checkRight(Node node, int posX, int posY) {
        // able to move, do the move and create child
        if (node.grid[posX+1][posY].getValue() == 0) {
            Block[][] gridCopy = node.deepCopy(grid);
            swapBlockValues(gridCopy[posX][posY], gridCopy[posX+1][posY]);

            return new Node(gridCopy, node.cost+1, node);
        }
        return null;
    }

    public Node checkLeft(Node node, int posX, int posY) {
        // able to move, do the move and create child
        if (node.grid[posX-1][posY].getValue() == 0) {
            Block[][] gridCopy = node.deepCopy(grid);
            swapBlockValues(gridCopy[posX][posY], gridCopy[posX-1][posY]);

            return new Node(gridCopy, node.cost+1, node);
        }
        return null;
    }

    public Node checkUpRight(Node node, int posX, int posY) {
        // able to move, do the move and create child
        if (node.grid[posX+1][posY+1].getValue() == 0) {
            Block[][] gridCopy = node.deepCopy(grid);

            swapBlockValues(gridCopy[posX][posY], gridCopy[posX+1][posY+1]);

            return new Node(gridCopy, node.cost+1, node);
        }
        return null;
    }

    public Node checkUpLeft(Node node, int posX, int posY) {
        // able to move, do the move and create child
        if (node.grid[posX-1][posY+1].getValue() == 0) {
            Block[][] gridCopy = node.deepCopy(grid);
            swapBlockValues(gridCopy[posX][posY], gridCopy[posX-1][posY+1]);

            return new Node(gridCopy, node.cost+1, node);
        }
        return null;
    }

    public Node checkDownLeft(Node node, int posX, int posY) {
        // able to move, do the move and create child
        if (node.grid[posX-1][posY-1].getValue() == 0) {
            Block[][] gridCopy = node.deepCopy(grid);

            swapBlockValues(gridCopy[posX][posY], gridCopy[posX-1][posY-1]);

            return new Node(gridCopy, node.cost+1, node);
        }
        return null;
    }

    public Node checkDownRight(Node node, int posX, int posY) {
        // able to move, do the move and create child
        if (node.grid[posX+1][posY-1].getValue() == 0) {
            Block[][] gridCopy = node.deepCopy(grid);

            swapBlockValues(gridCopy[posX][posY], gridCopy[posX+1][posY-1]);

            return new Node(gridCopy, node.cost+1, node);
        }
        return null;
    }


    public Block[][] getGrid() {
        return grid;
    }

    public Node getParent() {
        return parent;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    // print grid in cmd
    public void printGrid() {
        int i, j;


        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {

                System.out.print("| "+ grid[i][j].getValue() + " ");
                if (j == 2) {
                    System.out.print("|");
                    System.out.println();
                }
            }
        }
    }

    // calls printGrid() and the result is the printing of the grid to the cmd
    public void printNodeString() {
        System.out.println("Grid: ");
        printGrid();
        //System.out.println("Cost: " + cost);
    }

    public double getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(double heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

}
