import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

public class AndOrTree
{
    // 解结点链表。
    private LinkedList<AndOrTreeNode> solutionNodes;

    // 与或树的根结点。
    private AndOrTreeNode root;

    // 与或树的结点数组。
    private AndOrTreeNode[] treeNodes;

    // 解树。
    private AndOrTree solutionTree;

    // 构造一个和原本与或树相同根结点的与或树。
    public AndOrTree(AndOrTreeNode root)
    {
        this.root = root;
    }

    /**
     * 与或树的构造方法。
     * @param filePath 与或树结点信息保存在的文件的路径。
     * @throws Exception 文件找不到的时候抛出找不到此文件异常。
     */
    public AndOrTree(String filePath) throws Exception
    {
        // 用Scanner来读取文件。
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        // 文件的第一行规定了与或树的结点个数。
        int nodeNumber = Integer.parseInt(scanner.nextLine());
        treeNodes = new AndOrTreeNode[nodeNumber];

        for (int i = 0; i < nodeNumber; i++)
        {
            // 文件第二行开始每一行都是一个结点的信息。格式为：【结点编号】【结点类型】【结点是否可解】【子结点】。中间用空格分开。
            String treeNodeText = scanner.nextLine();
            // 读取出来用空格分割开的各个信息全部化为小写并存储在一个数组里。line[0] = id，line[1] = nodeType，line[2] = solvable，剩下的全是子结点。
            String[] line = treeNodeText.toLowerCase().split(" ");

            // 结点编号。
            int id = Integer.parseInt(line[0]);

            // 结点类型只可能有两种：与结点，或结点。
            AndOrTreeNodeType nodeType;

            // 或结点。
            if (line[1].equals("or"))
                nodeType = AndOrTreeNodeType.OR_NODE;

            // 与结点。
            else if (line[1].equals("and"))
                nodeType = AndOrTreeNodeType.AND_NODE;

            // 未知节点抛出异常。
            else
                throw new Exception("Invalid data format of 'AndOrTreeNodeType' in file " + filePath + ", line " + (i + 1));

            // 是否可解只有三种：可解，不可解，未知。
            Boolean solvable;

            // 可解结点。
            if (line[2].equals("true"))
                solvable = true;

            // 不可解结点。
            else if (line[2].equals("false"))
                solvable = false;

            // 未知是否可解结点。
            else if (line[2].equals("unknown"))
                solvable = null;

            // 其他抛出异常。
            else
                throw new Exception("Invalid data format of 'AndOrTreeNodeType' in file " + filePath + ", line " + (i + 1));

            // 创建一个新的结点。
            AndOrTreeNode node = new AndOrTreeNode(id,nodeType,solvable);

            // 将结点存入树结点数组。
            treeNodes[node.getId()] = node;

            // 将结点的子结点和他相连。
            for (int j = 3; j < line.length; j++)
            {
                int children = Integer.parseInt(line[j]);
                treeNodes[children].setParent(node);
                node.addChild(treeNodes[children]);
            }
        }

        // 判断结点是不是根结点。
        for (AndOrTreeNode node : treeNodes)
        {
            // 如果结点的父结点是空的，则此节点是根结点。
            if (node.getParent() == null)
                root = node;
        }
    }

    /**
     * 与或树深度优先搜索解树方法。
     * @return 返回解树。
     */
    public AndOrTree DepthFirstSearch()
    {
        // 将解树置为空。
        solutionTree = null;

        // 如果根结点就是叶子结点，即一棵树就一个结点。
        if (root.isLeaf(root))
        {
            // 如果这个节点不可解，解树为空，直接返回null。
            if (!root.getSolvable())
                return null;
        }

        // 给每个结点进行访问标记。
        boolean[] marked = new boolean[treeNodes.length];

        // 初始化全部为未访问。
        for (int i = 0; i < marked.length; i++)
            marked[i] = false;

        // 整个树是否可解，初始化为不可解。
        Bool solved = new Bool(false);

        // 调用深度优先方法。
        DepthFirstSearch(root, marked, solved);

        // 返回解树。
        return solutionTree;
    }

