package dev.donkz.pendragon.controller;

public interface ViewableController extends HierarchicalController<ViewableController>, RenderableController{
    void switchView();
}
