package SSAD_Ass3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = Integer.parseInt(scanner.nextLine());
        Board.setInstance(n);
        int[][] grid = Board.getGrid();
        Figure[][] figures = Board.getFigures();
        RedTeam redTeam = new RedTeam();
        GreenTeam greenTeam = new GreenTeam();

        int[] greenCords = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        int[] redCords = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        Figure initialGreenFigure = new Figure(Colors.GREEN);
        Figure initialRedFigure = new Figure(Colors.RED);
        setAllPositions(initialGreenFigure, greenCords[0] - 1, greenCords[1] - 1, figures);
        setAllPositions(initialRedFigure, redCords[0] - 1, redCords[1] - 1, figures);
        greenTeam.addFigure(initialGreenFigure);
        redTeam.addFigure(initialRedFigure);

        int m = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < m; i++) {
            int[] coinString = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            grid[coinString[0] - 1][coinString[1] - 1] = coinString[2];
        }

        processGame(scanner, grid, figures, new Team[]{greenTeam, redTeam});
    }

    /**
     * Sets the position of a figure on the board
     * @param figure the figure to be placed
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param figures the board's figure grid
     */
    public static void setAllPositions(Figure figure, int x, int y, Figure[][] figures) {
        figures[x][y] = figure;
        figure.setPosition(x, y);
    }

    /**
     * Function for any move action
     * @param direction the direction to move
     * @param grid the game grid for coins
     * @param figures the grid with figures positions
     * @param team the current player's team
     * @param enemyTeam the opposing team
     * @param isClone indicates if the figure being moved is a clone
     */
    public static void moveFunction(String direction, int[][] grid, Figure[][] figures, Team team, Team enemyTeam, boolean isClone) {
        Figure curFigure = isClone ? (Figure) team.team.get(1) : (Figure) team.team.get(0);
        Position curFigurePosition = curFigure.getPosition();
        String type = curFigure.getColor() == Colors.RED ? "RED" : "GREEN";
        type = type + (isClone ? "CLONE" : "");
        if (!curFigurePosition.checkDirection(direction, curFigure.moveStep(), grid.length)) {
            System.out.println("INVALID ACTION");
            return;
        }
        Position newPosition = new Position(-1, -1);
        switch (direction) {
            case "UP":
                newPosition.x = curFigurePosition.x - curFigure.moveStep();
                newPosition.y = curFigurePosition.y;
                break;
            case "DOWN":
                newPosition.x = curFigurePosition.x + curFigure.moveStep();
                newPosition.y = curFigurePosition.y;
                break;
            case "LEFT":
                newPosition.x = curFigurePosition.x;
                newPosition.y = curFigurePosition.y - curFigure.moveStep();
                break;
            case "RIGHT":
                newPosition.x = curFigurePosition.x;
                newPosition.y = curFigurePosition.y + curFigure.moveStep();
                break;
            default:
                break;
        }
        Figure targetFigure = figures[newPosition.x][newPosition.y];
        if (curFigure.getAlive() && (targetFigure == null || targetFigure.getColor() != curFigure.getColor())) {
            int oldX = curFigurePosition.x;
            int oldY = curFigurePosition.y;
            if (targetFigure == null && grid[newPosition.x][newPosition.y] == 0) {
                setAllPositions(curFigure, newPosition.x, newPosition.y, figures);
                figures[oldX][oldY] = null;
                System.out.println(type + " MOVED TO " + (newPosition.x + 1) + " " + (newPosition.y + 1));
            } else if (targetFigure != null) {
                targetFigure.setAlive();
                enemyTeam.deleteFigure(targetFigure);
                setAllPositions(curFigure, newPosition.x, newPosition.y, figures);
                figures[oldX][oldY] = null;
                String targetColor = targetFigure.getColor() == Colors.RED ? "RED" : "GREEN";
                if (targetFigure.getClone()) {
                    targetColor += "CLONE";
                }
                System.out.println(type + " MOVED TO " + (newPosition.x + 1) + " " + (newPosition.y + 1)
                        + " AND KILLED " + targetColor);
            } else if (grid[newPosition.x][newPosition.y] != 0) {
                curFigure.addScore(grid[newPosition.x][newPosition.y]);
                setAllPositions(curFigure, newPosition.x, newPosition.y, figures);
                figures[oldX][oldY] = null;
                System.out.println(type + " MOVED TO " + (newPosition.x + 1) + " " + (newPosition.y + 1)
                        + " AND COLLECTED " + grid[newPosition.x][newPosition.y]);
                grid[newPosition.x][newPosition.y] = 0;
            }
        } else {
            System.out.println("INVALID ACTION");
        }
    }

    /**
     * Changes the playing style of a figure
     * @param curFigure the figure whose style will be changed
     * @param figureName the name (color + type) of the figure
     */
    public static void changeStyleFunction(Figure curFigure, String figureName) {
        if (!curFigure.getAlive()) {
            System.out.println("INVALID ACTION");
            return;
        }
        if (curFigure.getState().equals("NormalState")) {
            curFigure.makeAttack();
            System.out.println(figureName + " CHANGED STYLE TO ATTACKING");
        } else {
            curFigure.makeNormal();
            System.out.println(figureName + " CHANGED STYLE TO NORMAL");
        }
    }

    /**
     * Function to copy a figure
     * @param figure the figure to clone
     * @param grid the game grid for coins
     * @param figures the grid with figures positions
     * @param type the type of figure that will be cloned
     * @param team the current team
     */
    public static void cloneFunction(Figure figure, int[][] grid, Figure[][] figures, String type, Team team) {
        Position clonePosition = new Position(figure.getPosition().y, figure.getPosition().x);
        if (figure.getAlive() && (figure.getPosition().x != figure.getPosition().y)
                && team.team.size() == 1
                && (grid[clonePosition.x][clonePosition.y] == 0)
                && (figures[clonePosition.x][clonePosition.y] == null)
                && !type.equals("REDCLONE") && !type.equals("GREENCLONE")) {
            Figure cloneFigure = (Figure) figure.clone();
            team.addFigure(cloneFigure);
            figures[clonePosition.x][clonePosition.y] = cloneFigure;
            System.out.println(type + " CLONED TO " + (clonePosition.x + 1) + " " + (clonePosition.y + 1));
        } else {
            System.out.println("INVALID ACTION");
        }
    }

    /**
     * Processes the game actions based on the input.
     * @param scanner the Scanner to read input
     * @param grid the game grid for coins
     * @param figures the grid with figures positions
     * @param teams an array of two teams: GreenTeam, RedTeam
     */
    public static void processGame(Scanner scanner, int[][] grid, Figure[][] figures, Team[] teams) {
        int p = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < p; i++) {
            String[] command = scanner.nextLine().split(" ");
            Args args = new Args();
            args.command = command;
            args.figures = figures;
            args.grid = grid;
            args.teams = teams;
            if (i % 2 == 0) {
                greenProcess(args);
            } else {
                redProcess(args);
            }
        }
        int greenScore = teams[0].getCurrentScore();
        int redScore = teams[1].getCurrentScore();
        if (greenScore > redScore) {
            System.out.println("GREEN TEAM WINS. SCORE " + greenScore + " " + redScore);
        } else if (greenScore < redScore) {
            System.out.println("RED TEAM WINS. SCORE " + greenScore + " " + redScore);
        } else {
            System.out.println("TIE. SCORE " + greenScore + " " + redScore);
        }
    }

    /**
     * Processes actions for the Green team.
     * @param args the necessary arguments
     */
    public static void greenProcess(Args args) {
        String[] command = args.command;
        Figure[][] figures = args.figures;
        int[][] grid = args.grid;
        GreenTeam team = (GreenTeam) args.teams[0];
        if (command[0].equals("GREEN")) {
            switch (command[1]) {
                case "COPY":
                    cloneFunction((Figure) team.team.get(0), grid, figures, "GREEN", team);
                    break;
                case "STYLE":
                    Figure curFigure = (Figure) team.team.get(0);
                    changeStyleFunction(curFigure, "GREEN");
                    break;
                default:
                    moveFunction(command[1], grid, figures, team, (Team) args.teams[1], false);
                    break;
            }
        } else if (command[0].equals("GREENCLONE")) {
            if (team.team.size() < 2) {
                System.out.println("INVALID ACTION");
                return;
            }
            switch (command[1]) {
                case "COPY":
                    System.out.println("INVALID ACTION");
                    break;
                case "STYLE":
                    Figure curFigure = (Figure) team.team.get(1);
                    changeStyleFunction(curFigure, "GREENCLONE");
                    break;
                default:
                    moveFunction(command[1], grid, figures, team, (Team) args.teams[1], true);
                    break;
            }
        }
    }

    /**
     * Processes actions for the Red team.
     * @param args the necessary arguments
     */
    public static void redProcess(Args args) {
        String[] command = args.command;
        Figure[][] figures = args.figures;
        int[][] grid = args.grid;
        RedTeam team = (RedTeam) args.teams[1];
        if (command[0].equals("RED")) {
            switch (command[1]) {
                case "COPY":
                    cloneFunction((Figure) team.team.get(0), grid, figures, "RED", team);
                    break;
                case "STYLE":
                    Figure curFigure = (Figure) team.team.get(0);
                    changeStyleFunction(curFigure, "RED");
                    break;
                default:
                    moveFunction(command[1], grid, figures, team, (Team) args.teams[0], false);
                    break;
            }
        } else if (command[0].equals("REDCLONE")) {
            if (team.team.size() < 2) {
                System.out.println("INVALID ACTION");
                return;
            }
            switch (command[1]) {
                case "COPY":
                    System.out.println("INVALID ACTION");
                    break;
                case "STYLE":
                    Figure curFigure = (Figure) team.team.get(1);
                    changeStyleFunction(curFigure, "REDCLONE");
                    break;
                default:
                    moveFunction(command[1], grid, figures, team, (Team) args.teams[0], true);
                    break;
            }
        }
    }
}

