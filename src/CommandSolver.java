import sun.awt.OSInfo;

import java.util.Arrays;

public class CommandSolver {
    public interface Execute{
        Directory action(Directory directory, String[] args);
    }

    public enum Command implements Execute{
        LS("ls",0){
            @Override
            public Directory action(Directory directory, String[] args){
                Directory newDir = directory;
                boolean isInvalidPath = false;
                int i=0;
                if(args!=null){
                    for(i=0; i<args.length && !isInvalidPath; i++){
                        if(args[i].equals("..")){
                            if(newDir.getParentDirectory()!=null){
                                newDir = newDir.getParentDirectory();
                            }
                            else {
                                isInvalidPath = true;
                            }
                        }
                        else{
                            isInvalidPath = true;
                            for(int j=0; j<newDir.getItemCount() && isInvalidPath; j++){
                                FileEntity fe = newDir.getFileEntity(j);
                                if(fe.name.equals(args[i]) &&  fe instanceof Directory){
                                    newDir = (Directory) fe;
                                    isInvalidPath = false;
                                }
                            }

                        }
                    }
                }
                if(isInvalidPath){
                    System.out.println("There's no folder called \"" + args[i] + "\" under \"" + newDir.getPath() + "\" !");
                }
                else{
                    int count = newDir.getItemCount();
                    int fileCnt = 0;
                    int directoryCnt = 0;
                    FileEntity curFileEntity;
                    for(int j=0; j<count; j++){
                        curFileEntity = newDir.getFileEntity(j);
                        System.out.print(curFileEntity.name);
                        if(curFileEntity instanceof File){
                            System.out.println(" (file)");
                            fileCnt++;
                        }
                        else if(curFileEntity instanceof Directory){
                            System.out.println(" (directory)");
                            directoryCnt++;
                        }
                    }
                    System.out.println("Total: " + directoryCnt + " folders, " + fileCnt + " files.");
                }
//                if (args == null) {
//                    int count = directory.getItemCount();
//                    int fileCnt = 0;
//                    int directoryCnt = 0;
//                    FileEntity curFileEntity;
//                    for(int i=0; i<count; i++){
//                        curFileEntity = directory.getFileEntity(i);
//                        System.out.print(curFileEntity.name);
//                        if(curFileEntity instanceof File){
//                            System.out.println(" (file)");
//                            fileCnt++;
//                        }
//                        else if(curFileEntity instanceof Directory){
//                            System.out.println(" (directory)");
//                            directoryCnt++;
//                        }
//                    }
//                    System.out.println("Total: " + directoryCnt + " folders, " + fileCnt + " files.");
//                }
//                else if(args.length>0){
//                    boolean dirExists = false;
//                    for(int i=0; i<directory.getItemCount(); i++){
//                        FileEntity fe = directory.getFileEntity(i);
//                        if(fe instanceof Directory && fe.name.equals(args[0])){
//                            dirExists = true;
//                            String[] newArgs = null;
//                            if(args.length>1){
//                                newArgs = new String[args.length-1];
//                                System.arraycopy(args, 1, newArgs,0,args.length-1);
//                            }
//                            action((Directory) fe,newArgs);
//                        }
//                    }
//                    if(!dirExists){
//                        System.out.println("There's no folder called \"" + args[0] + "\" under \"" + directory.getPath() + "\" !");
//                    }
//                }
                return directory;
            }
        },
        CD("cd",1){
            @Override
            public Directory action(Directory directory, String[] args){
                System.out.println(Arrays.toString(args));
                boolean isInvalidPath = false;
                Directory newDir = directory;
                int i;
                for(i=0; i< args.length && !isInvalidPath; i++){
                    if(args[i].equals("..")){
                        if(newDir.getParentDirectory()!=null){
                            newDir = newDir.getParentDirectory();
                        }
                        else {
                            isInvalidPath = true;
                        }
                    }
                    else{
                        isInvalidPath = true;
                        for(int j=0; j<newDir.getItemCount() && isInvalidPath; j++){
                            FileEntity fe = newDir.getFileEntity(j);
                            if(fe.name.equals(args[i]) &&  fe instanceof Directory){
                                newDir = (Directory) fe;
                                isInvalidPath = false;
                            }
                        }

                    }
                }
                if(isInvalidPath){
                    System.out.println("There's no folder called \"" + args[i] + "\" under \"" + newDir.getPath() + "\" !");
                }
                return (isInvalidPath) ? directory : newDir;
            }
        },
        TOUCH("touch",1){
            @Override
            public Directory action(Directory directory, String[] args) {
                if(args.length==1){
                    if(directory.isEntityExist(args[0])){
                        System.out.println("File already exists!");
                    }
                    else{
                        directory.addFileEntity(new File(args[0]));
                    }
                }
                else{
                    for(int i=0; i<directory.getItemCount(); i++){
                        FileEntity fileEntity = directory.getFileEntity(i);
                        if(fileEntity instanceof Directory && fileEntity.name.equals(args[0])){
                            String[] newArgs = new String[args.length-1];
                            System.arraycopy(args, 1, newArgs, 0, args.length-1);
                            break;
                        }
                    }
                }
                return directory;
            }
        },
        MKDIR("mkdir",1) {
            @Override
            public Directory action(Directory directory, String[] args) {
                System.out.println(Arrays.toString(args));
                boolean dirAlreadyExists = false;
                for(int i=0; i<directory.getItemCount(); i++){
                    FileEntity fileEntity = directory.getFileEntity(i);
                    if(fileEntity instanceof Directory && fileEntity.name.equals(args[0])){
                        dirAlreadyExists = true;
                        break;
                    }
                }

                if(dirAlreadyExists) {
                    System.out.println("Directory already exists!");
                }
                else{
                    directory.addFileEntity(new Directory(directory, args[0]));
                }
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
        String command = inputs[0];
        String[] args = null;
        if(inputs.length>1){
            args = inputs[1].split("/");
        }
        System.out.println(Arrays.toString(args));
        System.out.println("---------------");
        for(Command cmd : Command.values()){
            if(inputs[0].equals(cmd.value)){// && inputs.length-1==cmd.numOfArgs){
//                if(cmd.numOfArgs>0){
//                   System.arraycopy(inputs, 1, args, 0, inputs.length-1);
//                }
                return new Cmd(cmd,args);
            }
        }
        return null;
    }
}
