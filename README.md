# 8-Puzzle Solver with UCS and A*

This repository contains an implementation of an 8-Puzzle solver using Uniform Cost Search (UCS) and A* search algorithms. This project was completed as an assignment for the Artificial Intelligence course at the Department of Computer Science and Engineering, University of Ioannina, Spring 2024.

**Project Description:**

The 8-Puzzle is a classic problem in artificial intelligence, involving sliding numbered tiles on a 3x3 grid to reach a goal state. This project extends the standard 8-Puzzle by implementing two search algorithms:

* **Uniform Cost Search (UCS):** A graph search algorithm that explores nodes in order of increasing cost.
* **A\* Search:** An informed search algorithm that uses a heuristic function to estimate the cost to reach the goal.

**Algorithms Implemented:**

* **Uniform Cost Search (UCS)**
* **A\* Search**

**Running the Application:**

1.  Clone the repository.
```sh
git clone
```
2. Compile in the following order
```sh 
javac Block.java Node.java Program.java
```

3. Run the application
```sh
java Program.java
```

**Implementation Notes and Known Limitations:**

* **Heap Capacity:** The current implementation uses a fixed-size heap, which may limit the search depth for complex puzzle configurations. Consequently, the solver may not find solutions for very challenging initial states due to heap overflow.
* **Heuristic Choice:** Previous attempts with other heuristic functions proved less effective. The Chebyshev distance was adopted to improve the performance and accuracy of the A\* search.

**Course Information:**

* Course: Artificial Intelligence
* Department: Department of Computer Science and Engineering
* University: University of Ioannina
* Semester: Spring 2024