/**
 * Class to encapsulate input arguments for processing game commands.
 */
class Args {
    int[][] grid;
    Figure[][] figures;
    Team[] teams;
    String[] command;
}

interface Score {
    int getScore();
}

/**
 * Represents a team in the game.
 */
class Team implements Score {
    List<Score> team = new ArrayList<>();
    int totalScore;
    public Team() {
        totalScore = 0;
    }
    void addFigure(Figure figure) {
        team.add(figure);
    }
    void deleteFigure(Figure figure) {
        team.remove(figure);
        totalScore += figure.getScore();
    }
    int getCurrentScore() {
        int newCurScore = totalScore;
        for (Score figure : team) {
            newCurScore += figure.getScore();
        }
        return newCurScore;
    }
    @Override
    public int getScore() {
        int scoreSum = totalScore;
        for (Score figure : team) {
            scoreSum += figure.getScore();
        }
        return scoreSum;
    }
}

/**
 * Represents the Red Team.
 */
class RedTeam extends Team {
    public RedTeam() {
        super();
    }
}

/**
 * Represents the Green Team.
 */
class GreenTeam extends Team {
    public GreenTeam() {
        super();
    }
}

/**
 * Represents a game figure.
 */
class Figure implements Score, CloneFigure {
    private FigureState state;
    private final Colors color;
    private int score;
    private boolean isClone = false;
    private boolean isAlive = true;
    private Position position;
    public Figure(Colors color) {
        this.state = new NormalState();
        this.color = color;
    }
    public void setState(FigureState newState) {
        this.state = newState;
    }
    public void setPosition(int x, int y) {
        this.position = new Position(x, y);
    }
    public void setAlive() {
        this.isAlive = false;
    }
    public String getState() {
        if (this.state instanceof NormalState) {
            return "NormalState";
        } else if (this.state instanceof AttackState) {
            return "AttackState";
        }
        return "NoneState";
    }
    public Position getPosition() {
        return this.position;
    }
    public boolean getAlive() {
        return this.isAlive;
    }
    public boolean getClone() {
        return this.isClone;
    }
    public Colors getColor() {
        return this.color;
    }
    public int moveStep() {
        return state.moveStep();
    }
    public void makeNormal() {
        state.makeNormal(this);
    }
    public void makeAttack() {
        state.makeAttack(this);
    }
    public void addScore(int scoreToAdd) {
        this.score += scoreToAdd;
    }
    @Override
    public int getScore() {
        return this.score;
    }
    @Override
    public CloneFigure clone() {
        Figure cloneFigure = new Figure(this.color);
        cloneFigure.isClone = true;
        cloneFigure.setPosition(this.position.y, this.position.x);
        return cloneFigure;
    }
}

