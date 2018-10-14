package AndOrTree;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // 初始化一个与或树。
        AndOrTree tree = new AndOrTree("src/AndOrTree/AndOrTreeText.txt");

        System.out.println("DFS:");
        // 深度优先搜索解树。
        AndOrTree DFSSolutionTree = tree.DepthFirstSearch();
        // 打印解树。
        System.out.println(DFSSolutionTree);

        // 空行。
        System.out.println();

        System.out.println("BFS:");
        // 广度优先搜索解树。
        AndOrTree BFSSolutionTree = tree.BreadthFirstSearch();
        // 打印解树。
        System.out.println(BFSSolutionTree);
    }
}
