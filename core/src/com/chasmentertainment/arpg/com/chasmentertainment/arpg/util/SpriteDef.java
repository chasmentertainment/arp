package com.chasmentertainment.arpg.com.chasmentertainment.arpg.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tyler on 4/26/2014.
 */
public class SpriteDef {

    static final String TAG_NAME_SPRITE = "sprite";
    static final String TAG_NAME_SPRITE_ANIMATION = "animation";
    static final String TAG_NAME_SPRITE_STILL = "still";
    static final String TAG_NAME_SPRITE_FRAME = "frame";
    static final String TAG_NAME_SPRITE_FRAMESET = "frameset";

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


    private HashMap<String, AnimationDef> mAnimations = new HashMap<String, AnimationDef>();
    private HashMap<String, StillFrame> mStills = new HashMap<String, StillFrame>();

    private String mFilename;
    private WeakReference<Texture> mTexture;
    private Boolean mIsCells;
    private SpriteSheetType mSheetType;

    private int mRows;
    private int mColumns;

    public enum FramesetType {
        Wrap("wrap"),
        Box("box");

        String type;

        FramesetType(String s) {
            type = s;
        }

        public String getType() {
            return type;
        }
    }

    public enum SpriteSheetType {
        Cells("cells"),
        Explicit("explicit");

        String type;

        SpriteSheetType(String s) {
            type = s;
        }

        public String getType() {
            return type;
        }
    }

    public SpriteDef(String filename) {
        FileHandle file = Gdx.files.local(filename);
//        if (file.exists()) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(file.file());
                doc.getDocumentElement().normalize();

                Node filenameNode = doc.getFirstChild().getAttributes().getNamedItem(ATTRIBUTE_SPRITE_SHEET_FILENAME);
                mFilename = filenameNode.getNodeValue();

                String frameType = doc.getFirstChild().getAttributes().getNamedItem(ATTRIBUTE_SPRITE_FRAME_TYPE).getNodeValue();

                if (frameType.equals(SpriteSheetType.Explicit.getType())) {
                    mSheetType = SpriteSheetType.Explicit;
                } else if (frameType.equals(SpriteSheetType.Cells.getType())) {
                    mSheetType = SpriteSheetType.Cells;
                    mColumns = Integer.parseInt(doc.getFirstChild().getAttributes().getNamedItem(ATTRIBUTE_SPRITE_CELLS_COLUMNS).getNodeValue());
                    mRows = Integer.parseInt(doc.getFirstChild().getAttributes().getNamedItem(ATTRIBUTE_SPRITE_CELLS_ROWS).getNodeValue());

                    if (mColumns == 0 || mRows == 0) {
                        throw new IllegalArgumentException("Must define number of rows & columns for a cell-type sprite");
                    }
                }

                NodeList animationNodes = doc.getElementsByTagName(TAG_NAME_SPRITE_ANIMATION);


                for (int i = 0; i < animationNodes.getLength(); i++) {
                    Node node = animationNodes.item(i);
                    String name = node.getAttributes().getNamedItem(ATTRIBUTE_SPRITE_ANIMATION_NAME).getNodeValue();
                    if (name != null) {
                        AnimationDef anim = parseAnimation(node);
                        if (anim != null) {
                            mAnimations.put(name, anim);
                        }
                    }
                }

//                for (int i = 0; i < nodes.getLength(); i++) {
//                    Node node = nodes.item(i);
//
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
    }

    private StillFrame parseFrame(Node frameNode) {
        StillFrame frame = null;
        if (frameNode.getNodeName().equals(TAG_NAME_SPRITE_FRAME)) {
            int x,y,width,height;
            NamedNodeMap attributes = frameNode.getAttributes();
            x = Integer.parseInt(attributes.getNamedItem(ATTRIBUTE_SPRITE_FRAME_X).getNodeValue());
            y = Integer.parseInt(attributes.getNamedItem(ATTRIBUTE_SPRITE_FRAME_Y).getNodeValue());
            width = Integer.parseInt(attributes.getNamedItem(ATTRIBUTE_SPRITE_FRAME_WIDTH).getNodeValue());
            height = Integer.parseInt(attributes.getNamedItem(ATTRIBUTE_SPRITE_FRAME_HEIGHT).getNodeValue());
            frame = new StillFrame(x, y, width, height);
        }

        return frame;
    }

