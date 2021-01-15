package tech.hokkaydo.bindea;

import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Hokkaydo on 11-01-2021.
 */
public class LanguagePopupList extends BaseListPopupStep<String> {

    private static final Map<String, String> languages = new HashMap<>();

    static {
        languages.put("c", "c");
        languages.put("cpp", "cpp");
        languages.put("csharp", "cs");
        languages.put("css", "css");
        languages.put("diff", "diff");
        languages.put("erlang", "erl");
        languages.put("elixir", "ex");
        languages.put("go", "go");
        languages.put("objectivec", "h");
        languages.put("haskell", "hs");
        languages.put("html", "html");
        languages.put("ini", "ini");
        languages.put("java", "java");
        languages.put("javascript", "js");
        languages.put("json", "json");
        languages.put("kotlin", "kt");
        languages.put("less", "less");
        languages.put("lisp", "lisp");
        languages.put("lua", "lua");
        languages.put("md", "md");
        languages.put("php", "php");
        languages.put("perl", "pl");
        languages.put("python", "py");
        languages.put("ruby", "rb");
        languages.put("rust", "rs");
        languages.put("sass", "sass");
        languages.put("scala", "scala");
        languages.put("scss", "scss");
        languages.put("bash", "sh");
        languages.put("sql", "sql");
        languages.put("swift", "swift");
        languages.put("toml", "toml");
        languages.put("typescript", "ts");
        languages.put("text", "txt");
        languages.put("xml", "xml");
        languages.put("yaml", "yml");
    }

    public LanguagePopupList(String defaultExtension) {
        super("Language", new ArrayList<>(languages.keySet()));
        List<String> keySet = new ArrayList<>(languages.keySet());

        for (int i = 0; i < keySet.size(); i++) {
            if(languages.get(keySet.get(i)).equals(defaultExtension)) {
                setDefaultOptionIndex(i);
                return;
            }
        }
    }

    @Override
    public boolean isSpeedSearchEnabled() {
        return true;
    }
    private final CompletableFuture<String> selectedValue = new CompletableFuture<>();
    @Override
    public @Nullable PopupStep<?> onChosen(String selectedValue, boolean finalChoice) {
        if(finalChoice) this.selectedValue.complete(selectedValue);
        return PopupStep.FINAL_CHOICE;
    }

    public CompletableFuture<String> getSelectedValue() {
        return selectedValue;
    }
}
