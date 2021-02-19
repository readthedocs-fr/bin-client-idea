package fr.readthedocs.rtb;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.TextTransferable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class UploadAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String text;
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        String defaultExtension = "txt";
        if(Objects.nonNull(editor)) {
            text = editor.getSelectionModel().getSelectedText();
            if(text == null) {
                text = editor.getDocument().getText();
            }
            Editor textEditor = FileEditorManager.getInstance(Objects.requireNonNull(e.getProject())).getSelectedTextEditor();
            if(textEditor != null) {
                Document currentDoc = textEditor.getDocument();
                VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
                if(currentFile != null) {
                    defaultExtension = currentFile.getExtension();
                }
            }
        }else {
            Messages.showErrorDialog(e.getProject(), "Please open a file and/or select a piece of code to upload", "No Code Found");
            return;
        }
        LanguagePopupList languagePopupList = new LanguagePopupList(defaultExtension);
        JBPopupFactory.getInstance().createListPopup(languagePopupList, 5).showCenteredInCurrentWindow(Objects.requireNonNull(e.getProject()));
        String finalText = text;
        languagePopupList.doFinalStep(() -> languagePopupList.getSelectedValue().thenAccept(lang -> createAndSendBin(finalText, lang, e.getProject())));
    }

    private void createAndSendBin(String code, String lang, Project project) {
        try {
            BinCreator.createBin(code, lang, tuple -> {
                if(tuple.getT() >= 400 ) {
                    Messages.showErrorDialog(project, "Please report this to developers with the following code status : " + tuple.getT(), "An Error Occurred");
                    return;
                }
                storeBin(tuple.getU());
                String[] options = new String[]{"Copy", "Ok"};
                int option = Messages.showDialog(project, tuple.getU(), "Your Code", options, 0, Messages.getInformationIcon());
                if(option == 0) {
                    copyToClipboard(tuple.getU(), project);
                }
            });
        } catch (IOException | URISyntaxException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }

    private void copyToClipboard(String content, Project project) {
        CopyPasteManager.getInstance().setContents(new TextTransferable(content));
        Messages.showInfoMessage(project, "Done !", "URL Copy");
    }
    private void storeBin(String url) {
        File file = Constants.BINS_FILE;
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write("\n" + url);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
