## Sliding-Puzzle Solver

![image](https://user-images.githubusercontent.com/73400995/120373079-991b3900-c320-11eb-95bf-86ecd04e3f8c.png)

# Launch:
this is an Dynamic AI solver for sliding-puzzle.
in order to use it all you need to do is:
```
$ git clone https://github.com/avieha/Sliding-Puzzle.git
```
and then, inside 'src' folder just run the commands(make sure first that you have installed java and javac on your computer)
```
javac *.java
```
and:
```
java Ex1 input.txt
```

in input.txt you can customize your own board, with start state and goal state (with one or two empty blocks), and of course at the **size**
that you decided.

you also have to choose your favorite Algorithm at the first line- A*, DFID, DFBnB, BFS or IDA*.
A*,IDA*,DFBnB are using Heuristic function.
