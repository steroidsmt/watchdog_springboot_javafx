package com.example.demo.controller;


import com.example.demo.model.WatchDog;
import com.example.demo.service.StartOrStop;
import com.example.demo.service.WatchDogService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@FxmlView("main-stage.fxml")
public class MyController {

    @FXML
    private TextField txtDirectory;
    @FXML
    private TextArea txaDirectory;
    @FXML
    private TextArea txaLog;
    @FXML
    private Button btnStart;

    private StartOrStop startwatchdog;
    private boolean startOrStop = true;
    private Thread fileWatcher;
    private WatchService watchService;
    WatchKey key = null;

    String directoryPath = "";
    String textLog = "";
    String textStart = "Please click [Start] button to start application! \n";
    String textStart2 = "Please choose Folder to scan \n";

    @Autowired
    private WatchDogService watchDogService;

    @Autowired
    public MyController(StartOrStop startOrStop) {
        this.startwatchdog = startOrStop;
    }
    public void loadstartOrStop(javafx.event.ActionEvent actionEvent) {
        if (startOrStop) {
            startButtonActionPerformed();
            startOrStop = !startOrStop;
            btnStart.setText(startwatchdog.setStopButonText());
        } else {
            stopButtonActionPerformed();
            startOrStop = !startOrStop;
            btnStart.setText(startwatchdog.setStartButonText());
        }
    }

    private void startButtonActionPerformed() {
        //Start
        fileWatcher = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    getConnection();
                    showLog();
                } catch (IOException | SQLException e) {
                    System.out.println(e);
                }
            }
        });
        fileWatcher.start();
        //End
    }
    private void stopButtonActionPerformed() {
        try {
//            watchService.close();
            fileWatcher.interrupt();
//            closeDb();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showLog() throws IOException, SQLException {

        WatchDog watchDog = new WatchDog();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//      SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
//      SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm:ss");

        watchService = FileSystems.getDefault().newWatchService();

        Path dir = Paths.get(directoryPath);
        System.out.println("Watch Service registered for dir: " + dir.getFileName() + "\n");
        if (directoryPath == "") {
            textLog = "No Folder Selected \n";
        } else {
            textLog = "WatchDog Program is running... \n";
        }
        txaLog.setText(textLog);
        txaDirectory.setText(directoryPath);

        dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        while (true) {
            try {
                System.out.println("Waiting for key to be signalled...");
                key = watchService.take();
            } catch (Exception ex) {
                textLog += "Stopped! \n";
                txaLog.setText(textLog);
                System.out.println("Stopped!");
                return;
            }
            for (WatchEvent<?> event : key.pollEvents()) {
                // Retrieve the type of event by using the kind() method.
                WatchEvent.Kind<?> kind = event.kind();
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path fileName = ev.context();
                // get current time
                String date = LocalDateTime.now().format(formatter1);
                String time = LocalDateTime.now().format(formatter2);
//              String date = dateFormatter.format(new Date());
//              String time = hourFormatter.format(new Date());
                String filename1 = String.valueOf(fileName.getFileName());
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    textLog += "[Time: " + date + " " + time + "] " + fileName.getFileName() + " created \n";
                    txaLog.setText(textLog);
                    System.out.println(textLog);
                    givenWritingStringToFile_whenUsingFileOutputStream_thenCorrect(textLog);
//                    String a = "INSERT INTO watchdog " +
//                            "(Path,Date,Time,FileName,Action) " +
//                            "VALUES ('" + directoryPath + "','" + date + "','" + time + "','" + fileName.getFileName() + "','created') ";
//                    writeDb(a);
                    watchDogService.createWatchDog(directoryPath, date, time, filename1, "created");
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    textLog += "[Time: " + date + " " + time + "] " + fileName.getFileName() + " modified \n";
                    txaLog.setText(textLog);
                    System.out.println(textLog);
                    givenWritingStringToFile_whenUsingFileOutputStream_thenCorrect(textLog);
//                    String a = "INSERT INTO watchdog " +
//                            "(Path,Date,Time,FileName,Action) " +
//                            "VALUES ('" + directoryPath + "','" + date + "','" + time + "','" + fileName.getFileName() + "','modified') ";
//                    writeDb(a);
                    watchDogService.createWatchDog(directoryPath, date, time, filename1, "modified");
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    textLog += "[Time: " + date + " " + time + "] " + fileName.getFileName() + " deleted \n";
                    txaLog.setText(textLog);
                    System.out.println(textLog);
                    givenWritingStringToFile_whenUsingFileOutputStream_thenCorrect(textLog);
//                    String a = "INSERT INTO watchdog " +
//                            "(Path,Date,Time,FileName,Action) " +
//                            "VALUES ('" + directoryPath + "','" + date + "','" + time + "','" + fileName.getFileName() + "','deleted') ";
//                    writeDb(a);
                    watchDogService.createWatchDog(directoryPath, date, time, filename1, "deleted");
                }
            }
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
    public void choosePath() {
        Stage stage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) {
            txaDirectory.setText(textStart2);
            //No Directory selected
        } else {
            System.out.println(selectedDirectory.getAbsolutePath());
            directoryPath = selectedDirectory.getAbsolutePath();
            txtDirectory.setText(directoryPath);
        }
    }
    private static void givenWritingStringToFile_whenUsingFileOutputStream_thenCorrect(String logInput)
            throws IOException {
        String fileName = "OK.text";
        FileOutputStream outputStream = new FileOutputStream(fileName);
        byte[] strToBytes = logInput.getBytes();
        outputStream.write(strToBytes);
        outputStream.close();
    }

}
