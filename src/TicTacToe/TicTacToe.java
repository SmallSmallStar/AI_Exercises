package TicTacToe;

import java.util.Scanner;

/**
 * 游戏规定：玩家先走棋，输入的坐标从0开始。
 * 玩家走棋为“O”，电脑走棋为“X”。
 * 玩家MIN，电脑MAX。
 */
public class TicTacToe
{
    public static char[][] chessboard = new char[3][3];

    /**
     * 显示当前棋盘的样子。
     * @param chessboard 棋盘数组。
     */
    public static void showChessboard(char[][] chessboard)
    {
        System.out.println("-------------");
        for (int i = 0; i < chessboard.length; i++)
        {
            System.out.print("|");
            for (int j = 0; j < chessboard[0].length; j++)
            {
                System.out.print(" " + chessboard[i][j] + " |");
            }
            System.out.println();
            System.out.println("-------------");
        }
        System.out.println();
    }

    /**
     * O下棋即玩家下棋。
     * @param chessboard 棋盘数组。
     * @param x O棋子放置位置的横坐标。
     * @param y O棋子放置位置的纵坐标。
     */
    public static void putChessO(char[][] chessboard, int x, int y)
    {
        chessboard[x][y] = 'O';
    }

    /**
     * 显示：是玩家先走棋，还是电脑先走棋的菜单。
     */
    public static void showMenu()
    {
        System.out.println("Welcome to Tic-Tac-Toe");
        System.out.println("**********************");
        System.out.println(" You put piece first  ");
        System.out.println();
    }

    /**
     * 判断是不是赢了即三子连线。
     * @param chessboard 棋盘数组。
     * @return 如果赢了，返回true，否则返回false。
     */
    public static boolean isWin(char chessboard[][], char piece)
    {
        // 横向的棋子相同。
        if (chessboard[0][0] == chessboard[0][1] && chessboard[0][0] == piece)
            if (chessboard[0][1] == chessboard[0][2])
                return true;
        if (chessboard[1][0] == chessboard[1][1] && chessboard[1][0] == piece)
            if (chessboard[1][1] == chessboard[1][2])
                return true;
        if (chessboard[2][0] == chessboard[2][1] && chessboard[2][0] == piece)
            if (chessboard[2][1] == chessboard[2][2])
                return true;

        // 纵向的棋子相同。
        if (chessboard[0][0] == chessboard[1][0] && chessboard[0][0] == piece)
            if (chessboard[1][0] == chessboard[2][0])
                return true;
        if (chessboard[0][1] == chessboard[1][1] && chessboard[0][1] == piece)
            if (chessboard[1][1] == chessboard[2][1])
                return true;
        if (chessboard[0][2] == chessboard[1][2] && chessboard[0][2] == piece)
            if (chessboard[1][2] == chessboard[2][2])
                return true;

        // 斜对角的棋子相同。
        if (chessboard[0][0] == chessboard[1][1] && chessboard[0][0] == piece)
            if (chessboard[1][1] == chessboard[2][2])
                return true;
        if (chessboard[2][0] == chessboard[1][1] && chessboard[2][0]  == piece)
            if (chessboard[1][1] == chessboard[0][2])
                return true;

        return false;
    }

