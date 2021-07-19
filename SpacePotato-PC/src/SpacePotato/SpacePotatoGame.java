package SpacePotato;

import Draw.Draw;
import Draw.Image;
import Game.Game;
import GameData.ScoreData;
import GameObject.MenuObject;
import GameState.MenuState;
import GameState.ScoreState;
import Play.PlayState;
import Save.SaveData;
import Save.SaveItem;

public class SpacePotatoGame {

    private Game game;
    private ScoreData gameData;
    
    private final String SAVE_PATH = "space_potato_data";
    
    private final int VIEW_WIDTH = 330;
    private final int VIEW_HEIGHT = 560;
    
    private final String FONT_NAME = "Arial";
    private final int FONT_SIZE;
    
    private final String NAME_MENU = "STATE_MENU";
    private final String NAME_SCORE = "STATE_SCORE";
    private final String NAME_PLAY = "STATE_PLAY";
    
    private MenuState mainMenu;
    private ScoreState scoreState;
    private PlayState playState;
    
    private Image imgBackground;
    
    public SpacePotatoGame() {
        FONT_SIZE = (int) (0.05f * VIEW_HEIGHT);
        
        gameData = new ScoreData(3);
        loadScores();
        
        game = new Game(gameData, VIEW_WIDTH, VIEW_HEIGHT) {
            @Override
            public void finish() {
                onClose();
            }
        };
        setupStates();
        setupImages();
        
        game.addGameState(mainMenu);
        game.addGameState(scoreState);
        game.addGameState(playState);
        
        game.setState(NAME_MENU);
        game.start();
    }
    
    private void loadScores() {
        SaveData save = new SaveData(SAVE_PATH);
        save.reload();
        SaveItem gd = save.getItem("SpaceData");
        if (gd != null)
            gameData.load(gd);
    }
    
    private void setupStates() {
        setupMenu();
        setupScoreState();
        setupPlay();
    }
    
    private void setupMenu() {
        mainMenu = new MenuState(NAME_MENU, 3, 10) {
            @Override
            public void draw(Draw graphics) {
                graphics.drawImage(getBounds(), imgBackground);
                graphics.setFont(FONT_NAME, FONT_SIZE);
                super.draw(graphics);
            }
        };
        MenuObject btnStart = new MenuObject(NAME_PLAY, 1, 2, 1, 1) {
            @Override
            public void draw(Draw graphics) {
                graphics.setColor(255, 255, 255);
                graphics.drawCentredText("Start", getBounds());
            }
        };
        MenuObject btnScores = new MenuObject(NAME_SCORE, 1, 4, 1, 1) {
            @Override
            public void draw(Draw graphics) {
                graphics.setColor(255, 255, 255);
                graphics.drawCentredText("Scores", getBounds());
            }
        };
        mainMenu.addObject(btnStart);
        mainMenu.addObject(btnScores);
    }
    
    private void setupScoreState() {
        scoreState = new ScoreState(NAME_SCORE, NAME_MENU, 3, "Arial", 0) {
            @Override
            public void draw(Draw graphics) {
                graphics.drawImage(getBounds(), imgBackground);
                graphics.setFont(FONT_NAME, FONT_SIZE);
                graphics.setColor(255, 255, 255);
                super.draw(graphics);
            }
        };
    }
    
    private void setupPlay() {
        playState = new PlayState(NAME_PLAY, NAME_MENU, VIEW_WIDTH, VIEW_HEIGHT) {
            @Override
            public void draw(Draw graphics) {
                graphics.drawImage(getBounds(), imgBackground);
                graphics.setFont(FONT_NAME, FONT_SIZE);
                super.draw(graphics);
            }
        };
    }
    
    private void setupImages() {
        imgBackground = new Image("spacescape.png");
    }
    
    private void onClose() {
        SaveData save = new SaveData(SAVE_PATH);
        SaveItem scores = gameData.getSaveData();
        scores.setName("SpaceData");
        save.addItem(scores);
        save.editItem("SpaceData", scores);
        save.saveAll();
    }
    
}
