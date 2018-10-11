import com.sun.org.apache.xpath.internal.operations.And;
import org.omg.CORBA.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class AndOrTree
{
    private LinkedList<AndOrTreeNode> solutionNodes;
    private AndOrTreeNode root;
    private AndOrTreeNode[] treeNodes;

    private AndOrTree solutionTree;

    public AndOrTree(AndOrTreeNode root)
    {
        this.root = root;
    }


    public AndOrTree(String filePath) throws Exception
    {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        int nodeNumber = Integer.parseInt(scanner.nextLine());
        treeNodes = new AndOrTreeNode[nodeNumber];

        for (int i = 0; i < nodeNumber; i++)
        {
            String treeNodeText = scanner.nextLine();
            String[] line = treeNodeText.toLowerCase().split(" ");

            AndOrTreeNodeType nodeType;
            if (line[1].equals("or"))
                nodeType = AndOrTreeNodeType.OR_NODE;
            else if (line[1].equals("and"))
                nodeType = AndOrTreeNodeType.AND_NODE;
            else
                throw new Exception("Invalid data format of 'AndOrTreeNodeType' in file " + filePath + ", line " + (i + 1));

            Boolean solvable;
            if (line[2].equals("true"))
                solvable = true;
            else if (line[2].equals("false"))
                solvable = false;
            else if (line[2].equals("unknown"))
                solvable = null;
            else
                throw new Exception("Invalid data format of 'AndOrTreeNodeType' in file " + filePath + ", line " + (i + 1));

            int id = Integer.parseInt(line[0]);
            AndOrTreeNode node = new AndOrTreeNode(id,nodeType,solvable);
            treeNodes[node.getId()] = node;
            for (int j = 3; j < line.length; j++)
            {
                int children = Integer.parseInt(line[j]);
                treeNodes[children].setParent(node);
                node.addChild(treeNodes[children]);
            }

        }

        for (AndOrTreeNode node : treeNodes)
        {
            if (node.getParent() == null)
                root = node;
        }
    }


    public AndOrTree DepthFirstSearch()
    {
        solutionTree = null;

        if (root.isLeaf(root))
        {
            if (!root.getSolvable())
                return null;
        }

        boolean[] marked = new boolean[treeNodes.length];

        for (int i = 0; i < marked.length; i++)
            marked[i] = false;

        Bool solved = new Bool(false);

        DepthFirstSearch(root, marked, solved);

        return solutionTree;
    }

    private void DepthFirstSearch(AndOrTreeNode node, boolean[] marked, Bool solved)
    {
        if (solved.getValue())
            return;

        marked[node.getId()] = true;

        if (!node.isLeaf(node))
        {
            for (AndOrTreeNode child : node.getChildren())
            {
                DepthFirstSearch(child, marked, solved);
            }
        }
        else if (node.getSolvable())
        {
            solutionNodes = new LinkedList<>();
            solutionTree = traceUp(node, solutionNodes, marked);

            if (solutionNodes.contains(root))
                solved.setValue(true);
        }
    }

    public AndOrTree BreadthFirstSearch()
    {
        solutionTree = null;
        if (root.isLeaf(root))
        {
            if (!root.getSolvable())
                return null;
        }

        boolean[] marked = new boolean[treeNodes.length];

        LinkedList<AndOrTreeNode> bfsQueue = new LinkedList<>();
        return null;
    }




    private AndOrTree buildSolutionTree(LinkedList<AndOrTreeNode> solutionNodes)
    {
        solutionTree = new AndOrTree(this.root.copy());
        solutionTree.treeNodes = new AndOrTreeNode[this.treeNodes.length];
        for (AndOrTreeNode node : solutionNodes)
            solutionTree.treeNodes[node.getId()] = node.copy();
        for (AndOrTreeNode node : solutionNodes)
        {
            if (node.getParent() != null)
            {
                solutionTree.treeNodes[node.getId()].setParent(solutionTree.treeNodes[node.getParent().getId()]);
                solutionTree.treeNodes[node.getParent().getId()].addChild(solutionTree.treeNodes[node.getId()]);
            }
        }

        return solutionTree;
    }

    private AndOrTree traceUp(AndOrTreeNode node, LinkedList<AndOrTreeNode> solutionNodes, boolean[] marked)
    {
        if (node == root)
        {
            solutionNodes.addLast(node);
            return buildSolutionTree(solutionNodes);
        }

        if (node.isLeaf(node))
        {
            if (node.getSolvable())
            {
                solutionNodes.addLast(node);
                return traceUp(node.getParent(), solutionNodes, marked);
            }
            else
                return null;
        }

        if (node.getNodeType().equals(AndOrTreeNodeType.OR_NODE))
        {
            solutionNodes.addLast(node);
            return traceUp(node.getParent(), solutionNodes, marked);
        }

        if (node.getNodeType().equals(AndOrTreeNodeType.AND_NODE))
        {
            for (AndOrTreeNode child : node.getChildren())
            {
                if (!traceDown(child, solutionNodes, marked))
                    return null;
            }
            solutionNodes.addLast(node);
            return traceUp(node.getParent(), solutionNodes, marked);
        }

        return null;
    }

    public boolean traceDown(AndOrTreeNode node, LinkedList<AndOrTreeNode> solutionNodes, boolean[] marked)
    {
        if (!marked[node.getId()])
            return false;

        if (solutionNodes.contains(node))
            return true;

        if (node.isLeaf(node))
        {
            if (node.getSolvable())
            {
                solutionNodes.addLast(node);
                return true;
            }
            else
                return false;
        }

        if (node.getNodeType().equals(AndOrTreeNodeType.OR_NODE))
        {
            for (AndOrTreeNode child : node.getChildren())
            {
                if (traceDown(child, solutionNodes, marked))
                {
                    solutionNodes.addLast(node);
                    return true;
                }
            }
            return false;
        }

        if (node.getNodeType().equals(AndOrTreeNodeType.AND_NODE))
        {
            for (AndOrTreeNode child : node.getChildren())
            {
                if (!traceDown(child, solutionNodes, marked))
                {
                    return false;
                }
            }
            solutionNodes.addLast(node);
            return true;
        }

        return false;
    }

    @Override
    public String toString()
    {
        StringBuilder treeStructure = new StringBuilder();
        for (AndOrTreeNode node : treeNodes)
        {
            if (node != null)
            {
                treeStructure.append(node.toString());
                treeStructure.append("\n");
            }
        }
        if (root != null)
            treeStructure.append("Root: " + root.getId());
        return treeStructure.toString();
    }
}
