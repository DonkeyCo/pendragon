package dev.donkz.pendragon.controller;

public interface HierarchicalController<T extends Controller> extends Controller {
    T getParentController();
    void setParentController(T parentController);
}
