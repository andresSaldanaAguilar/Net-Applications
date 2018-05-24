package dropbocs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> implements Serializable{
    private T data;
    private List<TreeNode> children;
    private TreeNode parent;

    public TreeNode() {
        data = null;
        children = new ArrayList<>();
        parent = null;
    }
    public TreeNode(T data) {
        this.data = data;
        children = new ArrayList<>();
        parent = null;
    }

    public void addChild(TreeNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(T data) {
        TreeNode<T> newChild = new TreeNode<>(data);
        newChild.setParent(this);
        children.add(newChild);
    }

    public void addChildren(List<TreeNode> children) {
        for(TreeNode t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<TreeNode> getChildren() {
        return children;
    }
    
    public boolean hasChild(T data) {
        for(TreeNode child : children){
            if(child.getData().equals(data))
                return true;
        }
        return false;
    }
    public TreeNode getChild(T data) {
        for(TreeNode child : children){
            if(child.getData().equals(data))
                return child;
        }
        return null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public TreeNode getParent() {
        return parent;
    }
    
    public boolean hasChildren() {
        return children.size() > 0;
    }
}