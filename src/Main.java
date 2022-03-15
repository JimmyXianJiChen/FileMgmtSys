import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        CommandSolver.Cmd cmd = CommandSolver.input("cd adfa/dsfasda/fsd.txt");
//        for(String str : cmd.getArgs()){
//            System.out.println(str);
//        }
//        cmd.getCommand().action();

        Directory currentDir = new Directory(null, "root");
        Scanner sc = new Scanner(System.in);
        do{
            System.out.print(currentDir.getPath() + ">");
            String input = sc.nextLine();
            CommandSolver.Cmd cmd = CommandSolver.input(input);
            currentDir = cmd.execute(currentDir);
        }
        while(true);
    }
}
