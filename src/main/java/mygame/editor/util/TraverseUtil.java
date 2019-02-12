package mygame.editor.util;

import mygame.editor.model.Node;
import mygame.editor.views.NodeView;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TraverseUtil {


    public static boolean contains(List<NodeView> list,NodeView target){
        NodeView node = find(list, target::equals);
        return node != null;
    }

    public static  <T>void traverse(T node, Consumer<T> action, Function<T,List<T>> childrenSuplier) {
        action.accept(node);
        for (T ccNode : childrenSuplier.apply(node)) {
            traverse(ccNode, action,childrenSuplier);
        }
    }

    public static NodeView find(List<NodeView> nodes, Predicate<NodeView> predicate){
        Holder<NodeView> holder = new Holder<>();
        for (NodeView node : nodes) {
            traverse(node,n->{
                if(predicate.test(n)){
                    holder.result = n;
                }
            },NodeView::getChildren);
        }
        return holder.result;
    }

    public static void action(List<NodeView> nodes, Consumer<NodeView> predicate){
        for (NodeView node : nodes) {
            traverse(node, predicate,NodeView::getChildren);
        }

    }
    private static class Holder<T>{
        T result;
    }

}
