import java.util.LinkedList;

public class AndOrTreeNode
{
    private int id;
    private Boolean solvable;
    private AndOrTreeNodeType nodeType;
    private AndOrTreeNode parent;
    private LinkedList<AndOrTreeNode> children;

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

    public boolean isLeaf(AndOrTreeNode node)
    {
        if (node.children.size() == 0)
            return true;
        else
            return false;
    }

    public AndOrTreeNode copy()
    {
        AndOrTreeNode copyNode = new AndOrTreeNode(this.id, this.nodeType, this.solvable);
        return copyNode;
    }


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
