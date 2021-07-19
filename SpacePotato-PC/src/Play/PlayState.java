package Play;

import Draw.Draw;
import Draw.Image;
import GameData.GameData;
import GameData.ScoreData;
import GameObject.MenuObject;
import GameState.ContainerState;
import GameState.MenuState;
import GameState.PhysicsState;
import Input.Input;
import java.util.LinkedList;
import java.util.Random;

public class PlayState extends ContainerState {

    private class Point {
        public int x, y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    };
    
    private final String POTATO_PATH = "potato.png";
    
    private final String NAME_MAIN;
    private final String NAME_PLAY = "NAME_PLAY";
    private final String NAME_INPUT = "NAME_INPUT";
    private final String NAME_LOSE = "NAME_LOSE";
    
    private MenuState loseState;
    private PhysicsState playArea;
    private InputArea inputArea;

    private MenuObject lblScore;
    
    private final int MAX_POTATO_SPEED = 2;
    private final double POTATO_SIZE_PERCENT = 0.1f;
    
    private int viewWidth, viewHeight;
    private double potatoSize;
    private int level;
    
    private int topScore;
    private int score;
    private int deltaScore;
    
    private LinkedList<Potato> potatoes;
    
    private Image imgPotato;
    
    public PlayState(String name, String mainStateName, int viewWidth, int viewHeight) {
        super(name, 6, 10);
        NAME_MAIN = mainStateName;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        potatoSize = viewWidth * POTATO_SIZE_PERCENT;
        potatoes = new LinkedList<>();
        setupStates();
        setupScoreLabel();
        setupImages();
        level = 0;
    }
    
    private void setupStates() {
        setupPlayArea();
        setupInputArea();
        setupLoseState();
    }
    
    private void setupPlayArea() {
        playArea = new PhysicsState(NAME_PLAY);
        playArea.setLocation(0, 0, 6, 8);
    }
    
    private void setupInputArea() {
        inputArea = new InputArea(NAME_INPUT);
        inputArea.setLocation(0, 9, 6, 1);
        inputArea.setState(InputArea.States.CHOOSE);
        
        addObject(inputArea);
    }
    
    private void setupLoseState() {
        loseState = new MenuState("", 3, 7) {
            @Override
            public void draw(Draw graphics) {
                if (inputArea.getState() == InputArea.States.LOSE)
                    super.draw(graphics);
            }
        };
        loseState.setLocation(0, 0, 6, 9);
        MenuObject lblWrong = new MenuObject("", 1, 2, 1, 1) {
            @Override
            public void draw(Draw graphics) {
                graphics.setColor(255, 255, 255);
                graphics.drawCentredText("Wrong Answer", getBounds());
            }
        };
        MenuObject lblScore = new MenuObject("", 1, 3, 1, 1) {
            @Override
            public void draw(Draw graphics) {
                graphics.setColor(255, 255, 255);
                graphics.drawCentredText("Score: " + score, getBounds());
            }
        };
        MenuObject lblBest = new MenuObject("", 1, 4, 1, 1) {
            @Override
            public void draw(Draw graphics) {
                graphics.setColor(255, 255, 255);
                graphics.drawCentredText("Best:" + topScore, getBounds());
            }
        };
        loseState.addObject(lblWrong);
        loseState.addObject(lblScore);
        loseState.addObject(lblBest);
        this.addObject(loseState);
    }
    
    private void setupScoreLabel() {
        lblScore = new MenuObject("", 0, 0, 6, 2) {
            @Override
            public void draw(Draw graphics) {
                graphics.setColor(255, 255, 255);
                graphics.drawCentredText("" + ((int) score), getBounds());
            }
        };
        addObject(lblScore);
    }
    
    private void setupImages() {
        imgPotato = new Image(POTATO_PATH);
    }
    
    private void nextLevel() {
        inputArea.reset();
        potatoes.clear();
        level++;
        score += deltaScore;
        generatePotatoes();
        setChoices();
        deltaScore = potatoes.size() * 100;
        inputArea.setState(InputArea.States.CHOOSE);
    }
    
