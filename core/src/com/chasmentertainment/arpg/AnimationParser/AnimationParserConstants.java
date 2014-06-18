package com.chasmentertainment.arpg.AnimationParser;

/**
 * Created by Tyler on 5/14/2014.
 */
public interface AnimationParserConstants {
    // XML tag names <xxx ...>
    static final String TAG_NAME_SPRITE = "sprite";
    static final String TAG_NAME_SPRITE_ANIMATION = "animation";
    static final String TAG_NAME_SPRITE_STILL = "still";
    static final String TAG_NAME_SPRITE_FRAME = "frame";
    static final String TAG_NAME_SPRITE_FRAMESET = "frameset";

    // XML tag attribute names <... xxx="...">
    static final String ATTRIBUTE_SPRITE_ANIMATION_NAME = "name";
    static final String ATTRIBUTE_SPRITE_SHEET_FILENAME = "sheet";
    static final String ATTRIBUTE_SPRITE_FRAME_TYPE = "type";
    static final String ATTRIBUTE_SPRITE_CELLS_COLUMNS = "columns";
    static final String ATTRIBUTE_SPRITE_CELLS_ROWS = "rows";
    static final String ATTRIBUTE_SPRITE_FRAME_X = "x";
    static final String ATTRIBUTE_SPRITE_FRAME_Y = "y";
    static final String ATTRIBUTE_SPRITE_FRAME_WIDTH = "width";
    static final String ATTRIBUTE_SPRITE_FRAME_HEIGHT = "height";
    static final String ATTRIBUTE_SPRITE_FRAMESET_TYPE = "type";
    static final String ATTRIBUTE_SPRITE_FRAMESET_START_ROW = "startRow";
    static final String ATTRIBUTE_SPRITE_FRAMESET_START_COL = "startCol";
    static final String ATTRIBUTE_SPRITE_FRAMESET_END_ROW = "endRow";
    static final String ATTRIBUTE_SPRITE_FRAMESET_END_COL = "endCol";
}
