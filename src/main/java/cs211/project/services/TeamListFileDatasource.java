package cs211.project.services;

import cs211.project.models.Events;
import cs211.project.models.EventsList;
import cs211.project.models.Team;
import cs211.project.models.TeamList;

import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class TeamListFileDatasource implements Datasource<TeamList>{
    private String directoryName;
    private String fileName;

    public TeamListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted(){
        File file = new File(directoryName);
        if (!file.exists()){ file.mkdirs(); }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public TeamList readData() {
        TeamList teams = new TeamList();
        String filepath = directoryName + File.separator + fileName;
        File file = new File(filepath);

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        }   catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream, StandardCharsets.UTF_8
        );

        BufferedReader buffer = new BufferedReader(inputStreamReader);
        String line = "";
        try {
            while ((line = buffer.readLine()) != null){
                if (line.equals("")) continue;
                String[] data = line.split(",");
                String teamName = data[0].trim();
                Integer teamMaxSeat = Integer.parseInt(data[1]);
                Integer availableSeat = Integer.parseInt(data[2]);
                String registrationOpenDate = data[3].trim();
                String registrationCloseDate = data[4].trim();
                String teamInEvent = data[5];

                teams.addNewTeam(teamName, teamMaxSeat, availableSeat, registrationOpenDate, registrationCloseDate, teamInEvent);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return teams;
    }

    @Override
    public void writeData(TeamList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (Team team: data.getEvents()){
                String line = team.getTeamName() + ","
                        + team.getTeamMaxSeat() + ","
                        + team.getAvailableSeat() + ","
                        + team.getRegistrationOpenDate() + ","
                        + team.getRegistrationCloseDate() + ","
                        + team.getTeamInEvent();
                writer.append(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Cannot write " + filePath);
        }
    }
}