/**
 * Represents a position on the board
 */
class Position {
    int x;
    int y;
    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean checkDirection(String direction, int moveStep, int n) {
        return switch (direction) {
            case "UP" -> x - moveStep >= 0;
            case "DOWN" -> x + moveStep < n;
            case "RIGHT" -> y + moveStep < n;
            case "LEFT" -> y - moveStep >= 0;
            default -> false;
        };
    }
    @Override
    public String toString() {
        return "Position: x - " + this.x + ", y - " + this.y;
    }
}

enum Colors {
    RED,
    GREEN
}

/**
 * Class for state handling
 */
interface FigureState {
    void makeNormal(Figure figure);
    void makeAttack(Figure figure);
    int moveStep();
}

interface CloneFigure {
    CloneFigure clone();
}

class PlayingStyle {
}

/**
 * Represents NormalState state
 */
class NormalState extends PlayingStyle implements FigureState {
    @Override
    public int moveStep() {
        return 1;
    }
    @Override
    public void makeAttack(Figure figure) {
        figure.setState(new AttackState());
    }
    @Override
    public void makeNormal(Figure figure) {
    }
}

/**
 * Represents AttackState state
 */
class AttackState extends PlayingStyle implements FigureState {
    @Override
    public int moveStep() {
        return 2;
    }
    @Override
    public void makeAttack(Figure figure) {
    }
    @Override
    public void makeNormal(Figure figure) {
        figure.setState(new NormalState());
    }
}

/**
 * Singleton class for board
 */
class Board {
    private int n;
    private static Board board;
    private static int[][] grid;
    private static Figure[][] figures;
    private Board(int n) {
        this.n = n;
    }
    public static void setInstance(int n) {
        if (board == null) {
            board = new Board(n);
            grid = new int[n][n];
            figures = new Figure[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    figures[i][j] = null;
                }
            }
        }
    }
    public static int[][] getGrid() {
        return grid;
    }
    public static Figure[][] getFigures() {
        return figures;
    }
}
