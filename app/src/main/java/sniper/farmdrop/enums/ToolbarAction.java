package sniper.farmdrop.enums;

/**
 * Created by sniper on 01/13/17.
 * Use this flags to send toolbar setup from the fragments,
 * because they are the views and the toolbar need to be corresponding with the current view
 */
public enum ToolbarAction {
    ACTION_SET_TITLE,
    ACTION_SHOW_TITLE,
    ACTION_HIDE_TITLE,
    ACTION_BACK_TO_PREVIOUS_SCREEN,
    ACTION_REMOVE_BACK_TO_PREVIOUS_SCREEN,
    ACTION_SHOW_SEARCH,
    ACTION_HIDE_SEARCH;
}
