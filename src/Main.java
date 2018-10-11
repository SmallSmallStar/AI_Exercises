public class Main
{
    public static void main(String[] args) throws Exception
    {
        AndOrTree tree = new AndOrTree("AndOrTreeText.txt");

        System.out.println("DFS:");
        AndOrTree DFSSolutionTree = tree.DepthFirstSearch();
        System.out.println(DFSSolutionTree);

        System.out.println();

        System.out.println("BFS:");
        AndOrTree BFSSolutionTree = tree.DepthFirstSearch();
        System.out.println(BFSSolutionTree);

    }
}
