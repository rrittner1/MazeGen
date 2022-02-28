import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TimerListener implements ActionListener {
    int x;
    int y;
    int timesUnchanged;
    int gridH = 0;
    int gridW = 0;
    ArrayList<int[]> points = new ArrayList<>();
    ArrayList<boolean[][][]> mazeStates = new ArrayList<>();
    int i = 0;
    Maze maze;
    Timer timer;
    String algorithm;

    public TimerListener(Maze m, Timer t, String alg) {
        super();
        algorithm = alg;
        timer = t;
        maze = m;
        timesUnchanged = 0;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (algorithm.equals("My algorithm")) {
            if (points.size() == 0) {
                gridW = maze.getGRIDWIDTH();
                gridH = maze.getGRIDHEIGHT();
                for (int i = 1; i < gridW; i++) {
                    for (int j = 1; j < gridH; j++) {
                        points.add(new int[]{i, j});
                    }
                }
            }
            if (timesUnchanged == gridW * gridH * 2) {
                timer.stop();
            }
            int[] point = points.remove((int) (Math.random() * points.size()));
            x = point[0];
            y = point[1];
            boolean changed = maze.makeMazeGen(x, y);
            if (changed) {
                timesUnchanged = 0;
            } else {
                timesUnchanged++;
            }
            maze.frame.repaint();
        } else if (algorithm.equals("Recursive division")) {
            if (i == 0) {
                maze.recursiveDiv(true);
            } else if (i == mazeStates.size() + 1) {
                timer.stop();
            } else {
                maze.grid = mazeStates.get(i - 1);
                maze.frame.repaint();
            }
            i++;
        } else if (algorithm.equals("Dads algorithm")) {
            if (i == 0) {
                maze.dadsAlgorithm(true);
            } else if (i == mazeStates.size() + 1) {
                maze.refillEdges();
                maze.frame.repaint();
                timer.stop();
            } else {
                maze.grid = mazeStates.get(i - 1);
                maze.frame.repaint();
            }
            i++;
        }
    }
}
