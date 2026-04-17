import java.util.ArrayList;

/**
 * Draws a tower and its stacking items using the Shapes project.
 * Each item is drawn proportionally to its size.
 * The visual position matches the logical size of each item.
 * 
 * TowerView is responsible only for drawing, not for rules.
 * 
 * @author Daniel Cortes
 * @version 2.0
 */
public class TowerView
{
    private int width;
    private int maxHeight;
    private boolean visible;

    private Rectangle towerBody;
    private ArrayList<Rectangle> marks;
    private ArrayList<Rectangle> blocks;

    private static final int PIXELS_PER_UNIT = 10;
    private static final int BASE_X = 140;
    private static final int BASE_Y = 250;
    private static final int OFFSET_X = -70;
    private static final int OFFSET_Y = -15;

    /**
     * Creates a view for a tower.
     * 
     * @param width tower width (in cm)
     * @param maxHeight maximum height (in cm)
     */
    public TowerView(int width, int maxHeight)
    {
        this.width = width;
        this.maxHeight = maxHeight;
        visible = false;

        marks = new ArrayList<Rectangle>();
        blocks = new ArrayList<Rectangle>();

        buildTower();
        buildMarks();
    }

    /**
     * Makes the tower visible.
     */
    public void makeVisible()
    {
        visible = true;
        showStructure();
    }

    /**
     * Makes the tower invisible.
     */
    public void makeInvisible()
    {
        hideAll();
        visible = false;
    }

    /**
     * Updates the drawing according to current items.
     * Each item is drawn with height proportional to its size.
     * The bottom of each item aligns with its cumulative height mark.
     * 
     * @param items list of stacking items in the tower
     */
    public void update(ArrayList<StackingItem> items)
    {
        if(!visible) {
            return;
        }

        clearBlocks();
        drawBlocks(items);
    }

    // ---------------- PRIVATE METHODS ----------------

    /**
     * Creates the main rectangle of the tower.
     */
    private void buildTower()
    {
        towerBody = new Rectangle();

        int pixelWidth = width * PIXELS_PER_UNIT;
        int pixelHeight = maxHeight * PIXELS_PER_UNIT;

        towerBody.changeColor("black");
        towerBody.changeSize(pixelHeight, pixelWidth);

        int x = BASE_X - pixelWidth / 2;
        int y = BASE_Y - pixelHeight;

        towerBody.moveHorizontal(x + OFFSET_X);
        towerBody.moveVertical(y + OFFSET_Y);
    }

    /**
     * Creates centimeter marks using thin rectangles.
     * Each mark corresponds to one unit of height.
     */
    private void buildMarks()
    {
        int leftX = BASE_X - (width * PIXELS_PER_UNIT) / 2;

        for(int i = 1; i <= maxHeight; i++) {
            Rectangle mark = new Rectangle();
            mark.changeColor("black");
            mark.changeSize(2, 10);

            int y = BASE_Y - (i * PIXELS_PER_UNIT);
            mark.moveHorizontal((leftX + OFFSET_X) - 12);
            mark.moveVertical(y + OFFSET_Y);

            marks.add(mark);
        }
    }

    /**
     * Shows the tower structure.
     */
    private void showStructure()
    {
        towerBody.makeVisible();

        for(Rectangle mark : marks) {
            mark.makeVisible();
        }
    }

    /**
     * Hides everything.
     */
    private void hideAll()
    {
        towerBody.makeInvisible();

        for(Rectangle mark : marks) {
            mark.makeInvisible();
        }

        clearBlocks();
    }

    /**
     * Removes drawn blocks from the view.
     */
    private void clearBlocks()
    {
        for(Rectangle r : blocks) {
            r.makeInvisible();
        }
        blocks.clear();
    }

    /**
     * Draws all cups and lids currently in the tower.
     * Each item's height in pixels equals its size * PIXELS_PER_UNIT.
     * Items are stacked from the bottom, each starting where the previous ends.
     * This makes the visual position match the logical size.
     */
    private void drawBlocks(ArrayList<StackingItem> items)
    {
        int pixelWidth = width * PIXELS_PER_UNIT;
        int leftX = BASE_X - pixelWidth / 2;

        int currentBottom = BASE_Y; // starts at the base of the tower

        for(int i = 0; i < items.size(); i++) {
            StackingItem it = items.get(i);

            int itemHeightPixels = (it instanceof Lid) ? PIXELS_PER_UNIT : it.getSize() * PIXELS_PER_UNIT;
            int itemWidthPixels = pixelWidth - 4 - (it.getSize() * 2);
            if(itemWidthPixels < 20) {
                itemWidthPixels = 20;
            }

            Rectangle block = new Rectangle();
            block.changeColor(getColor(it));
            block.changeSize(itemHeightPixels, itemWidthPixels);

            int y = currentBottom - itemHeightPixels;
            int centeredX = leftX + (pixelWidth - itemWidthPixels) / 2;

            block.moveHorizontal(centeredX + OFFSET_X);
            block.moveVertical(y + OFFSET_Y);

            block.makeVisible();
            blocks.add(block);

            currentBottom -= itemHeightPixels;
        }
    }

    /**
     * Returns the correct color for an item.
     */
    private String getColor(StackingItem it)
    {
        return it.getColor();
    }
}
