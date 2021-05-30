package dev.donkz.pendragon.controller;

/**
 * Controller which provides a Hierarchy
 * @param <T>
 */
public interface HierarchicalController<T extends Controller> extends Controller {
    T getParentController();
    void setParentController(T parentController);
}
