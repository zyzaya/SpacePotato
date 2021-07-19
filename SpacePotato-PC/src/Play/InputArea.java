package Play;

import Draw.Draw;
import GameData.GameData;
import GameObject.MenuObject;
import GameState.MenuState;
import Input.Input;

public class InputArea extends MenuState {

    public final String AGAIN = "again";
    public final String BACK = "back";
    
    private final int NUM_CHOICES = 3;
    
    private final String NAME_CHOICE = "stateChoice";
    private final String NAME_CONTINUE = "stateContinue";
    private final String NAME_LOSE = "stateLose";
    
    private MenuState stateChoice;
    private MenuState stateContinue;
    private MenuState stateLose;
    
    private int choiceSelection;
    private boolean shouldContinue;
    
    private States state;
    private int[] choices;
    
    private String loseChoice;
    
    public enum States {
        CHOOSE,
        CONTINUE,
        LOSE
    }
    
    public InputArea(String name) {
        super(name, 1, 1);
        loseChoice = "";
        shouldContinue = false;
        choiceSelection = -1;
        state = States.CHOOSE;
        choices = new int[NUM_CHOICES];
        for (int i = 0; i < NUM_CHOICES; i++) {
            choices[i] = 0;
        }
        setupStates();
    }
    
    private void setupStates() {
        setupStateChoice();
        setupStateContinue();
        setupStateLose();
    }
    
    private void setupStateChoice() {
        stateChoice = new MenuState(NAME_CHOICE, 3, 1);
        stateChoice.setLocation(0, 0, 1, 1);
        for (int i = 0; i < NUM_CHOICES; i++) {
            final int J = i;
            MenuObject btnChoice = new MenuObject("", i, 0, 1, 1) {
                
                private boolean prior = true;
                
                @Override
                public void draw(Draw graphics) {
                    graphics.setColor(100, 255, 0, 0);
                    graphics.drawRect(getBounds());
                    graphics.setColor(255, 255, 255);
                    graphics.drawCentredText(choices[J] + "", getBounds());
                }
                
                @Override
                public void update(Input input, GameData gameData) {
                    super.update(input, gameData);
                    if (!prior && input.isPressed(Game.Game.MOUSE1) && getBounds().contains(input.getMouseX(), input.getMouseY()))
                        setChoiceSelection(J);
                    prior = input.isPressed(Game.Game.MOUSE1);
                }
            };
            stateChoice.addObject(btnChoice);
        }
        this.addObject(stateChoice);
    }
    
    private void setupStateContinue() {
        stateContinue = new MenuState(NAME_CONTINUE, 1, 1);
        stateContinue.setLocation(0, 0, 1, 1);
        MenuObject btnContinue = new MenuObject("", 0, 0, 1, 1) {
            private boolean prior = true;
            
            @Override
            public void draw(Draw graphics) {
                graphics.setColor(100, 255, 0, 0);
                graphics.drawRect(getBounds());
                graphics.setColor(255, 255, 255);
                graphics.drawCentredText("Continue", getBounds());
            }
            
            @Override
            public void update(Input input, GameData gameData) {
                super.update(input, gameData);
                if (!prior && input.isPressed(Game.Game.MOUSE1) && getBounds().contains(input.getMouseX(), input.getMouseY()))
                    continueButton();
                prior = input.isPressed(Game.Game.MOUSE1);
            }
        };
        stateContinue.addObject(btnContinue);
        addObject(stateContinue);
    }
    
    private void setupStateLose() {
        stateLose = new MenuState(NAME_LOSE, 2, 1);
        stateLose.setLocation(0, 0, 1, 1);
        MenuObject btnAgain = new MenuObject("", 0, 0, 1, 1) {
            
            private boolean prior = true;
            
            @Override
            public void draw(Draw graphics) {
                graphics.setColor(100, 255, 0, 0);
                graphics.drawRect(getBounds());
                graphics.setColor(255, 255, 255);
                graphics.drawCentredText("Again", getBounds());
            }
            
            @Override
            public void update(Input input, GameData gameData) {
                super.update(input, gameData);
                if (!prior && input.isPressed(Game.Game.MOUSE1) && getBounds().contains(input.getMouseX(), input.getMouseY()))
                    again();
                prior = input.isPressed(Game.Game.MOUSE1);
            }
        };
        MenuObject btnBack = new MenuObject("", 1, 0, 1, 1) {
            
            private boolean prior = true;
            
            @Override
            public void draw(Draw graphics) {
                graphics.setColor(100, 255, 0 , 0);
                graphics.drawRect(getBounds());
                graphics.setColor(255, 255, 255);
                graphics.drawCentredText("Back", getBounds());
            }
            
            @Override
            public void update(Input input, GameData gameData) {
                super.update(input, gameData);
                if (!prior && input.isPressed(Game.Game.MOUSE1) && getBounds().contains(input.getMouseX(), input.getMouseY()))
                    back();
                prior = input.isPressed(Game.Game.MOUSE1);
            }
        };
        
        stateLose.addObject(btnAgain);
        stateLose.addObject(btnBack);
        
        addObject(stateLose);
    }
    
    private void continueButton() {
        shouldContinue = true;
    }
    
    private void again() {
        loseChoice = AGAIN;
    }
    
    private void back() {
        loseChoice = BACK;
    }
    
    private void setChoiceSelection(int i) {
        choiceSelection = choices[i];
    }
    
    public boolean shouldContinue() {
        return shouldContinue;
    }
    
    public String getLoseChoice() {
        return loseChoice;
    }
    
    public int getChoice() {
        return choiceSelection;
    }

    public void setChoice(int i, int value) {
        choices[i] = value;
    }
    
    public void setState(States state) {
        this.state = state;
    }
    
    public void reset() {
        shouldContinue = false;
        choiceSelection = -1;
        loseChoice = "";
        for (int i = 0; i < NUM_CHOICES; i++) {
            choices[i] = 0;
        }
    }
    
    public States getState() {
        return state;
    }
    
    @Override
    public void draw(Draw graphics) {
        super.draw(graphics);
        graphics.setColor(0, 0, 0);
        graphics.drawRect(getBounds());
        switch (state) {
            case CHOOSE:
                stateChoice.draw(graphics);
                break;
            case CONTINUE:
                stateContinue.draw(graphics);
                break;
            case LOSE:
                stateLose.draw(graphics);
                break;
        }
    }
    
    @Override
    public void update(Input input, GameData gameData) {
//        super.update(input, gameData);
        switch (state) {
            case CHOOSE:
                stateChoice.update(input, gameData);
                break;
            case CONTINUE:
                stateContinue.update(input, gameData);
                break;
            case LOSE:
                stateLose.update(input, gameData);
                break;
        }
    }
}
