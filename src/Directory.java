import java.util.ArrayList;

public class Directory extends FileEntity{
    private final Directory parentDirectory;
    private final ArrayList<FileEntity> fileEntities;

    public Directory(Directory parentDir, String name){
        super(name);
        this.parentDirectory = parentDir;
        fileEntities = new ArrayList<FileEntity>();
    }

    public Directory getParentDirectory(){
        return this.parentDirectory;
    }

    public int getItemCount(){
        return this.fileEntities.size();
    }

    public FileEntity getFileEntity(int idx){
        return this.fileEntities.get(idx);
    }

    public boolean isEntityExist(String entityName){
        for(FileEntity fileEntity : fileEntities){
            if(fileEntity.name.equals(entityName)){
                return true;
            }
        }
        return false;
    }

    public void addFileEntity(FileEntity fileEntity){
        this.fileEntities.add(fileEntity);
    }


    public String search(String name){
        StringBuilder sb = new StringBuilder();
        String path = getPath();
        for(FileEntity fileEntity : fileEntities){
            if(fileEntity instanceof File){
                if(fileEntity.name.contains(name)){
                    sb.append(path);
                    sb.append(fileEntity.name);
                    sb.append(" (file)");
                    sb.append("\n");
                }
            }
            else if(fileEntity instanceof Directory){
                if(fileEntity.name.contains(name)){
                    if(parentDirectory==null){
                        sb.append("root/");
                    }
                    else{
                        sb.append(parentDirectory.getPath());
                    }
                    sb.append(name);
                    sb.append(" (directory)");
                    sb.append("\n");
                }
                Directory dir = (Directory) fileEntity;
                sb.append(dir.search(name));
            }
        }
        return sb.toString();
    }

    public String getPath(){
        if(parentDirectory!=null){
            return parentDirectory.getPath() + name + "/";
        }
        else{
            return name + "/";
        }
    }

//    public ArrayList<FileEntity> getFileEntities(){
//        return this.fileEntities;
//    }
}
