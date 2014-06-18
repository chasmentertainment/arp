package com.chasmentertainment.arpg.AnimationParser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.chasmentertainment.arpg.AnimationParser.AnimationFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tyler on 4/26/2014.
 */
public class SpriteDef implements AnimationParserConstants {

    private HashMap<String, List<Rectangle>> mAnimations = new HashMap<String, List<Rectangle>>();
//    private HashMap<String, StillFrame> mStills = new HashMap<String, StillFrame>();

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
                } else if ( frameType.equals(SpriteSheetType.Cells.getType()) ){
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
                        List<Rectangle> anim = parseAnimation(node);
                        if (anim != null) {
                            mAnimations.put(name, anim);
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private Rectangle parseFrame(Node frameNode) {
        Rectangle frame = null;
        if (frameNode.getNodeName().equals(TAG_NAME_SPRITE_FRAME)) {
            int x,y,width,height;
            NamedNodeMap attributes = frameNode.getAttributes();
            x = Integer.parseInt(attributes.getNamedItem(ATTRIBUTE_SPRITE_FRAME_X).getNodeValue());
            y = Integer.parseInt(attributes.getNamedItem(ATTRIBUTE_SPRITE_FRAME_Y).getNodeValue());
            width = Integer.parseInt(attributes.getNamedItem(ATTRIBUTE_SPRITE_FRAME_WIDTH).getNodeValue());
            height = Integer.parseInt(attributes.getNamedItem(ATTRIBUTE_SPRITE_FRAME_HEIGHT).getNodeValue());
            frame = new Rectangle(x, y, width, height);
        }

        return frame;
    }

    private ArrayList<Rectangle> parseAnimation(Node animationNode) {
        ArrayList<Rectangle> animDef = new ArrayList<Rectangle>();
        String name;
        name = animationNode.getAttributes().getNamedItem(ATTRIBUTE_SPRITE_ANIMATION_NAME).getNodeValue();
        if (name != null) {
            for (int j = 0; j < animationNode.getChildNodes().getLength(); j++) {
                Node frameNode = animationNode.getChildNodes().item(j);

                if (frameNode.getNodeName().equals(TAG_NAME_SPRITE_FRAME)) {
                    Rectangle frame = parseFrame(frameNode);
                    if (frame != null) {
                        animDef.add(frame);
                    }
                } else if (frameNode.getNodeName().equals(TAG_NAME_SPRITE_FRAMESET)) {
                    List<Rectangle> frames = parseFrameset(frameNode);
                    animDef.addAll(frames);
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

    private List<Rectangle> parseFrameset(Node framesetNode) {
        ArrayList<Rectangle> frames = new ArrayList<Rectangle>();

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
                            Rectangle f = new Rectangle(x, y, width, height);
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
                            Rectangle f = new Rectangle(x, y, width, height);
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
                            Rectangle f = new Rectangle(x, y, width, height);
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
                            Rectangle f = new Rectangle(x, y, width, height);
                            frames.add(f);
                        }
                    }
                }
            }
        }

        return frames;
    }

    /*  Create gets an animation from the file this object was created with. If the animation has
        already been created, use the old animation, otherwise create one.
     */
    public Animation getAnimationNamed(String name) {
        Animation anim = null;

        List<Rectangle> def = mAnimations.get(name);
        if (def != null) {
            anim = AnimationFactory.makeAnimation(getTexture(), def);
        }

        return anim;
    }

    /*  returns the texture specified by the filename.

     */
    public Texture getTexture() {
        if (mTexture != null) {
            return mTexture.get();
        }

        Texture t = new Texture(mFilename);
        mTexture = new WeakReference<Texture>(t);
        return t;
    }
}