    /**
     * 带参数的深度优先搜索解树。
     * @param node 结点。
     * @param marked 结点是否被标记。
     * @param solved 树是否已经可解。
     */
    private void DepthFirstSearch(AndOrTreeNode node, boolean[] marked, Bool solved)
    {
        // 如果树已经可解，不再搜寻，直接返回。
        if (solved.getValue())
            return;

        // 将此结点标记为已访问。
        marked[node.getId()] = true;

        // 如果这个节点不是叶子结点，继续向下搜索。
        if (!node.isLeaf(node))
        {
            // 对于这个结点的所有孩子都深度优先搜索一下。
            for (AndOrTreeNode child : node.getChildren())
            {
                DepthFirstSearch(child, marked, solved);
            }
        }

        // 如果是叶子结点且这个结点是可解的，向上找解树。
        else if (node.getSolvable())
        {
            // 初始化一个解结点链表。
            solutionNodes = new LinkedList<>();

            // 从此叶子结点向上找。
            solutionTree = traceUp(node, solutionNodes, marked);

            // 如果解结点链表里面包含了根结点，整个树就已经可解了。
            if (solutionNodes.contains(root))
                solved.setValue(true);
        }
    }

    /**
     * 与或树广度优先搜索解树方法。
     * @return 返回解树。
     */
    public AndOrTree BreadthFirstSearch()
    {
        // 将解树置为空。
        solutionTree = null;

        // 如果根结点就是叶子结点，即一棵树就一个结点。
        if (root.isLeaf(root))
        {
            // 如果这个节点不可解，解树为空，直接返回null。
            if (!root.getSolvable())
                return null;
        }

        // 给每个结点进行访问标记。
        boolean[] marked = new boolean[treeNodes.length];

        // 初始化全部为未访问。
        for (int i = 0; i < marked.length; i++)
            marked[i] = false;

        // 层次访问与或树的结点，模拟队列的先进先出原则即可做到。
        LinkedList<AndOrTreeNode> bfsQueue = new LinkedList<>();

        // 将根结点标记为访问过。
        marked[root.getId()] = true;

        // 根结点进队列。
        bfsQueue.addLast(root);

        // 将队列里的结点出队。
        while (bfsQueue.size() != 0)
        {
            // 出头结点。
            AndOrTreeNode node = bfsQueue.removeFirst();

            // 如果不是叶子结点，同层结点即下层的继续入队。
            if (!node.isLeaf(node))
            {
                for (AndOrTreeNode child : node.getChildren())
                {
                    // 子结点未被标记过。
                    if (!marked[child.getId()])
                    {
                        // 标记此子结点。
                        marked[child.getId()] = true;

                        // 子结点进队。
                        bfsQueue.addLast(child);
                    }
                }
            }

            // 此结点是可解的叶子结点。
            else if (node.getSolvable())
            {
                // 初始化一个空的解结点链表。
                solutionNodes = new LinkedList<>();

                // 从此解结点向上搜寻解树。
                solutionTree = traceUp(node, solutionNodes, marked);

                // 如果解结点链表已经包含根结点，直接返回解树，不再搜寻。
                if (solutionNodes.contains(root))
                    return solutionTree;
            }
        }

        // 返回解树。
        return solutionTree;
    }

    /**
     * 与或树生成解树的方法。
     * @param solutionNodes 解结点链表。
     * @return 返回解树。
     */
    private AndOrTree buildSolutionTree(LinkedList<AndOrTreeNode> solutionNodes)
    {
        // 创建一个和原本树相同根结点的新树。
        solutionTree = new AndOrTree(this.root.copy());

        // 创建解树的结点。
        solutionTree.treeNodes = new AndOrTreeNode[this.treeNodes.length];

        // 把解结点链表的结点复制给解树。
        for (AndOrTreeNode node : solutionNodes)
            solutionTree.treeNodes[node.getId()] = node.copy();

        // 为每个结点找自己的子结点并与自己相连。
        for (AndOrTreeNode node : solutionNodes)
        {
            if (node.getParent() != null)
            {
                // 设置父结点。
                solutionTree.treeNodes[node.getId()].setParent(solutionTree.treeNodes[node.getParent().getId()]);

                // 设置子结点。
                solutionTree.treeNodes[node.getParent().getId()].addChild(solutionTree.treeNodes[node.getId()]);
            }
        }

        // 返回解树。
        return solutionTree;
    }

