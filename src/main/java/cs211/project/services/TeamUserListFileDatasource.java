package cs211.project.services;

import cs211.project.models.EventUser;
import cs211.project.models.TeamUser;
import cs211.project.models.TeamUserList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamUserListFileDatasource implements Datasource<TeamUserList>{
    private String directoryName;
    private String fileName;
    public TeamUserListFileDatasource(String directoryName, String fileName){
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }
    private void checkFileIsExisted(){
        File file = new File(directoryName);
        if (!file.exists()){
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public TeamUserList readData() {
        TeamUserList teamUserList = new TeamUserList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream, StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);
        String line = "";
        try {
            // ใช้ while loop เพื่ออ่านข้อมูลรอบละบรรทัด
            while ((line = buffer.readLine()) != null) {
                // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                if (line.equals("")) continue;
                // แยกสตริงด้วย ,
                String[] data = line.split(",");
                // อ่านข้อมูลตาม index แล้วจัดการประเภทของข้อมูลให้เหมาะสม
                String eventName = data[0].trim();
                String teamName = data[1].trim();
                String username = data[2].trim();

                // เพิ่มข้อมูลลงใน list
                teamUserList.addNewTeamUser(eventName, teamName, username);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  teamUserList;
    }

    @Override
    public void writeData(TeamUserList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (TeamUser teamUser : data.getTeamUser()) {
                String eventName = teamUser.getEventName();
                String teamName = teamUser.getTeamName();
                String username = teamUser.getUserName();

                // Write the event, team, and username to the file
                writer.write(eventName + "," + teamName + "," + username);
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Cannot write " + filePath);
        }
    }

}
