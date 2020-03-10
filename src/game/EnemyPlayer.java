package game;

import utilities.Vector2D;

import static game.Constants.*;

public class EnemyPlayer implements Controller{

    private static final int MAX_POS_UPDATE_DELAY = 20;

    private static final int MAX_ACTION_DELAY = 10;

    private Game game;
    private EnemyShip enemyShip;
    private int playerType;
    private Action action;
    private int framesUntilUpdatingEnemyPosition;
    private int actionDelay;
    private boolean canAct;
    private boolean acted;
    private Vector2D targetPosition;

    private double distanceBetween;
    private double angleDifference;


    public EnemyPlayer(Game g){
        game = g;
        action = new Action();
        acted = true;
        canAct();
        nextUpdateIn();
    }

    public void setEnemyShip(EnemyShip e){
        enemyShip = e;
    }


    public void revive(){
        playerType = (int)(Math.random() * 5);
        System.out.println("Enemy "+ playerType);
        getTargetPosition();
        nextUpdateIn();
        acted = true;
        canAct();
    }

    private void nextUpdateIn(){
        framesUntilUpdatingEnemyPosition = (int)(Math.random() * MAX_POS_UPDATE_DELAY);
    }

    private void nextActionIn(){
        actionDelay = (int)(Math.random()* MAX_ACTION_DELAY);
    }

    private void canAct(){
        if (acted){
            acted = false;
            nextActionIn();
        } else if (canAct || actionDelay > 0){
            canAct = false;
            actionDelay--;
        } else{
            canAct = true;
        }
    }

    private void getTargetPosition(){
        if (framesUntilUpdatingEnemyPosition > 0){
            framesUntilUpdatingEnemyPosition--;
        } else{
            targetPosition = game.getShipPosition();
            nextUpdateIn();
        }
    }

    @Override
    public Action action() {
        canAct();
        switch (playerType) {
            case 0:
                rotateShootAction();
                break;
            case 1:
                chaseShootAction();
                break;
            case 2:
                aimShootAction();
                break;
            case 3:
                aimWarpAction();
                break;
            case 4:
            default:
                randomAction();
                break;
        }
        return action;
    }

    private void rotateShootAction(){
        action.shoot = (Math.random() > 0.8);
        action.turn = 1;
        thrustIfSlowerThan(10);
    }

    private void chaseShootAction (){
        if (analysePosition()) {
            if (distanceBetween < 300) {
                if (distanceBetween < 150) {
                    if (angleDifference >= 0 - (Math.PI /16) && angleDifference <= (Math.PI / 16) && canAct) {
                        action.shoot = true;
                        acted = true;
                    } else {
                        action.shoot = false;
                    }

                    action.thrust = 0;
                } else{
                    action.thrust = 1;
                }
            } else {
                randomAction();
            }
            turnToPlayer();
        } else {
            randomAction();
        }
        //return action;
    }

    private void thrustIfSlowerThan(double minSpeed){
        if (enemyShip.velocity.mag() < minSpeed){
            action.thrust = 1;
        } else{
            action.thrust = 0;
        }
    }

    private void randomAction(){
        action.turn = (int)(Math.random() * 3) - 1;
        action.thrust = (int) (Math.random() * 2);
        action.shoot = (Math.random() > 0.95);
        action.warp = (Math.abs(Math.random()) == 0);
    }

    private void aimShootAction(){
        if (analysePosition()) {
            if (angleDifference >= 0 - (Math.PI /4) && angleDifference <= (Math.PI / 4) && canAct) {
                action.shoot = true;
                acted = true;
            } else {
                action.shoot = false;
            }
            turnToPlayer();
        } else{
            randomAction();
        }
        thrustIfSlowerThan(20);
    }

    private void aimWarpAction(){
        if (analysePosition()) {
            if (angleDifference >= 0 - (Math.PI / 4) && angleDifference <= (Math.PI / 4) && canAct) {
                action.warp = true;
                acted = true;
            } else {
                action.warp = false;
                turnToPlayer();
            }
        } else{
            randomAction();
        }
    }

    private void turnToPlayer(){
        if (angleDifference > 0 ) {
            action.turn = -1;
        } else if (angleDifference < 0) {
            action.turn = 1;
        } else{
            action.turn = 0;
        }
    }

    private boolean analysePosition(){
        getTargetPosition();
        if (targetPosition == null){
            return false;
        }
        Vector2D vectorBetween = enemyShip.position.getVectorTo(targetPosition, FRAME_WIDTH, FRAME_HEIGHT);
        distanceBetween = vectorBetween.mag();
        angleDifference = vectorBetween.angle(enemyShip.direction);
        return true;
    }

}
