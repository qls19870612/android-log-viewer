package name.mlopatkin.andlogview.ui.logtable.menus;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.KeyStroke;

import name.mlopatkin.andlogview.ui.logtable.Column;
import name.mlopatkin.andlogview.ui.logtable.TableRow;

/**
 * 创建人  liangsong
 * 创建时间 2024/08/20 10:03
 */
public class CopyUrlAction extends CellActionBase {
    Pattern urlPattern = Pattern.compile("http(s?)://\\S+");
    private String url;
    
    public CopyUrlAction(String name, String key) {
        super(name, key);
    }
    
    @Override
    public void setMouseData(Column c, TableRow row) {
        super.setMouseData(c, row);
        final Object value = c.getValue(row.getRowIndex(), row.getRecord());
        if (value != null) {
            
            final String string = value.toString();
            final Matcher matcher = urlPattern.matcher(string);
            if (matcher.find()) {
                String url = matcher.group();
                int len = url.length();
                boolean hasXieGang = false;
                for (int i = "https://".length(); i < len; i++) {
                    final char ch = url.charAt(i);
                    if (!hasXieGang) {
                        if (ch == '/') {
                            hasXieGang = true;
                        }
                    } else {
                        if (ch == ':') {
                            url = url.substring(0, i);
                            break;
                        }
                    }
                }
                this.url = url;
                setEnabled(true);
                return;
            }
        }
        this.url = null;
        setEnabled(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        final Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        systemClipboard.setContents(new StringSelection(url), null);
        
        
    }
}
