package name.mlopatkin.andlogview.ui.logtable.menus;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import name.mlopatkin.andlogview.ui.logtable.Column;
import name.mlopatkin.andlogview.ui.logtable.TableRow;

/**
 * 创建人  liangsong
 * 创建时间 2024/08/20 9:51
 */
public class CopyColAction extends CellActionBase {
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    public CopyColAction(String name, String key) {
        super(name, key);
    }
    
    @Override
    public void setMouseData(Column c, TableRow row) {
        super.setMouseData(c, row);
        final Object value = c.getValue(row.getRowIndex(), row.getRecord());
        setEnabled(value!=null);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        final Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Object value = c.getValue(row.getRowIndex(), row.getRecord());
        if (value!=null) {
            if (value instanceof Date) {
                Date date = (Date) value;
                
                String formattedDateTime = sdf.format(date);
                systemClipboard.setContents(new StringSelection(formattedDateTime),null);
            }
            else {
                
                systemClipboard.setContents(new StringSelection(value.toString()),null);
            }
        }
        else {
            systemClipboard.setContents(new StringSelection(""),null);
        }
    }
    
  
}
