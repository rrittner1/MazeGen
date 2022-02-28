import javax.swing.*;

public class Test {
    boolean[][][] grid;
    final static int GRIDWIDTH = 4;
    final static int GRIDHEIGHT = 4;
    public Test() {
        grid = new boolean[2][GRIDHEIGHT + 1][GRIDWIDTH + 1];
        for (int i = 0; i < GRIDHEIGHT; i++) {
            grid[0][i][0] = true;
            grid[0][i][GRIDWIDTH] = true;
        }
        for (int i = 0; i < GRIDWIDTH; i++) {
            grid[1][0][i] = true;
            grid[1][GRIDHEIGHT][i] = true;
        }
        grid[0][1][1] = true;
        grid[1][1][1] = true;
        grid[0][1][2] = true;
        grid[1][2][1] = true;
        grid[0][2][1] = true;
    }
    public static void main(String[] args) {
        String[] arg = {"+++++++++++++++++++++[>++>+++>++++>+++++>++++++<<<<<-]+++++++++++++++++++++>>>----.>>-----.-----.<-.>-----.-.<<<<<-----------."};
        int[] arr = new int[100];
        for (String str : arg) {
            decode(str, 0, arr);
        }
    }

    public static void decode(String str, int begin, int[] arr) {
        int ptr = 0;
        if (arr[0] == 1) {
            System.out.println("here");
        }
        char[] code = str.substring(begin).toCharArray();
        for (int i = 0; i < code.length; i++) {
            char c = code[i];
            if (c == '>') {
                ptr++;
            }
            if (c == '<') {
                ptr--;
            }
            if (c == '+') {
                arr[ptr]++;
            }
            if (c == '-') {
                arr[ptr]--;
            }
            if (c == '.') {
                char o =(char) arr[ptr];
                System.out.print(o);
            }
            if (c == '[') {
                int condition = ptr;
                while (arr[condition] != 0) {
                    decode(str, i + 1, arr);
                }
            }
            if (c == ']') {
                return;
            }
        }
    }
    public static int[] test(int[] arr) {
        arr[0] = 6;
        return arr;
    }
    private int isLooped(int x, int y) {
        return isLoopedRunner(x, y, x, y, -1);
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
            if (inBounds(x, y, 1) && grid[1][y][x] && d != 1) {
                if (x + 1 == startX && y == startY) {
                    return 1;
                }
                if (!(x + 1 == GRIDWIDTH)) {
                    loop = isLoopedRunner(x + 1, y, startX, startY, 3);
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
            if (inBounds(x - 1, y, 1) && grid[1][y][x - 1] && d != 3) {
                if (x - 1 == startX && y == startY) {
                    return 1;
                }
                if (!(x - 1 == 0)) {
                    loop = isLoopedRunner(x - 1, y, startX, startY, 1);
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
            } if (bounds == 1) {
                return 2;
            }
            return loop;
        } catch (StackOverflowError e) {/*
            System.out.println("" + x + y + startX + startY + d);
            for (int j = 0; j < grid[0].length; j++) {
                for (int i = 0; i < grid[0][j].length; i++) {
                    if (grid[0][j][i]) {
                        System.out.print("|");
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
            System.out.println();
            for (int j = 0; j < grid[1].length; j++) {
                for (int i = 0; i < grid[1][j].length; i++) {
                    if (grid[1][j][i]) {
                        System.out.print("_");
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
            System.exit(0);
            return 0;*/
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
}