    /**
     * 在某个结点向上搜寻解树。
     * @param node 此结点。
     * @param solutionNodes 解结点链表。
     * @param marked 此结点是否被标记过。
     * @return 返回解树。
     */
    private AndOrTree traceUp(AndOrTreeNode node, LinkedList<AndOrTreeNode> solutionNodes, boolean[] marked)
    {
        // 如果此结点是根结点。
        if (node == root)
        {
            // 将根结点加入解结点链表。
            solutionNodes.addLast(node);

            // 返回解树。
            return buildSolutionTree(solutionNodes);
        }

        // 如果是叶子结点。
        if (node.isLeaf(node))
        {
            // 如果叶子结点可解。
            if (node.getSolvable())
            {
                // 将此叶子结点加入解结点链表。
                solutionNodes.addLast(node);

                // 再向上找，即将他的父结点作为参数传入方法。
                return traceUp(node.getParent(), solutionNodes, marked);
            }

            // 叶子结点不可解。
            else
                // 返回空。
                return null;
        }

        // 如果是或结点。
        if (node.getNodeType().equals(AndOrTreeNodeType.OR_NODE))
        {
            // 将此叶子结点加入解结点链表。
            solutionNodes.addLast(node);

            // 再向上找，即将他的父结点作为参数传入方法。
            return traceUp(node.getParent(), solutionNodes, marked);
        }

        // 如果是与结点。
        if (node.getNodeType().equals(AndOrTreeNodeType.AND_NODE))
        {
            // 每个孩子结点都要看。
            for (AndOrTreeNode child : node.getChildren())
            {
                // 如果没有向下搜索，即这个点不可解，返回空。
                if (!traceDown(child, solutionNodes, marked))
                    return null;
            }

            // 否则将此结点加入解结点链表。
            solutionNodes.addLast(node);

            // 再向上找，即将他的父结点作为参数传入方法。
            return traceUp(node.getParent(), solutionNodes, marked);
        }

        // 否则返回空。
        return null;
    }

    /**
     * 在某个结点向上搜寻解树。
     * @param node 此结点。
     * @param solutionNodes 解结点链表。
     * @param marked 结点是否被标记过。
     * @return 如果向下搜索成功，返回true，否则返回false。
     */
    public boolean traceDown(AndOrTreeNode node, LinkedList<AndOrTreeNode> solutionNodes, boolean[] marked)
    {
        // 如果结点没有访问过，直接返回false。
        if (!marked[node.getId()])
            return false;

        // 如果解结点链表里包含此结点，则返回true。
        if (solutionNodes.contains(node))
            return true;

        // 如果是叶子结点。
        if (node.isLeaf(node))
        {
            // 叶子结点可解。
            if (node.getSolvable())
            {
                // 解结点链表加入此结点，并返回true。
                solutionNodes.addLast(node);
                return true;
            }

            // 不可解，返回false。
            else
                return false;
        }

        // 是或结点。
        if (node.getNodeType().equals(AndOrTreeNodeType.OR_NODE))
        {
            // 对于他的每个孩子都要向下看。
            for (AndOrTreeNode child : node.getChildren())
            {
                // 如果子结点向下搜索为true。
                if (traceDown(child, solutionNodes, marked))
                {
                    // 将此结点加入解结点链表并返回true。
                    solutionNodes.addLast(node);
                    return true;
                }
            }

            // 否则返回false。
            return false;
        }

        // 是与结点。
        if (node.getNodeType().equals(AndOrTreeNodeType.AND_NODE))
        {
            // 对于他的每个孩子都要向下看。
            for (AndOrTreeNode child : node.getChildren())
            {
                // 只要一个子结点向下搜索返回的是false，这个结点就不可解，返回false。
                if (!traceDown(child, solutionNodes, marked))
                    return false;
            }

            // 否则将此结点加入解结点链表，并返回true。
            solutionNodes.addLast(node);
            return true;
        }

        // 否则返回false。
        return false;
    }

    /**
     * 将树显示出来。
     * @return 返回树的信息。
     */
    @Override
    public String toString()
    {
        StringBuilder treeStructure = new StringBuilder();

        // 对于树的每一个结点。
        for (AndOrTreeNode node : treeNodes)
        {
            // 结点不为空。
            if (node != null)
            {
                // 显示结点信息。
                treeStructure.append(node.toString());
                treeStructure.append("\n");
            }
        }

        // 如果根结点不为空，显示根结点的信息。
        if (root != null)
            treeStructure.append("Root: " + root.getId());

        // 返回树的信息。
        return treeStructure.toString();
    }
}
