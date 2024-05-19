import java.util.*;


public class Program {

    private Queue<Node> searchFrontier;
    private final Node initialState;
    private final Node goalState;

    public Program(Node initialState, Node goalState) {
        this.searchFrontier = new LinkedList<>();
        this.initialState = initialState;
        this.goalState = goalState;
    }

    // problem related

    // find the value of the passed block in the goal grid
    public Block findValueInGoalState(Block block) {
        int i, j;
        int value = block.getValue();
        Block[][] goalGrid = goalState.getGrid();

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                if (goalGrid[i][j].getValue() == value) {
                    return goalGrid[i][j];
                }
            }
        }
        return null;
    }


    // function that calculates euclidean distance
    public void euclideanDistance(Node node) {
        int i,j;

        // get the nodes grid
        Block[][] grid = node.getGrid();

        // this is the sum of squaared differences that will be square rooted in the end
        double approx = 0;

        // for each cell in the grid
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {

                // get position of value in goal grid
                Block posOfValueInGoalState = findValueInGoalState(grid[i][j]);
                int xGoal = posOfValueInGoalState.getPosX();
                int yGoal = posOfValueInGoalState.getPosY();

                // get positions in current grid
                int x = grid[i][j].getPosX();
                int y = grid[i][j].getPosY();

                // calculate squared diff
                double approximation = Math.pow(x-xGoal, 2) + Math.pow(y-yGoal, 2);

                // add to approx
                approx += approximation;
            }
        }
        // square root to calc euclidean distance
        double distApprox = Math.sqrt(approx);

        // add dist aprox to node cost(for A*)
        distApprox += node.getCost();

        // set heuristic cost
        node.setHeuristicCost(distApprox);

    }


    // this method is responsible for initializing the search frontier
    public void initializeSearchFrontier(Node initialState) {
        searchFrontier.add(initialState);
    }


    // this method is responsible for removing a Node from the search frontier
    public Node removeFromSearchFrontier() {

        // get node in front of the queue
        return searchFrontier.poll();
    }

    // replace the old list and points to a new one, for rerunning the algorithms
    public void setDefaultSearchFrontier() {
        searchFrontier = new LinkedList<Node>();
    }


    // this method is responsible for checking if a Node is a goalState
    public boolean checkIfGoalState(Node node) {
        int i,j;

        // get goal grid
        Block[][] goalGrid = goalState.getGrid();

        // get current grid
        Block[][] nodeGrid = node.getGrid();

        // iterate and check
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                if (nodeGrid[i][j].getValue() != goalGrid[i][j].getValue()) {
                    return false;
                }
            }
        }

        return true;
    }

    // this method is responsible for creating the children of a node and also adding them to the search frontier
    public void createChildren(Node node, String algorithm) {

        // all children of the given node will be stored in children list
        List<Node> children = node.checkIfAbleToMove(node);

        // calculate the heuristic function of distance(only for a-star)
        if (algorithm.equals("A*")) {
            for (Node child: children) {
                euclideanDistance(child);
            }
        }

        // add all children created to search frontier
        searchFrontier.addAll(children);
    }

    // this method is responsible for printing information
    public void printPath(Node node, String algorithm) {

        List<Node> path = searchBack(node).reversed();
        double cost = node.getCost();
        double heuristicCostPath = 0;
        for (Node k: path) {
            k.printNodeString();
            // added last minute
            if (algorithm.equals(("A*"))) {
                System.out.println("Heuristic cost: "+ k.getHeuristicCost());
                heuristicCostPath += k.getHeuristicCost();
            }
        }

        // for the number of actions
        int length = path.toArray().length-1;
        double totalCost = cost;

        if (algorithm.equals(("A*"))) {
            totalCost += heuristicCostPath;
            System.out.println("Total number of actions was: " + length);
            System.out.println("The total cost(initial to goal) was: "+ totalCost);
        }

        else {
            System.out.println("Total number of actions was: " + length);
            System.out.println("The total cost was: "+ totalCost);
        }
    }

    // follows the path until it reaches the initial node and returns a list containing the path the solution followed
    public List<Node> searchBack(Node node) {

        List<Node> path = new ArrayList<>();

        path.add(node);

        Node parent = node.getParent();
        if (parent == null) {
            return path;
        }
        path.add(parent);
        while (true) {
            parent = parent.getParent();
            if (parent == null) {
                return path;
            }
            path.add(parent);
        }
    }



    // UCS related

    // adds the initial state to the search frontier
    public void initUCS(Node initialState) {
        initializeSearchFrontier(initialState);
    }


    // performs the UCS algorithm
    public void performUCS() {

        // check if the search frontier is empty
        while (!searchFrontier.isEmpty()) {

            // take the first state from the search frontier and check if it is goal state
            Node state = removeFromSearchFrontier();

            if (checkIfGoalState(state)) {
                searchBack(state);
                printPath(state, "UCS");
                return;
            }

            // if not goal state, produce its children and perform again ucs
            createChildren(state, "UCS");
        }

        System.out.println("No solution");
    }



    // A-star related

    // finds the Node to expand in A* by returning the Node with the min h(n) + g(n)
    public Node findNodeToExpand() {
        // search in search frontier, find node with min cost, when found, remove
        double minValue = Double.MAX_VALUE;
        Node retNode = null;
        for (Node node: searchFrontier) {
            if (node.getCost()+ node.getHeuristicCost() < minValue) {
                minValue = node.getCost() + node.getHeuristicCost();
                retNode = node;
            }
        }

        return retNode;
    }


    // adds the initial state to the search frontier
    public void initAStar(Node initialState) {
        initializeSearchFrontier(initialState);
    }

    // performs the A-Star algorithm, should be initialized first
    public void performAStar() {

        // check if the search frontier is empty and if true terminates
        while (!searchFrontier.isEmpty()) {

            // finds the node with the minimum cost
            Node state = findNodeToExpand();

            // removes it from the search frontier
            searchFrontier.remove(state);

            // checks if the state that has been removed is a goal state
            if (checkIfGoalState(state)) {
                searchBack(state);
                printPath(state, "A*");
                return;
            }

            // if not goal state, produce its children and perform again A*
            createChildren(state, "A*");

        }

        System.out.println("No solution");


    }


    public static void main(String[] args) {


        Node initialState;
        Node goalState;

        // create blocks for initial state, hard-coded :(
        Block b1 = new Block(0, 0, 6);
        Block b2 = new Block(0, 1, 5);
        Block b3 = new Block(0, 2, 4);
        Block b4 = new Block(1, 0, 0);
        Block b5 = new Block(1, 1, 7);
        Block b6 = new Block(1, 2, 3);
        Block b7 = new Block(2, 0, 8);
        Block b8 = new Block(2, 1, 2);
        Block b9 = new Block(2, 2, 1);

        // create blocks for goal state, hard-coded :(
        Block g1 = new Block(0, 0, 6);
        Block g2 = new Block(0, 1, 0);
        Block g3 = new Block(0, 2, 4);
        Block g4 = new Block(1, 0, 5);
        Block g5 = new Block(1, 1, 7);
        Block g6 = new Block(1, 2, 3);
        Block g7 = new Block(2, 0, 8);
        Block g8 = new Block(2, 1, 2);
        Block g9 = new Block(2, 2, 1);

        Block[][] initialBlocks = new Block[3][3];
        initialBlocks[0][0] = b1;
        initialBlocks[0][1] = b2;
        initialBlocks[0][2] = b3;
        initialBlocks[1][0] = b4;
        initialBlocks[1][1] = b5;
        initialBlocks[1][2] = b6;
        initialBlocks[2][0] = b7;
        initialBlocks[2][1] = b8;
        initialBlocks[2][2] = b9;

        Block[][] goalBlocks = new Block[3][3];
        goalBlocks[0][0] = g1;
        goalBlocks[0][1] = g2;
        goalBlocks[0][2] = g3;
        goalBlocks[1][0] = g4;
        goalBlocks[1][1] = g5;
        goalBlocks[1][2] = g6;
        goalBlocks[2][0] = g7;
        goalBlocks[2][1] = g8;
        goalBlocks[2][2] = g9;


        initialState = new Node(initialBlocks, 0, null);
        goalState = new Node(goalBlocks, 0, null);

        // creates an arraylist for the search frontier, and one for the pathToGoalState
        Program program = new Program(initialState, goalState);



        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the algorithm you want to run\n1. UCS\n2. A*");
        String option = scanner.next();
        String opt1 = "1";
        String opt2 = "2";
        if (option.equals(opt1)) {
            program.initUCS(initialState);
            program.performUCS();
            program.setDefaultSearchFrontier();
        } else if (option.equals(opt2)) {
            program.initAStar(initialState);
            program.performAStar();
            program.setDefaultSearchFrontier();
        } else {
            System.out.println("Not a valid option");
        }


    }


}

