package cs211.project.services;

import cs211.project.models.EventUser;
import cs211.project.models.EventUserList;
import cs211.project.models.Events;
import cs211.project.models.EventsList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventUserFileDatasource implements Datasource<EventUserList> {
    private String directoryName;
    private String fileName;

    public EventUserFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }
    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public EventUserList readData() {
        EventUserList eventUserList = new EventUserList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        // เตรียม object ที่ใช้ในการอ่านไฟล์
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
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
                String event = data[0].trim();
                String user = data[1].trim();

                // เพิ่มข้อมูลลงใน list
                eventUserList.addEventUser(event, user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return eventUserList;
    }

    @Override
    public void writeData(EventUserList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            // Create a map to store users for each event
            Map<String, List<String>> eventUserMap = new HashMap<>();

            for (EventUser eventUser : data.getEventUser()) {
                String event = eventUser.getEvent();
                String user = eventUser.getUser();

                // Check if the event is already in the map
                if (eventUserMap.containsKey(event)) {
                    // If it is, add the user to the list of users for that event
                    eventUserMap.get(event).add(user);
                } else {
                    // If it's not, create a new list and add the user
                    List<String> userList = new ArrayList<>();
                    userList.add(user);
                    eventUserMap.put(event, userList);
                }
            }

            // Write the event-user data to the file
            for (Map.Entry<String, List<String>> entry : eventUserMap.entrySet()) {
                String event = entry.getKey();
                List<String> userList = entry.getValue();

                // Convert the list of users to a single comma-separated string
                String usersString = String.join(",", userList);

                // Write the event and users to the file
                writer.write(event + "," + usersString);
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Cannot write " + filePath);
        }
    }


}

