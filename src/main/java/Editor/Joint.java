// automatically generated by the FlatBuffers compiler, do not modify

package Editor;

import java.nio.*;
import java.lang.*;

import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Joint extends Table {
  public static Joint getRootAsJoint(ByteBuffer _bb) { return getRootAsJoint(_bb, new Joint()); }
  public static Joint getRootAsJoint(ByteBuffer _bb, Joint obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public Joint __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int bodyA() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int bodyB() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public Vec2f posA() { return posA(new Vec2f()); }
  public Vec2f posA(Vec2f obj) { int o = __offset(8); return o != 0 ? obj.__assign(o + bb_pos, bb) : null; }
  public Vec2f posB() { return posB(new Vec2f()); }
  public Vec2f posB(Vec2f obj) { int o = __offset(10); return o != 0 ? obj.__assign(o + bb_pos, bb) : null; }
  public byte type() { int o = __offset(12); return o != 0 ? bb.get(o + bb_pos) : 0; }

  public static void startJoint(FlatBufferBuilder builder) { builder.startObject(5); }
  public static void addBodyA(FlatBufferBuilder builder, int bodyA) { builder.addInt(0, bodyA, 0); }
  public static void addBodyB(FlatBufferBuilder builder, int bodyB) { builder.addInt(1, bodyB, 0); }
  public static void addPosA(FlatBufferBuilder builder, int posAOffset) { builder.addStruct(2, posAOffset, 0); }
  public static void addPosB(FlatBufferBuilder builder, int posBOffset) { builder.addStruct(3, posBOffset, 0); }
  public static void addType(FlatBufferBuilder builder, byte type) { builder.addByte(4, type, 0); }
  public static int endJoint(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}
