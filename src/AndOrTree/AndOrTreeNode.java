package AndOrTree;

import java.util.LinkedList;

public class AndOrTreeNode
{
    // 与或树结点的id。
    private int id;

    // 与或树结点是否可解，如果是叶子结点则false不可解，true可解，若不是叶子结点则为unknown。
    private Boolean solvable;

    // 与或树结点的类型，是一个枚举型变量，与结点，或结点。
    private AndOrTreeNodeType nodeType;

    // 与或树结点的父结点。
    private AndOrTreeNode parent;

    // 与或树结点的子结点，他的子结点可能是多个的也可能没有，用链表做。
    private LinkedList<AndOrTreeNode> children;

    // 树的构造方法。
    public AndOrTreeNode(int id, AndOrTreeNodeType nodeType, Boolean solvable)
    {
        this.id = id;
        this.solvable = solvable;
        this.nodeType = nodeType;
        this.parent = null;
        this.children = new LinkedList<>();
    }

    public int getId()
    {
        return id;
    }

    public Boolean getSolvable()
    {
        return solvable;
    }

    public void setSolvable(Boolean solvable)
    {
        this.solvable = solvable;
    }

    public AndOrTreeNodeType getNodeType()
    {
        return nodeType;
    }

    public AndOrTreeNode getParent()
    {
        return parent;
    }

    public void setParent(AndOrTreeNode parent)
    {
        this.parent = parent;
    }

    public Iterable<AndOrTreeNode> getChildren()
    {
        return children;
    }

    public void addChild(AndOrTreeNode child)
    {
        children.addLast(child);
    }

    // 判断结点是不是叶子结点。
    public boolean isLeaf(AndOrTreeNode node)
    {
        // 如果他没有子结点就是叶子结点，否则不是叶子结点。
        if (node.children.size() == 0)
            return true;
        else
            return false;
    }

    // 在生成解树时，把原先树里的解结点复制出来。
    public AndOrTreeNode copy()
    {
        AndOrTreeNode copyNode = new AndOrTreeNode(this.id, this.nodeType, this.solvable);
        return copyNode;
    }


    // 显示结点信息。
    @Override
    public String toString()
    {
        String string;
        string = "id = " + id;
        if (parent == null)
        {
            string += ", parent = null";
        }
        else
        {
            string += ", parent = " + parent.id;
        }
        string += ", children = ";
        if (children.size() == 0)
        {
            string += "null";
        }
        else
        {
            for (AndOrTreeNode node : children)
            {
                string += node.id + " ";
            }
        }

        return string;
    }
}
