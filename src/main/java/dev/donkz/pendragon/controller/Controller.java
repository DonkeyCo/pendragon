package dev.donkz.pendragon.controller;

public interface Controller {
    Controller getParentController();
    void setParentController(Controller parentController);
    void switchView();
}
