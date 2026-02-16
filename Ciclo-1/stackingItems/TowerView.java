import java.util.ArrayList;

/**
 * Draws a tower and its stacking items using the Shapes project.
 * 
 * TowerView is responsible only for drawing, not for rules.
 * 
 * @author Daniel
 * @version 1.0
 */
public class TowerView
{
    private int width;
    private int maxHeight;
    private boolean visible;

    private Rectangle towerBody;
    private ArrayList<Rectangle> marks;
    private ArrayList<Rectangle> blocks;

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

        int pixelWidth = width * 10;
        int pixelHeight = maxHeight * 10;

        towerBody.changeColor("black");
        towerBody.changeSize(pixelHeight, pixelWidth);

        int x = 140 - pixelWidth / 2;
        int y = 250 - pixelHeight;

        towerBody.moveHorizontal(x - 70);
        towerBody.moveVertical(y - 15);
    }

    /**
     * Creates centimeter marks using thin rectangles.
     */
    private void buildMarks()
    {
        int pixelHeight = maxHeight * 10;
        int baseY = 250;
        int leftX = 140 - (width * 10) / 2;

        for(int i = 1; i <= maxHeight; i++) {
            Rectangle mark = new Rectangle();
            mark.changeColor("black");
            mark.changeSize(2, 10);

            int y = baseY - (i * 10);
            mark.moveHorizontal((leftX - 70) - 12);
            mark.moveVertical((y - 15));

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
     */
    private void drawBlocks(ArrayList<StackingItem> items)
    {
        int pixelWidth = width * 10;
        int leftX = 140 - pixelWidth / 2;
        int baseY = 250;

        for(int i = 0; i < items.size(); i++) {
            StackingItem it = items.get(i);

            Rectangle block = new Rectangle();
            block.changeColor(getColor(it));
            int itemWidth = (pixelWidth - 4) - (it.getSize() * 5);

            if(itemWidth < 20) {
            itemWidth = 20;
            }

            block.changeSize(10, itemWidth);

            int y = baseY - ((i + 1) * 10);
            int centeredX = leftX + (pixelWidth - itemWidth) / 2;
            block.moveHorizontal((centeredX - 70));
            block.moveVertical((y - 15));

            block.makeVisible();
            blocks.add(block);
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