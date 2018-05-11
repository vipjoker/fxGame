package mygame.editor.repository;

import Editor.EditorRoot;
import Editor.NodeTree;
import Editor.Vec2f;
import com.google.flatbuffers.FlatBufferBuilder;
import mygame.editor.views.CcNode;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by oleh on 11.05.18.
 */
public class FlatBuffersRepository implements NodeRepository {

    public FlatBuffersRepository() {


    }

    @Override
    public List<CcNode> getAllNodes() {
        return null;
    }

    @Override
    public void save(CcNode node) {













        FlatBufferBuilder builder = new FlatBufferBuilder(1024);




        int editorRoot = EditorRoot.createEditorRoot(builder, 0, 0);



        int defName = builder.createString("Hello Oleh");
        int defName2 = builder.createString("How are you");
        Editor.NodeTree.startNodeTree(builder);
        Editor.NodeTree.addName(builder,defName);
        Editor.NodeTree.addPosition(builder,Vec2f.createVec2f(builder, 0, 2));
        int node1 = NodeTree.endNodeTree(builder);

        Editor.NodeTree.startNodeTree(builder);
        Editor.NodeTree.addName(builder,defName2);
        int node2 = NodeTree.endNodeTree(builder);

        int[] nodes = {node1,node2};
        int nodesVector = EditorRoot.createNodesVector(builder, nodes);
        EditorRoot.startEditorRoot(builder);
        EditorRoot.addNodes(builder,nodesVector);

        int editorId = EditorRoot.endEditorRoot(builder);



//        // Use the `createWeapon()` helper function to create the weapons, since we set every field.
//        int[] weaps = new int[2];
//        weaps[0] = Weapon.createWeapon(builder, weaponOneName, weaponOneDamage);
//        weaps[1] = Weapon.createWeapon(builder, weaponTwoName, weaponTwoDamage);
//
//        // Serialize the FlatBuffer data.
//        int name = builder.createString("Orc");
//        byte[] treasure = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
//        int inv = Monster.createInventoryVector(builder, treasure);
//        int weapons = Monster.createWeaponsVector(builder, weaps);
//        int pos = Vec3.createVec3(builder, 1.0f, 2.0f, 3.0f);
//
//        Monster.startMonster(builder);
//        Monster.addPos(builder, pos);
//        Monster.addName(builder, name);
//        Monster.addColor(builder, Color.Red);
//        Monster.addHp(builder, (short)300);
//        Monster.addInventory(builder, inv);
//        Monster.addWeapons(builder, weapons);
//        Monster.addEquippedType(builder, Equipment.Weapon);
//        Monster.addEquipped(builder, weaps[1]);
//        int orc = Monster.endMonster(builder);








        builder.finish(editorId); // You could also call `Monster.finishMonsterBuffer(builder, orc);`.
        ByteBuffer buf = builder.dataBuffer();

        saveByteBuffer(buf,new File("MYFILE.fb"));

    }


    @Override
    public CcNode getNodeById(long id) {

        ByteBuffer buf = loadByteBuffer(new File("MYFILE.fb"));
        EditorRoot rootAsEditorRoot = EditorRoot.getRootAsEditorRoot(buf);

        for (int i = 0; i < rootAsEditorRoot.nodesLength(); i++) {
            NodeTree nodes = rootAsEditorRoot.nodes(i);
            System.out.println(nodes.name());
        }
//        Monster monster = Monster.getRootAsMonster(buf);
//
//        // Note: We did not set the `mana` field explicitly, so we get back the default value.
//        assert monster.mana() == (short)150;
//        assert monster.hp() == (short)300;
//        assert monster.name().equals("Orc");
//        assert monster.color() == Color.Red;
//        assert monster.pos().x() == 1.0f;
//        assert monster.pos().y() == 2.0f;
//        assert monster.pos().z() == 3.0f;
//
//        // Get and test the `inventory` FlatBuffer `vector`.
//        for (int i = 0; i < monster.inventoryLength(); i++) {
//            assert monster.inventory(i) == (byte)i;
//        }
//
//        // Get and test the `weapons` FlatBuffer `vector` of `table`s.
//        String[] expectedWeaponNames = {"Sword", "Axe"};
//        int[] expectedWeaponDamages = {3, 5};
//        for (int i = 0; i < monster.weaponsLength(); i++) {
//            assert monster.weapons(i).name().equals(expectedWeaponNames[i]);
//            assert monster.weapons(i).damage() == expectedWeaponDamages[i];
//        }
//
//        // Get and test the `equipped` FlatBuffer `union`.
//        assert monster.equippedType() == Equipment.Weapon;
//        Weapon equipped = (Weapon)monster.equipped(new Weapon());
//        assert equipped.name().equals("Axe");
//        assert equipped.damage() == 5;
//
//        System.out.println("The FlatBuffer was successfully created and verified!");
//    }

        return null;
    }

    @Override
    public void delete(CcNode node) {

    }

    @Override
    public void update(CcNode node) {

    }

    private void saveByteBuffer(ByteBuffer buff,File file) {


        boolean append = false;

        try (FileChannel wChannel = new FileOutputStream(file, append).getChannel()){
            wChannel.write(buff);
        } catch (IOException e){
            e.printStackTrace();
        }


    }

    private ByteBuffer loadByteBuffer(File file){
        ByteBuffer buf = ByteBuffer.allocate(2048);
        try(FileInputStream inFile = new FileInputStream(file)){
            FileChannel inChannel = inFile.getChannel();
            while (inChannel.read(buf) != -1) {
                System.out.println("String read: " + ((ByteBuffer) (buf.flip())).asCharBuffer().get(0));
                buf.clear();
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return buf;
    }


    public static void main(String[] args) {
        FlatBuffersRepository flatBuffersRepository = new FlatBuffersRepository();
        flatBuffersRepository.save(null);
        CcNode nodeById = flatBuffersRepository.getNodeById(0);
    }
}
