import java.util.Arrays;

public class CommandSolver {
    public interface Execute{
        Directory action(Directory directory, String[] args);
    }

    public enum Command implements Execute{
        LS("ls",0){
            @Override
            public Directory action(Directory directory, String[] args){
                int count = directory.getItemCount();
                int fileCnt = 0;
                int directoryCnt = 0;
                FileEntity curFileEntity;
                for(int i=0; i<count; i++){
                    curFileEntity = directory.getFileEntity(i);
                    System.out.println(curFileEntity.name);
                    if(curFileEntity instanceof File){
                        fileCnt++;
                    }
                    else if(curFileEntity instanceof Directory){
                        directoryCnt++;
                    }
                }
                System.out.println("Total: " + directoryCnt + " folders, " + fileCnt + " files.");
                return directory;
            }
        },
        CD("cd",1){
            @Override
            public Directory action(Directory directory, String[] args){
                if(directory.name.equals("root") && directory.getPath().equals("root/")){
                    return directory;
                }
                else if(args[0].equals("..")){
                    return directory.getParentDirectory();
                }
                else{
                    int count = directory.getItemCount();
                    for(int i=0; i<count; i++){
                        FileEntity fe = directory.getFileEntity(i);
                        if(fe.name.equals(args[0]) &&  fe instanceof Directory){
                            return (Directory) fe;
                        }
                    }
                    return directory;
                }
            }
        },
        TOUCH("touch",1){
            @Override
            public Directory action(Directory directory, String[] args) {
                if(directory.isEntityExist(args[0])){
                    System.out.println("File already exists!");
                }
                else{
                    directory.addFileEntity(new File(args[0]));
                }
                return directory;
            }
        },
        MKDIR("mkdir",1) {
            @Override
            public Directory action(Directory directory, String[] args) {
                directory.addFileEntity(new Directory(directory, args[0]));
                return directory;
            }
        },
        SEARCH("search",1) {
            @Override
            public Directory action(Directory directory, String[] args) {
                String ans = directory.search(args[0]);
                System.out.println(ans.substring(0,ans.length()-1));
                return directory;
            }
        };

        private final String value;
        private final int numOfArgs;
        Command (String value, int numOfArgs){
            this.value = value;
            this.numOfArgs = numOfArgs;
        }
    }

    public static class Cmd{
        private final Command command;
        private final String[] args;

        private Cmd(Command cmd, String[] args){
            this.command = cmd;
            this.args = args;
        }

        public Command getCommand() {
            return command;
        }

        public String[] getArgs() {
            return args;
        }

        public Directory execute(Directory currentDir){
            return command.action(currentDir,args);
        }

        @Override
        public String toString(){
            return this.command + Arrays.toString(args);
        }
    }

    public static Cmd input(String str){
        String[] inputs = str.split(" ");

        for(Command cmd : Command.values()){
            if(inputs[0].equals(cmd.value) && inputs.length-1==cmd.numOfArgs){
                String[] args = new String[cmd.numOfArgs];
                if(cmd.numOfArgs>0){
                   System.arraycopy(inputs, 1, args, 0, inputs.length-1);
                }
                return new Cmd(cmd,args);
            }
        }
        return null;
    }
}