    /**
     * 判断游戏是否已经结束。
     * @param chessboard 棋盘数组。
     * @return 如果棋盘已经满了，则游戏结束，返回true，否则返回false。
     */
    public static boolean gameOver(char[][] chessboard)
    {
        for (int i = 0; i < chessboard.length; i++)
        {
            for (int j = 0; j < chessboard[i].length; j++)
            {
                if (chessboard[i][j] == 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * 填充棋盘，用于价值评估，在当前空棋盘上放入棋子。
     * @param chessboard 棋盘数组。
     * @param piece 放入的棋子。
     */
    private static void fillChessboard(char[][] chessboard, char piece)
    {
        for (int i = 0; i < chessboard.length; i++)
        {
            for (int j = 0; j < chessboard[i].length; j++)
            {
                if (chessboard[i][j] == 0)
                    chessboard[i][j] = piece;
            }
        }
    }

    /**
     * 数一数棋盘上三子连线的情况有几种，用于价值评估。
     * @param chessboard 棋盘数组。
     * @param piece 放入的棋子。
     * @return 返回计数的结果。
     */
    private static int countThreePiecesOnLine(char[][] chessboard, char piece)
    {
        int count = 0;

        // 数一数横向的三子一线的情况有几种。
        for (int i = 0; i < chessboard.length; i++)
        {
            if (chessboard[i][0] == piece && chessboard[i][0] == chessboard[i][1] && chessboard[i][1] == chessboard[i][2])
                count++;
        }

        // 再数一数纵向三子连线的情况有几种。
        for (int i = 0; i < chessboard.length; i++)
        {
            if (chessboard[0][i] == piece && chessboard[0][i] == chessboard[1][i] && chessboard[1][i] == chessboard[2][i])
                count++;
        }

        // 再数一数对角线三子连线的情况有几种。
        if (chessboard[0][0] == piece && chessboard[0][0] == chessboard[1][1] && chessboard[1][1] == chessboard[2][2])
            count++;
        if (chessboard[2][0] == piece && chessboard[2][0] == chessboard[1][1] && chessboard[1][1] == chessboard[0][2])
            count++;

        return count;
    }

    /**
     * 价值评估。
     * @param chessboard 棋盘数组。
     * @return 如果游戏已经结束，即有一方获胜：电脑赢，返回无穷大，设为100，如果玩家赢，返回无穷小，设为-100；如果游戏没有结束：返回电脑与玩家的三字一线的数量差值。
     */
    private static int evaluate(char[][] chessboard)
    {
        char[][] temp = new char[3][3];
        int countX;
        int countO;
        int value;

        // 判断是不是游戏已经结束，即有一方已经获胜。
        // MAX获胜（电脑获胜）。
        if (isWin(chessboard, 'X'))
            return 100;
        // MIN获胜（玩家获胜）。
        if (isWin(chessboard, 'O'))
            return -100;

        // 所有空格上放上MAX棋子（电脑棋子）。
        copy(chessboard, temp);
        fillChessboard(temp, 'X');
        // 计算电脑棋盘的评估价值函数。
        countX = countThreePiecesOnLine(temp, 'X');

        // 所有空格上放上MIN棋子（玩家棋子）。
        copy(chessboard, temp);
        fillChessboard(temp, 'O');
        // 计算玩家棋盘的评估价值函数。
        countO = countThreePiecesOnLine(temp,'O');

        // 返回MAX和MIN的差值。
        value = countX - countO;
        return value;
    }

    /**
     * 复制当前的棋盘数组，用于电脑向前看两部下棋时的预测走棋。
     * @param chessboard 当前的棋盘数组。
     * @param copyChessboard 复制完的棋盘数组。
     * @return 返回复制完的棋盘数组。
     */
    public static char[][] copy(char[][] chessboard, char[][] copyChessboard)
    {
        for (int i = 0; i < chessboard.length; i++)
        {
            for (int j = 0; j < chessboard[i].length; j++)
            {
                copyChessboard[i][j] = chessboard[i][j];
            }
        }
        return copyChessboard;
    }

    /**
     * 电脑向前看两部下棋:首先看一看自己可以在哪里下棋，然后预测对手会在哪里下棋；先选出对手下棋情况中的评估价值最小的，再选出电脑下棋评估价值最大的。
     * @param chessboard 棋盘数组。
     */
    public static void computerPlay(char[][] chessboard)
    {
        int value;

        int minValue;
        int putX = 0;
        int putY = 0;

        int maxValue = Integer.MIN_VALUE;

        // 找一找当下棋盘中那些位置是未放置棋子的，放入电脑的棋子。
        for (int i = 0; i < chessboard.length; i++)
        {
            for (int j = 0; j < chessboard[i].length; j++)
            {
                if (chessboard[i][j] == 0)
                {
                    minValue = Integer.MAX_VALUE;

                    char[][] computerChess = new char[3][3];
                    computerChess = copy(chessboard, computerChess);
                    computerChess[i][j] = 'X';

                    // 预测一下对手会将棋子放在哪里。
                    for (int x = 0; x < computerChess.length; x++)
                    {
                        for (int y = 0; y < computerChess[x].length; y++)
                        {
                            if (computerChess[x][y] == 0)
                            {
                                char[][] playerChess = new char[3][3];
                                playerChess = copy(computerChess, playerChess);
                                playerChess[x][y] = 'O';

                                // 算一算现在的评估价值。
                                value = evaluate(playerChess);

                                // 找出当前评估价值最小的，即此时对手下棋对对手最有利。
                                if (value < minValue)
                                {
                                    minValue = value;
                                }
                            }
                        }
                    }

                    // 选出评估价值最大的位置。
                    if (minValue > maxValue)
                    {
                        maxValue = minValue;
                        putX = i;
                        putY = j;
                    }
                }
            }
        }

        // 电脑将棋子放置在最优位置上。
        chessboard[putX][putY] = 'X';
    }

    public static void main(String[] args)
    {
        showMenu();
        Scanner input = new Scanner(System.in);
        for (;;)
        {
            // 玩家下棋。
            int x;
            int y;
            for (;;)
            {
                System.out.print("Player input the coordinate (start with 0): ");
                x = input.nextInt();
                y = input.nextInt();
                if (chessboard[x][y] == 0)
                {
                    putChessO(chessboard, x, y);
                    break;
                }
                else
                    System.out.println("Cannot put piece here, please input again!");
            }
            showChessboard(chessboard);
            if (isWin(chessboard, 'O'))
            {
                System.out.println("You win!");
                break;
            }
            if (gameOver(chessboard))
            {
                System.out.println("Draw!");
                break;
            }

            // 电脑下棋。
            System.out.println("Computer put piece: ");
            computerPlay(chessboard);
            showChessboard(chessboard);
            if (isWin(chessboard, 'X'))
            {
                System.out.println("Computer wins!");
                break;
            }
            if (gameOver(chessboard))
            {
                System.out.println("Draw!");
                break;
            }
        }
    }
}
