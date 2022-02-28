import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Maze extends JPanel implements Runnable {
    private int GRIDHEIGHT = 30;
    private int GRIDWIDTH = 30;
    private static final int FRAMEHEIGHT = 600;
    private static final int FRAMEWIDTH = 600;
    boolean[][][] grid;
    int[][] maze;
    boolean drawSolve;
    String algorithm;
    Timer timer;
    JFrame frame;
    JLabel rowsLabel;
    JTextField rows;
    JLabel columnsLabel;
    JTextField columns;
    JButton enter;
    JButton animate;
    JButton solve;
    JComboBox<String> algorithmBox;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Maze());
    }

    @Override
    public void run() {
        frame = new JFrame();
        Container content = frame.getContentPane();
        rowsLabel = new JLabel("Rows:");
        rowsLabel.setBounds(0, 601, 50, 25);
        content.add(rowsLabel);
        rows = new JTextField("30");
        rows.setBounds(50, 601, 50, 25);
        rows.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                columns.requestFocus();
            }
        });
        content.add(rows);
        columnsLabel = new JLabel("Columns:");
        columnsLabel.setBounds(100, 601, 70, 25);
        content.add(columnsLabel);
        columns = new JTextField("30");
        columns.setBounds(170, 601, 50, 25);
        columns.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    timer.stop();
                    GRIDWIDTH = Integer.parseInt(columns.getText());
                    GRIDHEIGHT = Integer.parseInt(rows.getText());
                    grid = new boolean[2][GRIDHEIGHT + 1][GRIDWIDTH + 1];
                    algorithm = (String) algorithmBox.getSelectedItem();
                    if (algorithm.equals("My algorithm")) {
                        makeMaze();
                    } else if (algorithm.equals("Recursive division")) {
                        recursiveDiv(false);
                    } else if (algorithm.equals("Dads algorithm")) {
                        dadsAlgorithm(false);
                    }
                    frame.repaint();
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "Enter Integers",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        content.add(columns);
        enter = new JButton("Reload");
        enter.setBounds(225, 601, 80, 25);
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    timer.stop();
                    GRIDWIDTH = Integer.parseInt(columns.getText());
                    GRIDHEIGHT = Integer.parseInt(rows.getText());
                    grid = new boolean[2][GRIDHEIGHT + 1][GRIDWIDTH + 1];
                    algorithm = (String) algorithmBox.getSelectedItem();
                    if (algorithm.equals("My algorithm")) {
                        makeMaze();
                    } else if (algorithm.equals("Recursive division")) {
                        recursiveDiv(false);
                    } else if (algorithm.equals("Dads algorithm")) {
                        dadsAlgorithm(false);
                    }
                    frame.repaint();
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "Enter Integers",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        content.add(enter);
        animate = new JButton("Animate");
        animate.setBounds(310, 601, 90, 25);
        animate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.stop();
                        Maze m = ((TimerListener) timer.getActionListeners()[0]).maze; //this is really bad code
                        timer.removeActionListener(timer.getActionListeners()[0]);
                        timer.addActionListener(new TimerListener(m, timer, (String) algorithmBox.getSelectedItem()));
                        GRIDWIDTH = Integer.parseInt(columns.getText());
                        GRIDHEIGHT = Integer.parseInt(rows.getText());
                        grid = new boolean[2][GRIDHEIGHT + 1][GRIDWIDTH + 1];
                        for (int i = 0; i < GRIDHEIGHT; i++) {
                            grid[0][i][0] = true;
                            grid[0][i][GRIDWIDTH] = true;
                        }
                        for (int i = 0; i < GRIDWIDTH; i++) {
                            grid[1][0][i] = true;
                            grid[1][GRIDHEIGHT][i] = true;
                        }
                        timer.setInitialDelay(100);
                        timer.start();
                    }
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "Enter Integers",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        content.add(animate);
        solve = new JButton("Solve");
        solve.setBounds(405, 601, 80, 25);
        solve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                solve();
                drawSolve = true;
                frame.repaint();
            }
        });
        content.add(solve);
        String[] algs = {"My algorithm", "Recursive division", "Dads algorithm"};
        algorithmBox = new JComboBox<>(algs);
        algorithmBox.setBounds(490, 601, 110, 25);
        content.add(algorithmBox);
        content.add(this);

        algorithm = "My algorithm";
        grid = new boolean[2][GRIDHEIGHT + 1][GRIDWIDTH + 1];
        makeMaze();

        frame.setSize(FRAMEWIDTH + 16, FRAMEHEIGHT + 64);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        timer = new Timer(10, new TimerListener(this, timer, "My algorithm"));
        drawSolve = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setColor(Color.black);
        for (int i = 0; i < 2; i++) {
            for (int y = 0; y < GRIDHEIGHT + 1; y++) {
                for (int x = 0; x < GRIDWIDTH + 1; x++) {
                    if (grid[i][y][x]) {
                        if (i == 0) {
                            graphics2D.drawLine(x * (FRAMEWIDTH / GRIDWIDTH), y * (FRAMEHEIGHT / GRIDHEIGHT),
                                    x * (FRAMEWIDTH / GRIDWIDTH), (y + 1) * (FRAMEHEIGHT / GRIDHEIGHT));
                        } else {
                            graphics2D.drawLine(x * (FRAMEWIDTH / GRIDWIDTH), y * (FRAMEHEIGHT / GRIDHEIGHT),
                                    (x + 1) * (FRAMEWIDTH / GRIDWIDTH), y * (FRAMEHEIGHT / GRIDHEIGHT));
                        }
                    }
                }
            }
        }
        if (drawSolve) {
            drawSolve = false;
            graphics2D.setColor(Color.RED);
            for (int y = 1; y < maze.length - 1; y++) {
                for (int x = 1 + y % 2; x < maze[y].length - 1; x += 2) {
                    if (y % 2 == 1) {
                        if (maze[y][x] == 2) {
                            double i = x / 2 - 0.5;
                            double j = (y - 1) / 2 + 0.5;
                            graphics2D.drawLine((int) (i * (FRAMEWIDTH / GRIDWIDTH)),
                                    (int) (j * (FRAMEHEIGHT / GRIDHEIGHT)), (int) ((i + 1) * (FRAMEWIDTH / GRIDWIDTH)),
                                    (int) ((j) * (FRAMEHEIGHT / GRIDHEIGHT)));
                        }
                    }
                    if (y % 2 == 0) {
                        if (maze[y][x] == 2) {
                            double i = (x - 1) / 2 + 0.5;
                            double j = (y) / 2 - 0.5;
                            graphics2D.drawLine((int) (i * (FRAMEWIDTH / GRIDWIDTH)),
                                    (int) (j * (FRAMEHEIGHT / GRIDHEIGHT)), (int) ((i) * (FRAMEWIDTH / GRIDWIDTH)),
                                    (int) ((j + 1) * (FRAMEHEIGHT / GRIDHEIGHT)));
                        }
                    }
                }
            }
        }
    }

    private void makeMaze() {
        for (int i = 0; i < GRIDHEIGHT; i++) {
            grid[0][i][0] = true;
            grid[0][i][GRIDWIDTH] = true;
        }
        for (int i = 0; i < GRIDWIDTH; i++) {
            grid[1][0][i] = true;
            grid[1][GRIDHEIGHT][i] = true;
        }
        ArrayList<int[]> points = new ArrayList<>();
        int timesUnchanged = 0;
        boolean changed = false;
        while (timesUnchanged < 10) {
            for (int i = 1; i < GRIDWIDTH; i++) {
                for (int j = 1; j < GRIDHEIGHT; j++) {
                    points.add(new int[]{i, j});
                }
            }
            changed = false;
            while (points.size() > 0) {
                int[] point = points.remove((int) (Math.random() * points.size()));
                int x = point[0];
                int y = point[1];
                if (!grid[0][y - 1][x]) {
                    if (Math.random() > 0.5) {
                        grid[0][y - 1][x] = true;
                        if (!isLooped(x, y)) {
                            changed = true;
                        } else {
                            grid[0][y - 1][x] = false;
                        }
                    }
                }
                if (!grid[1][y][x]) {
                    if (Math.random() > 0.5) {
                        grid[1][y][x] = true;
                        if (!isLooped(x, y)) {
                            changed = true;
                        } else {
                            grid[1][y][x] = false;
                        }
                    }
                }
                if (!grid[0][y][x]) {
                    if (Math.random() > 0.5) {
                        grid[0][y][x] = true;
                        if (!isLooped(x, y)) {
                            changed = true;
                        } else {
                            grid[0][y][x] = false;
                        }
                    }
                }
                if (!grid[1][y][x - 1]) {
                    if (Math.random() > 0.5) {
                        grid[1][y][x - 1] = true;
                        if (!isLooped(x, y)) {
                            changed = true;
                        } else {
                            grid[1][y][x - 1] = false;
                        }
                    }
                }
            }
            if (!changed) {
                timesUnchanged++;
            } else {
                timesUnchanged = 0;
            }
        }
    }

    private boolean isLooped(int x, int y) {
        int loop = isLoopedRunner(x, y, x, y, -1);
        return loop == 1;
    }

    private int isLoopedRunner(int x, int y, int startX, int startY, int d) {
        try {
            int loop = 0;
            int bounds = 0;
            if (inBounds(x, y - 1, 0) && grid[0][y - 1][x] && d != 0) {
                if (x == startX && y - 1 == startY) {
                    return 1;
                }
                if (!(y - 1 == 0)) {
                    loop = isLoopedRunner(x, y - 1, startX, startY, 2);
                } else {
                    bounds++;
                }
                if (loop == 1) {
                    return 1;
                } else if (loop == 2) {
                    bounds++;
                    loop = 0;
                }
            }
            if (inBounds(x, y, 1) && grid[1][y][x] && d != 3) {
                if (x + 1 == startX && y == startY) {
                    return 1;
                }
                if (!(x + 1 == GRIDWIDTH)) {
                    loop = isLoopedRunner(x + 1, y, startX, startY, 1);
                } else {
                    bounds++;
                }
                if (loop == 1) {
                    return 1;
                } else if (loop == 2) {
                    bounds++;
                    loop = 0;
                }
            }
            if (inBounds(x, y, 0) && grid[0][y][x] && d != 2) {
                if (x == startX && y + 1 == startY) {
                    return 1;
                }
                if (!(y + 1 == GRIDHEIGHT)) {
                    loop = isLoopedRunner(x, y + 1, startX, startY, 0);
                } else {
                    bounds++;
                }
                if (loop == 1) {
                    return 1;
                } else if (loop == 2) {
                    bounds++;
                    loop = 0;
                }
            }
            if (inBounds(x - 1, y, 1) && grid[1][y][x - 1] && d != 1) {
                if (x - 1 == startX && y == startY) {
                    return 1;
                }
                if (!(x - 1 == 0)) {
                    loop = isLoopedRunner(x - 1, y, startX, startY, 3);
                } else {
                    bounds++;
                }
                if (loop == 1) {
                    return 1;
                } else if (loop == 2) {
                    bounds++;
                    loop = 0;
                }
            }
            if (bounds > 1) {
                return 1;
            }
            if (bounds == 1) {
                return 2;
            }
            return loop;
        } catch (StackOverflowError e) {
            System.out.println("Overflow");
            return 1;
        }
    }

    private boolean inBounds(int x, int y, int o) {
        if (o == 0) {
            return x > -1 && x <= GRIDWIDTH && y > -1 && y < GRIDHEIGHT;
        } else {
            return x > -1 && x < GRIDWIDTH && y > -1 && y <= GRIDHEIGHT;
        }
    }

    public void recursiveDiv(boolean fill) {
        for (int i = 0; i < GRIDHEIGHT; i++) {
            grid[0][i][0] = true;
            grid[0][i][GRIDWIDTH] = true;
        }
        for (int i = 0; i < GRIDWIDTH; i++) {
            grid[1][0][i] = true;
            grid[1][GRIDHEIGHT][i] = true;
        }
        boolean[][][] newGrid = new boolean[2][grid[0].length][grid[0][0].length];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (int k = 0; k < grid[0][0].length; k++) {
                    newGrid[i][j][k] = grid[i][j][k];
                }
            }
        }
        recursiveDivRunner(0, 0, GRIDWIDTH, GRIDHEIGHT, fill);
    }

    private void recursiveDivRunner(int x1, int y1, int x2, int y2, boolean fill) {
        int newX;
        int newY;
        if (x2 - x1 == 1 || y2 - y1 == 1) {
            return;
        }
        newX = x1 + 1 + (int) (weightedRandom() * (x2 - x1 - 2));
        for (int y = y1; y < y2; y++) {
            grid[0][y][newX] = true;
        }
        newY = y1 + 1 + (int) (weightedRandom() * (y2 - y1 - 2));
        for (int x = x1; x < x2; x++) {
            grid[1][newY][x] = true;
        }
        int noCut = (int) (Math.random() * 4);
        if (noCut != 0) {
            grid[0][y1 + (int) (Math.random() * (newY - y1))][newX] = false;
        }
        if (noCut != 1) {
            grid[1][newY][newX + (int) (Math.random() * (x2 - newX))] = false;
        }
        if (noCut != 2) {
            grid[0][newY + (int) (Math.random() * (y2 - newY))][newX] = false;
        }
        if (noCut != 3) {
            grid[1][newY][x1 + (int) (Math.random() * (newX - x1))] = false;
        }
        if (fill) {
            boolean[][][] gridState = new boolean[2][grid[0].length][grid[0][0].length];
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    for (int k = 0; k < grid[0][0].length; k++) {
                        gridState[i][j][k] = grid[i][j][k];
                    }
                }
            }
            ((TimerListener) (timer.getActionListeners()[0])).mazeStates.add(gridState);
        }

        recursiveDivRunner(x1, y1, newX, newY, fill);
        recursiveDivRunner(x1, newY, newX, y2, fill);
        recursiveDivRunner(newX, y1, x2, newY, fill);
        recursiveDivRunner(newX, newY, x2, y2, fill);
    }

    private double weightedRandom() {
        double out = 0;
        int depth = 10;
        for (int i = 0; i < depth; i++) {
            out += Math.random();
        }
        return out / depth;
    }

    public void dadsAlgorithm(boolean fill) {
        ArrayList<int[]> visitedSquares = new ArrayList<>();
        visitedSquares.add(new int[]{0, 0});
        for (int i = 0; i < GRIDHEIGHT + 1; i++) {
            Arrays.fill(grid[0][i], true);
            Arrays.fill(grid[1][i], true);
        }
        int numVisited = 0;
        while (visitedSquares.size() < GRIDWIDTH * GRIDHEIGHT) {
            int[] point = visitedSquares.get((int) (Math.random() * (visitedSquares.size())));
            numVisited += randomWalk(point[0], point[1], GRIDWIDTH - 1, GRIDHEIGHT - 1, visitedSquares, fill);
        }
        refillEdges();
    }

    public void refillEdges() {
        for (int i = 0; i < GRIDHEIGHT; i++) {
            grid[0][i][0] = true;
            grid[0][i][GRIDWIDTH] = true;
        }
        for (int i = 0; i < GRIDWIDTH; i++) {
            grid[1][0][i] = true;
            grid[1][GRIDHEIGHT][i] = true;
        }
        for (int i = 0; i < GRIDHEIGHT + 1; i++) {
            grid[1][i][GRIDWIDTH] = false;
        }
        for (int i = 0; i < GRIDWIDTH + 1; i++) {
            grid[0][GRIDHEIGHT][i] = false;
        }
    }

    private int randomWalk(int x, int y, int endX, int endY, ArrayList<int[]> visitedSquares, boolean fill) {
        /*
        System.out.println(x + " " + y + " " + endX + " " + endY + " " + visitedSquares.size());
        for (int i = 0; i < GRIDWIDTH; i++) {
            System.out.print(" _");
        }
        System.out.println();
        for (int j = 0; j < GRIDHEIGHT; j++) {
            for (int i = 0; i < 2 * GRIDWIDTH + 1; i++) {
                if (i % 2 == 0) {
                    if (grid[0][j][i / 2]) {
                        System.out.print("|");
                    } else {
                        System.out.print(" ");
                    }
                }
                if (i % 2 == 1) {
                    if (j < grid[1].length && grid[1][j + 1][(i - 1) / 2]) {
                        System.out.print("_");
                    } else {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
        */
        if (x == endX && y == endY) {
            return 1;
        }
        if (x == -1 || x == GRIDWIDTH || y == -1 || y == GRIDHEIGHT) {
            visitedSquares.remove(visitedSquares.size() - 1);
            return 0;
        }
        boolean done = false;
        int d;
        int nextX;
        int nextY;
        ArrayList<Integer> directions = new ArrayList<>();
        directions.add(0);
        directions.add(1);
        directions.add(2);
        directions.add(3);
        while (directions.size() > 0 && !done) {
            d = directions.remove((int) (Math.random() * directions.size()));
            nextX = x + (d % 2) * (2 - d);
            nextY = y + ((d + 1) % 2) * (d - 1);
            int wallX = x + (d % 2) * (1 - (d / 2));
            int wallY = y + ((d + 1) % 2) * (d / 2);
            boolean contains = false;
            for (int i = 0; i < visitedSquares.size(); i++) {
                int[] point = visitedSquares.get(i);
                if (point[0] == nextX && point[1] == nextY) {
                    contains = true;
                }
            }
            if (done = grid[1 - (d % 2)][wallY][wallX] && !contains) {
                grid[1 - (d % 2)][wallY][wallX] = false;
                visitedSquares.add(new int[]{nextX, nextY});
                if (fill) {
                    boolean[][][] gridState = new boolean[2][grid[0].length][grid[0][0].length];
                    for (int i = 0; i < grid.length; i++) {
                        for (int j = 0; j < grid[0].length; j++) {
                            for (int k = 0; k < grid[0][0].length; k++) {
                                gridState[i][j][k] = grid[i][j][k];
                            }
                        }
                    }
                    ((TimerListener) (timer.getActionListeners()[0])).mazeStates.add(gridState);
                }
                return 1 + randomWalk(nextX, nextY, endX, endY, visitedSquares, fill);
            }
        }

        return 1;
    }

    public boolean makeMazeGen(int x, int y) {
        boolean changed = false;
        if (!grid[0][y - 1][x]) {
            if (Math.random() > 0.5) {
                grid[0][y - 1][x] = true;
                if (!isLooped(x, y)) {
                    changed = true;
                } else {
                    grid[0][y - 1][x] = false;
                }
            }
        }
        if (!grid[1][y][x]) {
            if (Math.random() > 0.5) {
                grid[1][y][x] = true;
                if (!isLooped(x, y)) {
                    changed = true;
                } else {
                    grid[1][y][x] = false;
                }
            }
        }
        if (!grid[0][y][x]) {
            if (Math.random() > 0.5) {
                grid[0][y][x] = true;
                if (!isLooped(x, y)) {
                    changed = true;
                } else {
                    grid[0][y][x] = false;
                }
            }
        }
        if (!grid[1][y][x - 1]) {
            if (Math.random() > 0.5) {
                grid[1][y][x - 1] = true;
                if (!isLooped(x, y)) {
                    changed = true;
                } else {
                    grid[1][y][x - 1] = false;
                }
            }
        }
        return changed;
    }

    public void convertMaze() {
        maze = new int[1 + GRIDHEIGHT * 2][1 + GRIDWIDTH * 2];
        for (int i = 0; i < maze.length; i += 2) {
            for (int j = 0; j < maze[i].length; j += 2) {
                maze[i][j] = 1;
            }
        }
        for (int y = 0; y <= GRIDHEIGHT; y++) {
            for (int x = 0; x <= GRIDWIDTH; x++) {
                if (grid[0][y][x]) {
                    maze[1 + y * 2][x * 2] = 1;
                }
            }
        }
        for (int y = 0; y <= GRIDHEIGHT; y++) {
            for (int x = 0; x <= GRIDWIDTH; x++) {
                if (grid[1][y][x]) {
                    maze[y * 2][1 + x * 2] = 1;
                }
            }
        }
    }
    public void solve() {
        convertMaze();
        solveRunner(1, 1, -1);
    }

    private boolean solveRunner(int x, int y, int d) {
        if (x == maze[0].length - 2 && y == maze.length - 2) {
            return true;
        }
        if (maze[y][x] != 0) {
            return false;
        }
        maze[y][x] = 2;
        boolean path = false;
        if (d != 3 ) {
            path = solveRunner(x + 1, y, 1);
        }
        if (d != 0 && !path) {
            path = solveRunner(x, y + 1, 2);
        }
        if (d != 1 && !path) {
            path = solveRunner(x - 1, y, 3);
        }
        if (d != 2 && !path) {
            path = solveRunner(x, y - 1, 0);
        }
        if (!path) {
            maze[y][x] = 0;
        }
        return path;
    }

    public int getGRIDWIDTH() {
        return GRIDWIDTH;
    }

    public int getGRIDHEIGHT() {
        return GRIDHEIGHT;
    }
}