    private AnimationDef parseAnimation(Node animationNode) {
        AnimationDef animDef = new AnimationDef();
        String name;
        name = animationNode.getAttributes().getNamedItem(ATTRIBUTE_SPRITE_ANIMATION_NAME).getNodeValue();
        if (name != null) {
            for (int j = 0; j < animationNode.getChildNodes().getLength(); j++) {
                Node frameNode = animationNode.getChildNodes().item(j);

                if (frameNode.getNodeName().equals(TAG_NAME_SPRITE_FRAME)) {
                    StillFrame frame = parseFrame(frameNode);
                    if (frame != null) {
                        animDef.addFrame(frame);
                    }
                } else if (frameNode.getNodeName().equals(TAG_NAME_SPRITE_FRAMESET)) {
                    List<StillFrame> frames = parseFrameset(frameNode);
                    animDef.addFrames(frames);
                }


            }
        }

        return animDef;
    }

    private int getFrameWidth() {
        return getTexture().getWidth()/mColumns;
    }

    private int getFrameHeight() {
        return getTexture().getHeight()/mRows;
    }

    private List<StillFrame> parseFrameset(Node framesetNode) {
        ArrayList<StillFrame> frames = new ArrayList<StillFrame>();

        String framesetType = framesetNode.getAttributes().getNamedItem(ATTRIBUTE_SPRITE_FRAMESET_TYPE).getNodeValue();

        if (framesetType.equals(FramesetType.Wrap.getType()) || framesetType.equals(FramesetType.Box.getType())) {
            int startRow = Integer.parseInt(framesetNode.getAttributes().getNamedItem(ATTRIBUTE_SPRITE_FRAMESET_START_ROW).getNodeValue());
            int startCol = Integer.parseInt(framesetNode.getAttributes().getNamedItem(ATTRIBUTE_SPRITE_FRAMESET_START_COL).getNodeValue());
            int endRow = Integer.parseInt(framesetNode.getAttributes().getNamedItem(ATTRIBUTE_SPRITE_FRAMESET_END_ROW).getNodeValue());
            int endCol = Integer.parseInt(framesetNode.getAttributes().getNamedItem(ATTRIBUTE_SPRITE_FRAMESET_END_COL).getNodeValue());

            if (framesetType.equals(FramesetType.Wrap.type)) {

                if (startRow * mColumns + startCol > endRow * mColumns + endCol) {
                    for (int i = startRow; i >= endRow; i--) {
                        int start, end;
                        start = (i == startRow) ? startCol : mColumns;
                        end = (i == endRow) ? endCol : 0;
                        for (int j = start; j >= end; j--) {
                            int width = getFrameWidth();
                            int height = getFrameHeight();
                            int x = j * width;
                            int y = i * height;
                            StillFrame f = new StillFrame(x, y, width, height);
                            frames.add(f);
                        }
                    }
                } else {
                    for (int i = startRow; i <= endRow; i++) {
                        int start, end;
                        start = (i == startRow) ? startCol : 0;
                        end = (i == endRow) ? endCol : mColumns;
                        for (int j = start; j <= end; j++) {
                            int width = getFrameWidth();
                            int height = getFrameHeight();
                            int x = j * width;
                            int y = i * height;
                            StillFrame f = new StillFrame(x, y, width, height);
                            frames.add(f);
                        }
                    }
                }
            } else if (framesetType.equals(FramesetType.Box.type)) {
                if (startRow * mColumns + startCol > endRow * mColumns + endCol) {
                    for (int i = startRow; i >= endRow; i--) {
                        for (int j = startCol; j >= endCol; j--) {
                            int width = getFrameWidth();
                            int height = getFrameHeight();
                            int x = j * width;
                            int y = i * height;
                            StillFrame f = new StillFrame(x, y, width, height);
                            frames.add(f);
                        }
                    }
                } else {
                    for (int i = startRow; i <= endRow; i++) {
                        for (int j = startCol; j <= endCol; j++) {
                            int width = getFrameWidth();
                            int height = getFrameHeight();
                            int x = j * width;
                            int y = i * height;
                            StillFrame f = new StillFrame(x, y, width, height);
                            frames.add(f);
                        }
                    }
                }
            }
        }

        return frames;
    }

    public Animation getAnimationNamed(String name) {
        Animation anim = null;

        AnimationDef def = mAnimations.get(name);
        if (def != null) {
            anim = AnimationDef.makeAnimation(getTexture(), def);
        }

        return anim;
    }

    public Texture getTexture() {
        if (mTexture != null) {
            return mTexture.get();
//            return mTexture;
        }

        Texture t = new Texture(mFilename);
        mTexture = new WeakReference<Texture>(t);
//        mTexture = t;
        return t;
    }
//
//    public void getSpriteNamed(String name) {
//
//    }

//    public void getSpriteFromAnimationFrame() {}

}
