package fr.readthedocs.rtb;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Hokkaydo on 15-01-2021.
 */
public class OpenStorageAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        File file = Constants.BINS_FILE;
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        StringBuilder bins = new StringBuilder();
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()) bins.append(scanner.nextLine()).append("\n");
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        String[] options = new String[]{"Ok", "Clear"};
        int option = Messages.showDialog(e.getProject(), bins.toString(), "Your Bins", options, 0, Messages.getInformationIcon());
        if(option == 1) {
            clearFile(file);
        }
    }

    private void clearFile(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
