package bridge.controller;

import bridge.domain.BridgeGame;
import bridge.domain.MapConverter;
import bridge.view.InputView;
import bridge.view.OutputView;

public class BridgeGameController {

    private static final String RESTART = "R";

    private final BridgeGame bridgeGame;
    private final MapConverter mapConverter;

    public BridgeGameController() {
        this.bridgeGame = new BridgeGame();
        this.mapConverter = new MapConverter();
    }

    public void process() {
        initializeGame();
        playGame();
        finishGame();
    }

    private void initializeGame() {
        OutputView.printStartMessage();
        OutputView.printBlankLine();
        setBridgeSize();
        bridgeGame.setCurrentRoute();
        bridgeGame.setTrialCount();
    }

    private void setBridgeSize() {
        OutputView.printBridgeSizeInputMessage();
        int bridgeSize = InputView.readBridgeSize();
        OutputView.printBlankLine();
        bridgeGame.setBridge(bridgeSize);
    }

    private void playGame() {
        takeTrial();
        if (isFinish()) {
            return;
        }
        retryGame();
    }

    private void retryGame() {
        while (checkRestart()) {
            bridgeGame.retry();
            mapConverter.initialize();
            takeTrial();
            if (isFinish()) {
                return;
            }
        }
    }

    private void takeTrial() {
        do {
            if (isFinish()) {
                return;
            }
            takeTurn();
        } while (bridgeGame.isMovementSuccess());
    }

    private void takeTurn() {
        OutputView.printNextMovementInputMessage();
        String nextMovement = InputView.readMoving();
        bridgeGame.move(nextMovement);
        setMap(nextMovement);
    }

    private boolean isFinish() {
        return bridgeGame.isLastSquare();
    }

    private void setMap(String nextMovement) {
        boolean success = bridgeGame.isMovementSuccess();
        mapConverter.drawNext(nextMovement, success);
        OutputView.printMap(mapConverter.getUpperMap(), mapConverter.getLowerMap());
    }

    private boolean checkRestart() {
        OutputView.printRestartMessage();
        String gameCommand = InputView.readGameCommand();
        return gameCommand.equals(RESTART);
    }

    private void finishGame() {
        OutputView.printResultHeader();
        OutputView.printMap(mapConverter.getUpperMap(), mapConverter.getLowerMap());
        OutputView.printBlankLine();
    }
}
