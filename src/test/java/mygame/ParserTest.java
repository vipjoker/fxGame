package mygame;

import mygame.editor.parser.LocalParserKt;
import mygame.editor.views.CcNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {


    @Test
    public void shouldSaveNodeWithName(){
        CcNode ccNode = new CcNode();
        ccNode.setName("OLEH");
        List<CcNode> list = new ArrayList<>();
        list.add(ccNode);

        String result = LocalParserKt.createJsonFromNodes(list);
        System.out.println(result);
        assertTrue(result.contains("OLEH"));

        List<CcNode> parsedNodes = LocalParserKt.createNodesFromString(result);
        assertNotNull("Node is not null",parsedNodes.get(0) );
        assertEquals("Node name", "OLEH", parsedNodes.get(0).getName().getValue());
        assertNotEquals("Node name", "OLEH9", parsedNodes.get(0).getName().getValue());
    }

    @Test
    public void childrenShouldBeSerialized(){
        List<CcNode> repository = new ArrayList<>();
        CcNode parent = new CcNode();
        parent.setName("Parent");
        CcNode childOne = new CcNode();
        childOne.setName("Child one");
        CcNode childTwo = new CcNode();
        childTwo.setName("Child two");
        parent.addChild(childOne);
        parent.addChild(childTwo);




        repository.add(parent);


        String json = LocalParserKt.createJsonFromNodes(repository);
        System.out.println(json);
        assertTrue(json.contains("Child one"));
        assertTrue(json.contains("Child two"));


        List<CcNode> nodesFromString = LocalParserKt.createNodesFromString(json);
        assertEquals(nodesFromString.get(0).getChildren().size(),2);

    }
}
