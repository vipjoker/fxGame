package mygame;

import javafx.geometry.Point2D;
import mygame.editor.views.CcNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by oleh on 01.06.18.
 */
public class DataBaseTest {
    @Test
    public void test(){
        assertTrue(true)
        ;
    }

    @Test
    public void ccNodeTransformTest(){
        Point2D p = new Point2D(0,0);

        CcNode parent = new CcNode();
        parent.setX(100);
        parent.setY(100);
        CcNode child = new CcNode();
        child.setY(10);
        child.setX(10);
        CcNode grandChild = new CcNode();
        grandChild.setX(5);
        grandChild.setY(5);



        child.addChild(grandChild);
        parent.addChild(child);
        final Point2D result = grandChild.convertToLocalSpace(p);

        System.out.println((result == p));
        System.out.println(result);
    }

}