    private void generatePotatoes() {
        Random random = new Random();
        int numPotatoes = random.nextInt((int) ((level*1.3) - level) + 1) + level;
        int gridSize = (int) (Math.sqrt(numPotatoes)+1);
        int slotWidth = viewWidth / gridSize;
        int slotHeight = (int) ((viewHeight-inputArea.getBounds().getHeight()) / gridSize);
        LinkedList<Point> availableSlots = new LinkedList<>();
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                availableSlots.addLast(new Point(x, y));
            }
        }

        for (int i = 0; i < numPotatoes; i++) {
            int gridIndex = random.nextInt(availableSlots.size());
            Point gridSlot = availableSlots.get(gridIndex);
            
            double minX = gridSlot.x*slotWidth;
            double maxX = minX + slotWidth - potatoSize;
            double x = random.nextInt((int) (maxX - minX)) + minX;
            
            double minY = gridSlot.y*slotHeight;
            double maxY = minY + slotHeight - potatoSize;
            double y = random.nextInt((int) (maxY - minY)) + minY;
            
            double minSpeed = level*0.5;
            double xSpeed = random.nextDouble() * (MAX_POTATO_SPEED - minSpeed) + minSpeed;
            double ySpeed = random.nextDouble() * (MAX_POTATO_SPEED - minSpeed) + minSpeed;
            
            Potato potato = new Potato(imgPotato, xSpeed, ySpeed, viewWidth, (int) inputArea.getBounds().getY());
            potato.setBounds(x, y, potatoSize, potatoSize);
            potatoes.addLast(potato);
            availableSlots.remove(gridSlot);
        }
    }
    
    private void setChoices() {
        Random random = new Random();
        int[] options = new int[3];
        boolean isOneCorrect = false;
        int min = potatoes.size() / 2;
        int max = potatoes.size() * 2;
        for (int i = 0; i < options.length; i++) {
            options[i] = random.nextInt(max - min) + min;
            for (int j = 0; j < options.length; j++) {
                if (options[i] == options[j] && i != j) {
                    options[i]++;
                }
                if (options[i] == potatoes.size()) isOneCorrect = true;
            }
        }
        if (!isOneCorrect) {
            int correct = random.nextInt(3);
            options[correct] = potatoes.size();
        }
        for (int i = 0; i < options.length; i++) {
            inputArea.setChoice(i, options[i]);
        }
    }
    
    private void reset() {
        potatoes.clear();
        level = 0;
        score = 0;
        inputArea.setState(InputArea.States.CHOOSE);
    }
    
    @Override
    public void update(Input input, GameData gameData) {
        super.update(input, gameData);
        viewWidth = gameData.getWidth();
        viewHeight = gameData.getHeight();
        potatoSize = viewWidth / 10;
        if (level == 0) nextLevel();
        ScoreData scoreData = (ScoreData) gameData;
        topScore = (int) scoreData.getScore(0);
        deltaScore = (deltaScore < 10) ? 10 : deltaScore-1;
        updatePotatoes();
        updateInput(gameData);
    }
    
    private void updatePotatoes() {
        updatePotatoesHorizontally();
        updatePotatoesVertically();
    }
    
    private void updatePotatoesHorizontally() {
        for (Potato potato : potatoes) {
            potato.moveHorizontally();
            for (Potato other : potatoes) {
                if (potato.isColliding(other)) {
                    potato.reverseHorizontally();
                    other.reverseHorizontally();
                    potato.moveHorizontally();
                }
            }
        }
    }
    
    private void updatePotatoesVertically() {
        for (Potato potato : potatoes) {
            potato.moveVertically();
            for (Potato other : potatoes) {
                if (potato.isColliding(other)) {
                    potato.reverseVertically();
                    other.reverseVertically();
                    potato.moveVertically();
                }
            }
        }
    }
    
    private void updateInput(GameData data) {
        switch (inputArea.getState()) {
            case CHOOSE:
                updateChooseInput(data);
                break;
            case CONTINUE:
                updateContinueInput();
                break;
            case LOSE:
                updateLoseInput();
                break;
        }
    }
    
    private void updateChooseInput(GameData data) {
        int selection = inputArea.getChoice();
        if (selection >= 0) {
            ScoreData scoreData = (ScoreData) data;
            if (selection == potatoes.size()) {
                inputArea.setState(InputArea.States.CONTINUE);
                scoreData.incrementScore(deltaScore);
            } else {
                inputArea.setState(InputArea.States.LOSE);
                scoreData.updateScores();
                scoreData.resetScore();
            }
        }
    }
    
    private void updateContinueInput() {
        if (inputArea.shouldContinue()) {
            nextLevel();
        }
    }
    
    private void updateLoseInput() {
        String loseChoice = inputArea.getLoseChoice();
        if (!loseChoice.equals("")) {
            if (loseChoice.equals(inputArea.AGAIN)) {
                reset();
            } else if (loseChoice.equals(inputArea.BACK)) {
                reset();
                setNextState(NAME_MAIN);
            }
        }
        inputArea.reset();
    }
    
    @Override
    public void draw(Draw graphics) {
        super.draw(graphics);
        drawPotatoes(graphics);
        if (inputArea.getState() == InputArea.States.LOSE) {
            loseState.draw(graphics);
        } else {
            lblScore.draw(graphics);
        }
    }
    
    private void drawPotatoes(Draw graphics) {
        for (Potato potato : potatoes) {
            potato.draw(graphics);
        }
    }
}
